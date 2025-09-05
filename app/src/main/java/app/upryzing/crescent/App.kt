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
import app.upryzing.crescent.ui.composables.LoginMFA
import app.upryzing.crescent.ui.composables.MFADialog
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App(
    mainViewmodel: MainViewmodel = viewModel(),
    windowSizeClass: WindowSizeClass
) {
    val context = LocalContext.current
    val navigator = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        // TODO)) Rewrite the goddamn NavHost since it's a fucking mess right now.
        NavHost(navController = navigator, startDestination = "auth") {
            composable(
                "debug",
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
                }, navigateToDebugLogin = { navigator.navigate("auth") })
            }

            composable(
                "auth",
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
                    navigator.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                })
            }

            composable(
                "home",
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
                    navigateToChat = { navigator.navigate("messages/${it}") },
                    navigateToDebug = { navigator.navigate("debug") },
                    navigateToSettings = { navigator.navigate("settings") },
                    navigateToStartConversation = { navigator.navigate("home/start_conversation") },
                    windowSizeClass = windowSizeClass,
                )
            }

            composable(
                "home/start_conversation",
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
                "messages/{id}",
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
                "settings",
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
                    navigateToAccount = { navigator.navigate("settings/account") },
                    navigateToProfile = { navigator.navigate("settings/profile") },
                    navigateToClientSettings = { navigator.navigate("settings/client") },
                    navigateToAbout = { navigator.navigate("settings/about") },
                    onSessionDropped = {
                        navigator.navigate("auth") {
                            popUpTo("settings") { inclusive = true }
                        }
                    }
                )
            }
            composable(
                "settings/account",
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
                "settings/profile",
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
                "settings/client",
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
                "settings/about",
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