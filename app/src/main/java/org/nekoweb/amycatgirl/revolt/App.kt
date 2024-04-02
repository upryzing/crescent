package org.nekoweb.amycatgirl.revolt

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun App(
    mainViewmodel: ViewModel
) {
    val navigator = rememberNavController()
    RevoltTheme {
        NavHost(navController = navigator, startDestination = "login") {
            composable("login") {
                Text("TODO")
            }
        }
    }
}