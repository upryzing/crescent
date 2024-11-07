package app.upryzing.crescent.models.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.persistence.datastore.ConfigDataStoreKeys
import app.upryzing.crescent.persistence.datastore.PreferenceDataStoreHelper
import app.upryzing.crescent.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(
    private val clientNext: RevoltAPI? = null,
    @ApplicationContext private val context: Context
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
            val availableSession = ApiClient.jsonDeserializer.decodeFromString<app.upryzing.crescent.api.models.authentication.SessionResponse.Success>(
                currentSession
            )
            ApiClient.currentSession = availableSession

            ApiClient.startSession(availableSession)
        } else {
            Log.d("Login", "SerializedSession does not exist.")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            when (val response = client.loginWithPassword(email, password)) {
                is app.upryzing.crescent.api.models.authentication.SessionResponse.NeedsMultiFactorAuth ->
                    navigation.navigate(
                        LoginMFA(response.ticket)
                    )

                is app.upryzing.crescent.api.models.authentication.SessionResponse.AccountDisabled -> println("Account has been disabled")
                is app.upryzing.crescent.api.models.authentication.SessionResponse.Success -> {
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
            }.await()
    }

    var showPassword by mutableStateOf(false)
    fun toggleShowPassword() {
        showPassword = !showPassword
    }
}
