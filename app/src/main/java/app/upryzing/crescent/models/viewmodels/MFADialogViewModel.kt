package app.upryzing.crescent.models.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.api.RevoltAPI
import app.upryzing.crescent.api.models.authentication.MFAResponse
import app.upryzing.crescent.api.models.authentication.SessionRequest
import app.upryzing.crescent.persistence.datastore.ConfigDataStoreKeys
import app.upryzing.crescent.persistence.datastore.PreferenceDataStoreHelper
import app.upryzing.crescent.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import javax.inject.Inject

enum class SelectedMethod {
    TWO_FACTOR_AUTHENTICATION,
    RECOVERY_CODE
}

@HiltViewModel
class MFADialogViewModel @Inject constructor(
    private val navigator: Navigator,
    private val clientNext: RevoltAPI,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val preferenceDataStoreHelper = PreferenceDataStoreHelper(context)

    suspend fun handleMFAMethod(method: SelectedMethod, code: String? = null) {
//        require(ticket != null)
//        require(useNext != null)
//
//        when (method) {
//            SelectedMethod.TWO_FACTOR_AUTHENTICATION -> {
//                require(code != null)
//                if (!useNext) {
//                    val response = ApiClient.confirm2FA(ticket, code)
//                    val serializedSession = ApiClient.jsonDeserializer.encodeToString(response)
//                    preferenceDataStoreHelper.putPreference(
//                        ConfigDataStoreKeys.SerializedCurrentSession,
//                        serializedSession
//                    )
//                } else {
//                    clientNext.session.createSession(
//                        SessionRequest.MultiFactor(
//                            ticket,
//                            MFAResponse.TwoFactorMFA(
//                                code
//                            )
//                        )
//                    )
//                }
//            }
//
//            else -> Log.w("MFADialogVM", "I can't handle this method yet!")
//        }
    }
}