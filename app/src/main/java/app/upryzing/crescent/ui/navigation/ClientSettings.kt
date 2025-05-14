package app.upryzing.crescent.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.upryzing.crescent.R
import app.upryzing.crescent.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientSettingsPage(goBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_client)) },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(
                                R.string.ui_go_back
                            )
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ListItem(
                    headlineContent = { Text("Emoji Picker") },
                    supportingContent = { Text("Enabled by default, disabling will disable the emoji picker.") },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.material_symbols_mood_outlined),
                            ""
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = false,
                            onCheckedChange = {/* TODO */ }
                        )
                    }
                )
                ListItem(
                    headlineContent = { Text("Push Notifications") },
                    supportingContent = { Text("Receive push notifications while the app is closed.") },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.material_symbols_notifications_outlined),
                            ""
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = false,
                            onCheckedChange = {/* TODO */ }
                        )
                    }
                )
                ListItem(
                    headlineContent = { Text("Low Data Mode") },
                    supportingContent = { Text("Enabling this will reduce image quality while saving data usage") },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.material_symbols_network_cell_outlined),
                            ""
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = false,
                            onCheckedChange = {/* TODO */ }
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ClientSettingsPagePreview() {
    RevoltTheme {
        ClientSettingsPage {}
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun ClientSettingsPagePreviewTablet() {
    RevoltTheme {
        ClientSettingsPage {}
    }
}