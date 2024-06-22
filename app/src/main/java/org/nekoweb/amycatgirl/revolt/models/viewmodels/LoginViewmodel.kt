package org.nekoweb.amycatgirl.revolt.models.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.datastore.ConfigDataStoreKeys
import org.nekoweb.amycatgirl.revolt.datastore.PreferenceDataStoreHelper
import org.nekoweb.amycatgirl.revolt.models.api.authentication.SessionResponse

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
            ApiClient.currentSession =
                ApiClient.jsonDeserializer.decodeFromString<SessionResponse.Success>(
                    currentSession
                )
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
                        "auth/2fa/${response.ticket}"
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