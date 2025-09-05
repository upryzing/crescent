package app.upryzing.crescent

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import app.upryzing.crescent.navigation.Routes // <-- Import Routes
import app.upryzing.crescent.ui.composables.LoginMFA
import app.upryzing.crescent.ui.composables.MFADialog
import app.upryzing.crescent.ui.navigation.AboutPage
import app.upryzing.crescent.ui.navigation.AccountSettingsPage
import app.upryzing.crescent.ui.navigation.ChatPage
import app.upryzing.crescent.ui.navigation.ClientSettingsPage
import app.upryzing.crescent.ui.navigation.DebugScreen
import app.upryzing.crescent.ui.navigation.HomePage
import app.upryzing.crescent.ui.navigation.LoginPage
import app.upryzing.crescent.ui.navigation.ProfileSettingsPage
import app.upryzing.crescent.ui.navigation.SettingsPage
import app.upryzing.crescent.ui.navigation.StartConversationPage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App(
    windowSizeClass: WindowSizeClass,
    mainViewmodel: MainViewmodel = viewModel()
) {
    val context = LocalContext.current
    val navigator = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navigator, startDestination = Routes.AUTH) {
            composable(
                Routes.DEBUG,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) {
                DebugScreen(mainViewmodel.messageList, goBack = {
                    navigator.popBackStack()
                }, navigateToDebugLogin = { navigator.navigate(Routes.AUTH) })
            }

            composable(
                Routes.AUTH,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) {
                val viewmodel = viewModel {
                    LoginViewmodel(ApiClient, navigator, context)
                }
                LoginPage(viewmodel)
            }
            dialog<LoginMFA> { backStackEntry ->
                val data: LoginMFA = backStackEntry.toRoute()
                MFADialog(data, dismissCallback = { navigator.popBackStack() }, successCallback = {
                    navigator.navigate(Routes.HOME) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                })
            }

            composable(
                Routes.HOME,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) {
                val homeViewmodel = viewModel {
                    HomeViewmodel()
                }

                HomePage(
                    homeViewmodel,
                    windowSizeClass,
                    navigateToChat = { navigator.navigate(Routes.Messages.destination(it)) },
                    navigateToDebug = { navigator.navigate(Routes.DEBUG) },
                    navigateToSettings = { navigator.navigate(Routes.SETTINGS) },
                    navigateToStartConversation = { navigator.navigate(Routes.START_CONVERSATION) }
                )
            }

            composable(
                Routes.START_CONVERSATION,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) {
                StartConversationPage(goBack = { navigator.popBackStack() })
            }

            composable(
                Routes.Messages.ROUTE_PATTERN,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                Log.d(
                    "Navigator",
                    "navigating to chat, id: ${backStackEntry.arguments?.getString("id")}"
                )
                val viewmodel: ChatViewmodel = viewModel {
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) {
                AccountSettingsPage(goBack = { navigator.popBackStack() })
            }
            composable(
                Routes.SettingsSubRoutes.PROFILE,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) {
                ProfileSettingsPage(goBack = { navigator.popBackStack() })
            }
            composable(
                Routes.SettingsSubRoutes.CLIENT,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) {
                ClientSettingsPage(goBack = { navigator.popBackStack() })
            }
            composable(
                Routes.SettingsSubRoutes.ABOUT,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) {
                AboutPage(goBack = { navigator.popBackStack() })
            }
        }
    }
}