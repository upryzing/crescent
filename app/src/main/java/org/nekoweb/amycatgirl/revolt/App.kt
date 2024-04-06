package org.nekoweb.amycatgirl.revolt

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.nekoweb.amycatgirl.revolt.models.app.HomeViewmodel
import org.nekoweb.amycatgirl.revolt.models.app.MainViewmodel
import org.nekoweb.amycatgirl.revolt.ui.navigation.ChatPage
import org.nekoweb.amycatgirl.revolt.ui.navigation.HomePage
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun App(
    mainViewmodel: MainViewmodel = viewModel()
) {
    val navigator = rememberNavController()
    val scroll = rememberScrollState()
    RevoltTheme {
        NavHost(navController = navigator, startDestination = "home") {
            composable("debug") {
                Column(modifier = Modifier.verticalScroll(scroll)) {
                    for (message in mainViewmodel.messageList) {
                        Text("$message")
                    }
                }
            }

            composable(
                "home",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                }
            ) {
                val homeViewmodel = viewModel {
                    HomeViewmodel(mainViewmodel.client)
                }

                HomePage(
                    homeViewmodel,
                    navigateToChat = { navigator.navigate("messages/${it}") },
                    navigateToDebug = { navigator.navigate("debug") }
                )
            }

            composable(
                "messages/{id}",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                ChatPage()
            }
        }
    }
}