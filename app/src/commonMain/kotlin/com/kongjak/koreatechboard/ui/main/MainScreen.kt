package com.kongjak.koreatechboard.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kongjak.koreatechboard.ui.KoreatechBoardNavigationBar
import com.kongjak.koreatechboard.ui.KoreatechBoardNavigationRail
import com.kongjak.koreatechboard.ui.NavigationGraph
import com.kongjak.koreatechboard.ui.components.KoreatechBoardAppBar
import com.kongjak.koreatechboard.ui.components.KoreatechBoardAppBarAction
import com.kongjak.koreatechboard.util.getPlatformInfo
import com.kongjak.koreatechboard.util.openUrl
import com.kongjak.koreatechboard.util.routes.MainRoute
import koreatech_board.app.generated.resources.Res
import koreatech_board.app.generated.resources.content_description_notification
import koreatech_board.app.generated.resources.content_description_open_in_browser
import koreatech_board.app.generated.resources.ic_open_in_browser
import koreatech_board.app.generated.resources.open_in_browser_failed
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MainScreen(
    startDestination: String = MainRoute.Home.name,
    isDarkTheme: Boolean,
    isLargeScreen: Boolean,
    initDepartment: Int,
    userDepartment: Int,
    externalLink: String?,
    setExternalLink: (String) -> Unit
) {
    val navController = rememberNavController()

    val mainScreenRoutes = listOf(
        MainRoute.Home.name,
        MainRoute.Board.name,
        MainRoute.Settings.name
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route?.split("/")?.get(0)

    val canGoBack = if (currentRoute == null) {
        false
    } else {
        !(mainScreenRoutes.contains(currentRoute) || currentRoute == startDestination)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            val actionList = listOf(
                KoreatechBoardAppBarAction(
                    icon = Icons.Default.Notifications,
                    action = {
                        navController.navigate(MainRoute.Notice.name)
                    },
                    contentDescription = stringResource(Res.string.content_description_notification)
                )
            )
            val uriHandler = LocalUriHandler.current
            val openInBrowserFailedString = stringResource(Res.string.open_in_browser_failed)

            val externalLinkAction = listOf(
                KoreatechBoardAppBarAction(
                    icon = vectorResource(Res.drawable.ic_open_in_browser),
                    action = {
                        if (externalLink != null) {
                            openUrl(
                                uriHandler = uriHandler,
                                url = externalLink
                            )
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = openInBrowserFailedString,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    contentDescription = stringResource(
                        Res.string.content_description_open_in_browser
                    )
                )
            )

            KoreatechBoardAppBar(
                canGoBack = canGoBack,
                backAction = {
                    navController.navigateUp()
                },
                actionList = when (currentRoute) {
                    MainRoute.Notice.name -> emptyList()
                    MainRoute.Article.name -> externalLinkAction
                    else -> if (getPlatformInfo().isFirebaseSupported) actionList else emptyList()
                }
            )
        },
        bottomBar = {
            if (!isLargeScreen) {
                KoreatechBoardNavigationBar(navController = navController)
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { contentPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                if (isLargeScreen) {
                    KoreatechBoardNavigationRail(
                        navController = navController
                    )
                }
                NavigationGraph(
                    navController = navController,
                    currentRoute = startDestination,
                    isDarkTheme = isDarkTheme,
                    initDepartment = initDepartment,
                    userDepartment = userDepartment,
                    setExternalLink = setExternalLink,
                    showSnackbar = { message ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }
        }
    )
}
