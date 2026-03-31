package com.kongjak.koreatechboard.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kongjak.koreatechboard.ui.article.ArticleScreen
import com.kongjak.koreatechboard.ui.board.BoardScreen
import com.kongjak.koreatechboard.ui.home.HomeScreen
import com.kongjak.koreatechboard.ui.licenses.LicenseScreen
import com.kongjak.koreatechboard.ui.notice.Notice
import com.kongjak.koreatechboard.ui.search.SearchScreen
import com.kongjak.koreatechboard.ui.settings.SettingsScreen
import com.kongjak.koreatechboard.util.routes.MainRoute
import java.util.UUID

val items = listOf(
    MainRoute.Home,
    MainRoute.Board,
    MainRoute.Settings
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentRoute: String,
    isDarkTheme: Boolean,
    initDepartment: Int,
    userDepartment: Int,
    setExternalLink: (String) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = currentRoute
    ) {
        composable(MainRoute.Home.name) {
            HomeScreen(
                onArticleClick = { uuid, department ->
                    navController.navigate("${MainRoute.Article.name}/$uuid/$department")
                }
            )
        }
        composable(MainRoute.Board.name) {
            BoardScreen(
                initDepartment = initDepartment,
                userDepartment = userDepartment,
                onArticleClick = { uuid, department ->
                    navController.navigate("${MainRoute.Article.name}/$uuid/$department")
                },
                onSearch = { department, board, title ->
                    navController.navigate("${MainRoute.Search.name}/$department/$board/$title")
                }
            )
        }
        composable(MainRoute.Settings.name) {
            SettingsScreen(
                openLicenses = {
                    navController.navigate(MainRoute.Licenses.name)
                }
            )
        }
        composableWithAnimation(
            route = "${MainRoute.Article.name}/{uuid}/{department}"
        ) { backStackEntry ->
            val uuid = UUID.fromString(backStackEntry.arguments?.getString("uuid")!!)
            val department = backStackEntry.arguments?.getString("department")!!
            ArticleScreen(
                uuid = uuid,
                department = department,
                isDarkTheme = isDarkTheme,
                setExternalLink = setExternalLink
            )
        }
        composableWithAnimation(
            route = "${MainRoute.Search.name}/{department}/{board}/{title}"
        ) { backStackEntry ->
            val department = backStackEntry.arguments?.getString("department")!!
            val board = backStackEntry.arguments?.getString("board")!!
            val title = backStackEntry.arguments?.getString("title")!!
            SearchScreen(
                department = department,
                board = board,
                title = title,
                onArticleClick = { uuid, articleDepartment ->
                    navController.navigate("${MainRoute.Article.name}/$uuid/$articleDepartment")
                }
            )
        }
        composableWithAnimation(MainRoute.Notice.name) {
            Notice(
                onArticleClick = { uuid, department ->
                    navController.navigate("${MainRoute.Article.name}/$uuid/$department")
                }
            )
        }
        composable(MainRoute.Licenses.name) {
            LicenseScreen()
        }
    }
}

@Composable
fun KoreatechBoardNavigationBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.split("/")?.get(0)

    if (items.contains(MainRoute.valueOf(currentRoute ?: MainRoute.Home.name))) {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    label = {
                        Text(text = stringResource(id = item.stringResource))
                    },

                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = stringResource(id = item.stringResource)
                        )
                    },

                    selected = currentRoute == item.name,

                    alwaysShowLabel = true,

                    onClick = {
                        navController.navigate(item.name) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    // Control all the colors of the icon
                    colors = NavigationBarItemDefaults.colors()
                )
            }
        }
    }
}

@Composable
fun KoreatechBoardNavigationRail(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.split("/")?.get(0)

    if (items.contains(MainRoute.valueOf(currentRoute ?: MainRoute.Home.name))) {
        NavigationRail {
            items.forEach { item ->
                NavigationRailItem(
                    label = {
                        Text(text = stringResource(id = item.stringResource))
                    },

                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = stringResource(id = item.stringResource)
                        )
                    },
                    selected = currentRoute == item.name,
                    onClick = {
                        navController.navigate(item.name) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

fun NavGraphBuilder.composableWithAnimation(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        content = content
    )
}
