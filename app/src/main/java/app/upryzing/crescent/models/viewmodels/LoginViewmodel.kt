package app.upryzing.crescent.models.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.datastore.ConfigDataStoreKeys
import app.upryzing.crescent.datastore.PreferenceDataStoreHelper
import app.upryzing.crescent.models.api.authentication.SessionResponse
import app.upryzing.crescent.ui.composables.LoginMFA
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString

class LoginViewmodel(
    private val client: ApiClient,
    private val navigation: NavController,
    context: Context
) :
    ViewModel() {
    private val preferenceDataStoreHelper = PreferenceDataStoreHelper(context)

    init {
        viewModelScope.launch {
            checkSession()
        }
    }

    private suspend fun checkSession() {
        Log.d("Login", "Login Launched")
        var currentSession: String = ""
        currentSession = preferenceDataStoreHelper.getFirstPreference(
            ConfigDataStoreKeys.SerializedCurrentSession,
            ""
        )

        Log.d("Login", currentSession)

        if (currentSession.isNotEmpty()) {
            Log.d("Login", "SerializedSession exists, attempting deserialization.")
            val availableSession = ApiClient.jsonDeserializer.decodeFromString<SessionResponse.Success>(
                currentSession
            )
            ApiClient.currentSession = availableSession

            ApiClient.startSession(availableSession)
            navigation.navigate("home") {
                popUpTo("auth") { inclusive = true }
            }
        } else {
            Log.d("Login", "SerializedSession does not exist.")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            when (val response = client.loginWithPassword(email, password)) {
                is SessionResponse.NeedsMultiFactorAuth ->
                    navigation.navigate(
                        LoginMFA(response.ticket)
                    )

                is SessionResponse.AccountDisabled -> println("Account has been disabled")
                is SessionResponse.Success -> {
                    val serializedSession = ApiClient.jsonDeserializer.encodeToString(response)
                    Log.d("Preferences", "Login Completed, saving current session")
                    preferenceDataStoreHelper.putPreference(
                        ConfigDataStoreKeys.SerializedCurrentSession,
                        serializedSession
                    )
                    navigation.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            }
        }
    }
}