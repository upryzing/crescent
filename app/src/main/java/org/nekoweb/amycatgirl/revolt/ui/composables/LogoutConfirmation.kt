package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun LogoutConfirmationDialog(logoutCallback: () -> Unit, dismissCallback: () -> Unit) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Log Out",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Are you really sure you want to log out?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Go ahead", color = MaterialTheme.colorScheme.onError)
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LogoutConfirmationDialogPreviewLight() {
    RevoltTheme {
        LogoutConfirmationDialog(
            logoutCallback = { println("logout") },
            dismissCallback = { println("Dismiss") }
        )
    }
}

@Preview
@Composable
fun LogoutConfirmationDialogPreviewDark() {
    RevoltTheme(darkTheme = true) {
        LogoutConfirmationDialog(
            logoutCallback = { println("logout") },
            dismissCallback = { println("Dismiss") }
        )
    }
}