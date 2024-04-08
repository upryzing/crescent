package org.nekoweb.amycatgirl.revolt.ui.navigation

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.nekoweb.amycatgirl.revolt.BuildConfig
import org.nekoweb.amycatgirl.revolt.R
import org.nekoweb.amycatgirl.revolt.ui.composables.LogoutConfirmationDialog
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    goBack: () -> Unit
) {
    var shouldShowDialog by remember { mutableStateOf(false) }
    // TODO: Implement App configuration
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    AnimatedVisibility(visible = shouldShowDialog) {
        LogoutConfirmationDialog(
            logoutCallback = { /*TODO*/ },
            dismissCallback = { shouldShowDialog = false })
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text("Settings") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // TODO: Needed navigation for each categories
            ListItem(
                leadingContent = { Icon(Icons.Filled.LockPerson, "") },
                headlineContent = { Text("Account") },
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Person, "") },
                headlineContent = { Text("Profile") },
            )
            ListItem(
                leadingContent = { Icon(Icons.Default.Settings, "") },
                headlineContent = { Text("Client Settings") },
            )
            ListItem(
                leadingContent = { Icon(Icons.Default.Info, "") },
                headlineContent = { Text("About ${stringResource(R.string.app_name)}") },
            )
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                headlineContent = {
                    Text(
                        "Log Out",
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
        SettingsPage {}
    }
}