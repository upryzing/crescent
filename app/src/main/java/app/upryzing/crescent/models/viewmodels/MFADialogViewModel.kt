package app.upryzing.crescent.models.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.persistence.datastore.ConfigDataStoreKeys
import app.upryzing.crescent.persistence.datastore.PreferenceDataStoreHelper
import kotlinx.serialization.encodeToString

enum class SelectedMethod {
    TWO_FACTOR_AUTHENTICATION,
    RECOVERY_CODE
}

class MFADialogViewModel(val ticket: String, context: Context) : ViewModel() {
    private val preferenceDataStoreHelper = PreferenceDataStoreHelper(context)

    suspend fun handleMFAMethod(method: SelectedMethod, code: String? = null) {
        when (method) {
            SelectedMethod.TWO_FACTOR_AUTHENTICATION -> {
                require(code != null)
                val response = ApiClient.confirm2FA(ticket, code)
                val serializedSession = ApiClient.jsonDeserializer.encodeToString(response)
                preferenceDataStoreHelper.putPreference(
                    ConfigDataStoreKeys.SerializedCurrentSession,
                    serializedSession
                )
            }

            else -> Log.w("MFADialogVM", "I can't handle this method yet!")
        }
    }
}