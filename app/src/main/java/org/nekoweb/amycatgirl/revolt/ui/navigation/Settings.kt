package org.nekoweb.amycatgirl.revolt.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.BuildConfig
import org.nekoweb.amycatgirl.revolt.R
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.ui.composables.LogoutConfirmationDialog
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    goBack: () -> Unit,
    onSessionDropped: () -> Unit
) {
    var shouldShowDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    // TODO: Implement App configuration
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    AnimatedVisibility(visible = shouldShowDialog) {
        LogoutConfirmationDialog(
            logoutCallback = {
                scope.launch {
                    val result = ApiClient.dropSession()
                    when (result) {
                        true -> onSessionDropped()
                        false -> Log.e("App", "Logout failed, do something here")
                    }
                }
            },
            dismissCallback = { shouldShowDialog = false })
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text(stringResource(R.string.settings_header)) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.ui_go_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // TODO: Needed navigation for each categories
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Filled.LockPerson,
                        stringResource(R.string.settings_about)
                    )
                },
                headlineContent = { Text(stringResource(R.string.settings_account)) },
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Filled.Person,
                        stringResource(R.string.settings_profile)
                    )
                },
                headlineContent = { Text(stringResource(R.string.settings_profile)) },
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Settings,
                        stringResource(R.string.settings_client)
                    )
                },
                headlineContent = { Text(stringResource(R.string.settings_client)) },
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Info,
                        "${stringResource(R.string.settings_about)} ${stringResource(R.string.app_name)}"
                    )
                },
                headlineContent = {
                    Text(
                        "${stringResource(R.string.settings_about)} ${
                            stringResource(
                                R.string.app_name
                            )
                        }"
                    )
                },
            )
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(R.string.settings_logout),
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                headlineContent = {
                    Text(
                        stringResource(R.string.settings_logout),
                        color = MaterialTheme.colorScheme.error
                    )
                },
                modifier = Modifier.clickable { shouldShowDialog = true }
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.9f))
            Text(
                "${stringResource(R.string.app_name)} - Build ${BuildConfig.VERSION_CODE}",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun SettingsPagePreview() {
    RevoltTheme {
        SettingsPage({}, {})
    }
}