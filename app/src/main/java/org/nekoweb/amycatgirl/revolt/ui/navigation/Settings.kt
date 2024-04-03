package org.nekoweb.amycatgirl.revolt.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
    // TODO: Implement App configuration
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text("Settings") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { /*TODO: Add navigation to return Home.kt*/ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // TODO: Needed navigation for each categories
            ListItem(
                leadingContent = { Icon(Icons.Default.Person, "") },
                headlineContent = { Text("Account") },
                supportingContent = { Text("Change your account settings") }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Brush, "") },
                headlineContent = { Text("Appearance") },
                supportingContent = { Text("Customize your app") }
            )
            ListItem(
                leadingContent = { Icon(Icons.Default.Settings, "") },
                headlineContent = { Text("General") },
                supportingContent = { Text("Configure your app") }
            )
            ListItem(
                leadingContent = { Icon(Icons.Default.Info, "") },
                headlineContent = { Text("About RevoltMini") },
                supportingContent = { Text("Information about this app") }
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.9f))
            Text(
                "RevoltMini - Development Build",
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
        SettingsPage()
    }
}