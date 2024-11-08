package app.upryzing.crescent

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.api.models.authentication.SessionRequest
import app.upryzing.crescent.api.models.authentication.SessionResponse
import app.upryzing.crescent.models.viewmodels.ChatViewmodel
import app.upryzing.crescent.models.viewmodels.HomeViewmodel
import app.upryzing.crescent.models.viewmodels.LoginViewmodel
import app.upryzing.crescent.models.viewmodels.MFADialogViewModel
import app.upryzing.crescent.models.viewmodels.MainViewmodel
import app.upryzing.crescent.ui.composables.MFADialog
import app.upryzing.crescent.ui.navigation.ChatPage
import app.upryzing.crescent.ui.navigation.DebugScreen
import app.upryzing.crescent.ui.navigation.HomePage
import app.upryzing.crescent.ui.navigation.LoginPage
import app.upryzing.crescent.ui.navigation.Navigator
import app.upryzing.crescent.ui.navigation.ProfileSettingsPage
import app.upryzing.crescent.ui.navigation.SettingsPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun App(
    mainViewmodel: MainViewmodel = viewModel(),
) {
    val context = LocalContext.current
    val navController = rememberNavController()


    val newNavigator = Navigator()

    LaunchedEffect("navigation") {
        newNavigator.sharedFlow.onEach {
            navController.navigate(it)
        }.launchIn(this)
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "debug/next", enterTransition = {
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
            }) {
            composable("debug") {
                DebugScreen(mainViewmodel.messageList, goBack = {
                    navController.popBackStack()
                }, navigateToDebugLogin = { navController.navigate("auth") })
            }

            composable<Navigator.NavTarget.Login> {
                val viewmodel = hiltViewModel<LoginViewmodel>()
                LoginPage(newNavigator, viewmodel)
            }

            dialog<Navigator.NavTarget.MFADialog> { backStackEntry ->
                val data: Navigator.NavTarget.MFADialog = backStackEntry.toRoute()
                val viewmodel: MFADialogViewModel = hiltViewModel()
                MFADialog(
                    data,
                    dismissCallback = { navController.popBackStack() },
                    successCallback = {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }, viewmodel)
            }

            composable(
                "home"
            ) {
                val homeViewmodel = viewModel {
                    HomeViewmodel()
                }

                HomePage(
                    homeViewmodel,
                    navigateToChat = { navController.navigate("messages/${it}") },
                    navigateToDebug = { navController.navigate("debug") },
                    navigateToSettings = { navController.navigate("settings") },
                    navigateToStartConversation = { navigator.navigate("home/startconversation") }
                )
            }

            composable(
                "home/startconversation",
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
                StartConversationPage (goBack = { navigator.popBackStack() })
            }

            composable(
                "messages/{id}",
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
                        navController.popBackStack()
                    }
                )
            }

            composable(
                "settings"
            ) {
                SettingsPage(
                    goBack = { navController.popBackStack() },
                    navigateToAccount = {},
                    navigateToProfile = { navController.navigate("settings/profile") },
                    onSessionDropped = {
                        navController.navigate("auth") {
                            popUpTo("settings") { inclusive = true }
                        }
                    }
                )
            }
            composable("settings/profile") {
                ProfileSettingsPage(goBack = { navController.popBackStack() })
            }

            composable("debug/next") {
                var clientConnectionInformation = mainViewmodel.getClientInformation()
                val coroutineScope = rememberCoroutineScope()

                var emailValue = remember { mutableStateOf("") }
                var passwordValue = remember { mutableStateOf("") }

                var doesNeedMFA = remember { mutableStateOf(false) }
                var response: SessionResponse.NeedsMultiFactorAuth? = null

                Scaffold { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        Text("API CONFIGURATION FOR api.revolt.chat:")
                        Text("$clientConnectionInformation", softWrap = true)

                        Button(onClick = {
                            clientConnectionInformation = mainViewmodel.getClientInformation()

                            Log.d("API", "$clientConnectionInformation")
                        }) {
                            Text("Refresh")
                        }

                        TextField(
                            value = emailValue.value,
                            onValueChange = { value -> emailValue.value = value },
                            placeholder = { Text("Email") }
                        )


                        TextField(
                            value = passwordValue.value,
                            onValueChange = { value -> passwordValue.value = value },
                            placeholder = { Text("password") }
                        )

                        Button(onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                val result = mainViewmodel.clientNext.session.createSession(
                                    SessionRequest.Email(
                                        emailValue.value,
                                        passwordValue.value
                                    )
                                )

                                if (result is SessionResponse.NeedsMultiFactorAuth) {
                                    doesNeedMFA.value = true
                                    response = result
                                } else if (result is SessionResponse.Success) {
                                    Log.d("API", "Logged in! Token: ${mainViewmodel.clientNext.session.currentSession?.userToken} | User: ${mainViewmodel.clientNext.self?.user?.username}")
                                }
                            }
                        }) { Text("Test login with email/password") }

                        if (doesNeedMFA.value) {
                            Text("Account reports that it needs 2 factor auth")
                        }
                    }
                }
            }
        }
    }
}