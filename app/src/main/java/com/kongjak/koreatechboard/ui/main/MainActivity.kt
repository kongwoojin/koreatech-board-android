package com.kongjak.koreatechboard.ui.main

import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.messaging.FirebaseMessaging
import com.kongjak.koreatechboard.BuildConfig
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.ui.KoreatechBoardNavigationBar
import com.kongjak.koreatechboard.ui.KoreatechBoardNavigationRail
import com.kongjak.koreatechboard.ui.NavigationGraph
import com.kongjak.koreatechboard.ui.components.KoreatechBoardAppBar
import com.kongjak.koreatechboard.ui.components.KoreatechBoardAppBarAction
import com.kongjak.koreatechboard.ui.permission.CheckNotificationPermission
import com.kongjak.koreatechboard.ui.theme.KoreatechBoardTheme
import com.kongjak.koreatechboard.util.KoreatechBoardAnalytics
import com.kongjak.koreatechboard.util.isDebug
import com.kongjak.koreatechboard.util.routes.MainRoute
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val crashlytics = Firebase.crashlytics
        crashlytics.setCustomKeys {
            key("build_type", BuildConfig.BUILD_TYPE)
        }

        if (isDebug()) {
            getFirebaseToken(this)
            crashlytics.setCrashlyticsCollectionEnabled(false)
        } else {
            crashlytics.setCrashlyticsCollectionEnabled(true)
        }

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val isLargeScreen = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact

            var startDestination by remember { mutableStateOf(MainRoute.Home.name) }

            val defaultScreen = intent.getStringExtra("screen")

            if (defaultScreen != null) {
                startDestination = MainRoute.valueOf(defaultScreen).name
            }

            if (startDestination == MainRoute.Notice.name) {
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                manager.cancelAll()
            }

            mainViewModel.collectSideEffect { mainViewModel.handleSideEffect(it) }

            val uiState by mainViewModel.collectAsState()
            val isDynamicColor = uiState.isDynamicTheme
            val isDarkTheme = uiState.isDarkTheme ?: isSystemInDarkTheme()
            val initDestination = uiState.initDepartment
            val userDestination = uiState.userDepartment

            KoreatechBoardTheme(
                dynamicColor = isDynamicColor,
                darkTheme = isDarkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        CheckNotificationPermission(
                            onPermissionGranted = {
                                mainViewModel.sendSideEffect(MainSideEffect.SetSubscribe(true))
                            }
                        )
                    }
                    MainScreen(
                        startDestination = startDestination,
                        isDarkTheme = isDarkTheme,
                        isLargeScreen = isLargeScreen,
                        initDepartment = initDestination,
                        userDepartment = userDestination,
                        externalLink = uiState.externalLink,
                        setExternalLink = { url ->
                            mainViewModel.setExternalLink(url)
                        }
                    )
                }
            }
        }
    }
}

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
    val context = LocalContext.current

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

    Scaffold(
        topBar = {
            val actionList = listOf(
                KoreatechBoardAppBarAction(
                    icon = Icons.Default.Notifications,
                    action = {
                        navController.navigate(MainRoute.Notice.name)
                    },
                    contentDescription = stringResource(id = R.string.content_description_notification)
                )
            )

            val externalLinkAction = listOf(
                KoreatechBoardAppBarAction(
                    icon = ImageVector.vectorResource(R.drawable.ic_open_in_browser),
                    action = {
                        if (externalLink != null) {
                            FirebaseAnalytics.getInstance(context)
                                .logEvent(KoreatechBoardAnalytics.UserAction.OPEN_IN_BROWSER) {
                                    param(KoreatechBoardAnalytics.Param.ARTICLE_URL, externalLink)
                                }
                            val builder = CustomTabsIntent.Builder()
                            val customTabsIntent = builder.build()
                            customTabsIntent.launchUrl(context, Uri.parse(externalLink))
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.open_in_browser_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    contentDescription = stringResource(
                        id = R.string.content_description_open_in_browser
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
                    else -> actionList
                }
            )
        },
        bottomBar = {
            if (!isLargeScreen) {
                KoreatechBoardNavigationBar(navController = navController)
            }
        },
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
                    setExternalLink = setExternalLink
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    )
}

fun getFirebaseToken(context: Context) {
    val tag = "FCM"
    FirebaseMessaging.getInstance().token.addOnCompleteListener(
        OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(tag, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            val msg = context.getString(R.string.firebase_token, token)
            Log.d(tag, msg)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    )
}
