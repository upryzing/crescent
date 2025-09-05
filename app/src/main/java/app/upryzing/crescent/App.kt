package app.upryzing.crescent

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.viewmodels.ChatViewmodel
import app.upryzing.crescent.models.viewmodels.HomeViewmodel
import app.upryzing.crescent.models.viewmodels.LoginViewmodel
import app.upryzing.crescent.models.viewmodels.MainViewmodel
import app.upryzing.crescent.navigation.Routes
import app.upryzing.crescent.navigation.routes.AboutPage
import app.upryzing.crescent.navigation.routes.AccountSettingsPage
import app.upryzing.crescent.navigation.routes.ChatPage
import app.upryzing.crescent.navigation.routes.ClientSettingsPage
import app.upryzing.crescent.navigation.routes.DebugScreen
import app.upryzing.crescent.navigation.routes.HomePage
import app.upryzing.crescent.navigation.routes.LoginPage
import app.upryzing.crescent.navigation.routes.ProfileSettingsPage
import app.upryzing.crescent.navigation.routes.SettingsPage
import app.upryzing.crescent.navigation.routes.StartConversationPage
import app.upryzing.crescent.ui.composables.LoginMFA
import app.upryzing.crescent.ui.composables.MFADialog

const val ANIMATION_DURATION = 300

fun m3EnterTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
}

fun m3ExitTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
}

fun m3PopEnterTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
}

fun m3PopExitTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App(
    mainViewmodel: MainViewmodel = viewModel(),
    windowSizeClass: WindowSizeClass
) {
    val context = LocalContext.current
    val navigator = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(
            navController = navigator,
            startDestination = Routes.AUTH
        ) {
            composable(
                Routes.DEBUG,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                DebugScreen(mainViewmodel.messageList, goBack = {
                    navigator.popBackStack()
                }, navigateToDebugLogin = { navigator.navigate(Routes.AUTH) })
            }

            composable(
                Routes.AUTH,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                val viewmodel = viewModel {
                    LoginViewmodel(ApiClient, navigator, context)
                }
                LoginPage(viewmodel)
            }

            dialog(Routes.AuthSubRoutes.MFA) { backStackEntry ->
                val data: LoginMFA = backStackEntry.toRoute()
                MFADialog(data, dismissCallback = { navigator.popBackStack() }, successCallback = {
                    navigator.navigate(Routes.HOME) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                })
            }

            composable(
                Routes.HOME,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                val homeViewmodel = viewModel {
                    HomeViewmodel()
                }
                HomePage(
                    homeViewmodel,
                    windowSizeClass = windowSizeClass,
                    navigateToChat = { navigator.navigate(Routes.Messages.destination(it)) },
                    navigateToDebug = { navigator.navigate(Routes.DEBUG) },
                    navigateToSettings = { navigator.navigate(Routes.SETTINGS) },
                    navigateToStartConversation = { navigator.navigate(Routes.START_CONVERSATION) }
                )
            }

            composable(
                Routes.START_CONVERSATION,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                StartConversationPage(goBack = { navigator.popBackStack() })
            }

            composable(
                Routes.Messages.ROUTE_PATTERN,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) { backStackEntry ->
                Log.d(
                    "Navigator",
                    "navigating to chat, id: ${backStackEntry.arguments?.getString("id")}"
                )
                val viewmodel: ChatViewmodel = viewModel(
                     key = "chat_vm_${backStackEntry.arguments?.getString("id")!!}" // Ensure unique key for ViewModel
                ) {
                    ChatViewmodel(backStackEntry.arguments?.getString("id")!!)
                }
                ChatPage(
                    viewmodel,
                    backStackEntry.arguments?.getString("id")!!,
                    goBack = {
                        navigator.popBackStack()
                    }
                )
            }

            composable(
                Routes.SETTINGS,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                SettingsPage(
                    goBack = { navigator.popBackStack() },
                    navigateToAccount = { navigator.navigate(Routes.SettingsSubRoutes.ACCOUNT) },
                    navigateToProfile = { navigator.navigate(Routes.SettingsSubRoutes.PROFILE) },
                    navigateToClientSettings = { navigator.navigate(Routes.SettingsSubRoutes.CLIENT) },
                    navigateToAbout = { navigator.navigate(Routes.SettingsSubRoutes.ABOUT) },
                    onSessionDropped = {
                        navigator.navigate(Routes.AUTH) {
                            popUpTo(Routes.SETTINGS) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                Routes.SettingsSubRoutes.ACCOUNT,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                AccountSettingsPage(goBack = { navigator.popBackStack() })
            }
            composable(
                Routes.SettingsSubRoutes.PROFILE,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                ProfileSettingsPage(goBack = { navigator.popBackStack() })
            }
            composable(
                Routes.SettingsSubRoutes.CLIENT,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                ClientSettingsPage(goBack = { navigator.popBackStack() })
            }
            composable(
                Routes.SettingsSubRoutes.ABOUT,
                enterTransition = { m3EnterTransition() },
                exitTransition = { m3ExitTransition() },
                popEnterTransition = { m3PopEnterTransition() },
                popExitTransition = { m3PopExitTransition() }
            ) {
                AboutPage(goBack = { navigator.popBackStack() })
            }
        }
    }
}
