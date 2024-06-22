package org.nekoweb.amycatgirl.revolt.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object ConfigDataStoreKeys {
    val SerializedCurrentSession = stringPreferencesKey("CURRENT_SESSION")
    // TODO: Think of features we can add and add them to a todo list
}