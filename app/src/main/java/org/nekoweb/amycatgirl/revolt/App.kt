package org.nekoweb.amycatgirl.revolt

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    RevoltTheme {
        NavHost(navController = navigator, startDestination = "home") {
            composable("login") {
                Column {
                    for (message in mainViewmodel.messageList) {
                        Text("Message: ${message.authorId}/${message.content}")
                    }
                }
            }

            composable("home") {
                val homeViewmodel = viewModel {
                    HomeViewmodel(mainViewmodel.client)
                }

                HomePage(
                    homeViewmodel,
                    navigateToChat = { navigator.navigate("messages/${it}") },
                    navigateToDebug = { navigator.navigate("login") }
                )
            }

            composable("messages/{id}") {
                ChatPage()
            }
        }
    }
}