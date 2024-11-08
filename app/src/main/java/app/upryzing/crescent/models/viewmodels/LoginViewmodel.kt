package app.upryzing.crescent.models.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.api.RevoltAPI
import app.upryzing.crescent.api.models.authentication.SessionRequest
import app.upryzing.crescent.api.models.authentication.SessionResponse
import app.upryzing.crescent.api.models.authentication.SessionResponse.*
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
            val availableSession = ApiClient.jsonDeserializer.decodeFromString<SessionResponse.Success>(
                currentSession
            )
            ApiClient.currentSession = availableSession

            ApiClient.startSession(availableSession)
        } else {
            Log.d("Login", "SerializedSession does not exist.")
        }
    }

    suspend fun login(email: String, password: String): Navigator.NavTarget? {
        return viewModelScope.async(Dispatchers.IO) {
            return@async when(val response = clientNext?.session?.createSession(
                SessionRequest.Email(
                    email,
                    password
                )
            )) {
                is Success -> Navigator.NavTarget.Home

                is NeedsMultiFactorAuth -> Navigator.NavTarget.MFADialog(
                    ticket = response.ticket
                    // TODO: Allow methods
                )

                is AccountDisabled -> {
                    Log.d("Internal",  "TODO: Handle accountDisabled")
                    null
                }
                null -> {
                    Log.d("Internal", "Uhhhhhhhhhhhhhhhhh, that shouldn't happen! ;w;")
                    null
                }
            }
            }.await()
    }

    var showPassword by mutableStateOf(false)
    fun toggleShowPassword() {
        showPassword = !showPassword
    }
}
