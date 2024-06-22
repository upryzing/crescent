package org.nekoweb.amycatgirl.revolt

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.viewmodels.ChatViewmodel
import org.nekoweb.amycatgirl.revolt.models.viewmodels.HomeViewmodel
import org.nekoweb.amycatgirl.revolt.models.viewmodels.LoginViewmodel
import org.nekoweb.amycatgirl.revolt.models.viewmodels.MainViewmodel
import org.nekoweb.amycatgirl.revolt.ui.navigation.ChatPage
import org.nekoweb.amycatgirl.revolt.ui.navigation.DebugScreen
import org.nekoweb.amycatgirl.revolt.ui.navigation.HomePage
import org.nekoweb.amycatgirl.revolt.ui.navigation.LoginPage
import org.nekoweb.amycatgirl.revolt.ui.navigation.SettingsPage

@Composable
fun App(
    mainViewmodel: MainViewmodel = viewModel()
) {
    val context = LocalContext.current
    val navigator = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navigator, startDestination = "auth") {
            composable("debug",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                }
            ) {
                DebugScreen(mainViewmodel.messageList, goBack = {
                    navigator.popBackStack()
                }, navigateToDebugLogin = { navigator.navigate("auth") })
            }

            composable("auth") {
                val viewmodel = viewModel {
                    LoginViewmodel(ApiClient, navigator, context)
                }
                LoginPage(viewmodel)
            }

            composable(
                "home",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
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
                    navigateToSettings = { navigator.navigate("settings") }
                )
            }

            composable(
                "messages/{id}",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
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
                    backStackEntry.arguments?.getString("id")!!
                ) { navigator.popBackStack() }
            }

            composable(
                "settings",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                }
            ) {
                SettingsPage(
                    goBack = { navigator.popBackStack() },
                    onSessionDropped = {
                        navigator.navigate("auth") {
                            popUpTo("settings") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}