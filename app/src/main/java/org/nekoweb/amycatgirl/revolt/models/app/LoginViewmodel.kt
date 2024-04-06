package org.nekoweb.amycatgirl.revolt.models.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.authentication.SessionResponse

class LoginViewmodel(val client: ApiClient, val navigation: NavController) : ViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch {
            when (val response = client.loginWithPassword(email, password)) {
                is SessionResponse.NeedsMultiFactorAuth ->
                    navigation.navigate(
                        "auth/2fa/${response.ticket}"
                    )

                is SessionResponse.AccountDisabled -> println("Account has been disabled")
                is SessionResponse.Success -> navigation.navigate("home")
            }
        }
    }
}