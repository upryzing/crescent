package org.nekoweb.amycatgirl.revolt

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.app.ChatViewmodel
import org.nekoweb.amycatgirl.revolt.models.app.HomeViewmodel
import org.nekoweb.amycatgirl.revolt.models.app.LoginViewmodel
import org.nekoweb.amycatgirl.revolt.models.app.MainViewmodel
import org.nekoweb.amycatgirl.revolt.ui.navigation.ChatPage
import org.nekoweb.amycatgirl.revolt.ui.navigation.DebugScreen
import org.nekoweb.amycatgirl.revolt.ui.navigation.HomePage
import org.nekoweb.amycatgirl.revolt.ui.navigation.LoginPage
import org.nekoweb.amycatgirl.revolt.ui.navigation.SettingsPage

@SuppressLint("RestrictedApi")
@Composable
fun App(
    mainViewmodel: MainViewmodel = viewModel()
) {
    val navigator = rememberNavController()

    navigator.addOnDestinationChangedListener { controller, _, _ ->
        Log.d("BackStackLog", "BackStack: ${controller.currentBackStack}")
    }
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navigator, startDestination = "auth") {
            composable("debug",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            200,
                            easing = CubicBezierEasing(0.005f, 0.7f, 0.1f, 1f)
                        ),
                    ) + slideInHorizontally(
                        tween(
                            200,
                            easing = CubicBezierEasing(0.005f, 0.7f, 0.1f, 1f)
                        )
                    ) {
                        1500
                    }
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                    ) + slideOutHorizontally(
                        tween(
                            200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        )
                    ) {
                        1500
                    }
                }) {
                DebugScreen(mainViewmodel.messageList, goBack = {
                    navigator.popBackStack()
                }, navigateToDebugLogin = { navigator.navigate("auth") })
            }

            composable("auth") {
                val viewmodel = viewModel {
                    LoginViewmodel(ApiClient, navigator)
                }
                LoginPage(viewmodel)
            }

            composable(
                "home",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            200,
                            easing = CubicBezierEasing(0.005f, 0.7f, 0.1f, 1f)
                        ),
                    ) + slideInHorizontally(
                        tween(
                            200,
                            easing = CubicBezierEasing(0.005f, 0.7f, 0.1f, 1f)
                        )
                    ) {
                        1500
                    }
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                    ) + slideOutHorizontally(
                        tween(
                            200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        )
                    ) {
                        1500
                    }
                }
            ) {
                val homeViewmodel = viewModel {
                    HomeViewmodel()
                }

                HomePage(
                    homeViewmodel,
                    navigateToChat = {
                        println("navigating to chat, id: $it")
                        navigator.navigate("messages/${it}")
                    },
                    navigateToDebug = { navigator.navigate("debug") },
                    navigateToSettings = { navigator.navigate("settings") }
                )
            }

            composable(
                "messages/{id}",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            200,
                            easing = CubicBezierEasing(0.005f, 0.7f, 0.1f, 1f)
                        ),
                    ) + slideInHorizontally(
                        tween(
                            200,
                            easing = CubicBezierEasing(0.005f, 0.7f, 0.1f, 1f)
                        )
                    ) {
                        1500
                    }
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                    ) + slideOutHorizontally(
                        tween(
                            200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        )
                    ) {
                        1500
                    }
                },
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                println("navigating to chat, id: ${backStackEntry.arguments?.getString("id")}")
                val viewmodel: ChatViewmodel = viewModel {
                    ChatViewmodel()
                }
                ChatPage(
                    viewmodel,
                    backStackEntry.arguments?.getString("id")!!
                ) { navigator.popBackStack() }
            }

            composable(
                "settings",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            200,
                            easing = CubicBezierEasing(0.005f, 0.7f, 0.1f, 1f)
                        ),
                    ) + slideInHorizontally(
                        tween(
                            200,
                            easing = CubicBezierEasing(0.005f, 0.7f, 0.1f, 1f)
                        )
                    ) {
                        1500
                    }
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                    ) + slideOutHorizontally(
                        tween(
                            200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        )
                    ) {
                        1500
                    }
                }
            ) {
                SettingsPage(goBack = { navigator.popBackStack() })
            }
        }
    }
}