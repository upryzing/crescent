package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.PhonelinkLock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun MFADialog() {
    var mfaValue by rememberSaveable { mutableStateOf("") }

    Dialog(
        onDismissRequest = { /*TODO*/ },
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    Icons.Default.PhonelinkLock,
                    "",
                    modifier = Modifier
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    "MFA Required",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Please enter your MFA code provided by your authenticator app",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = mfaValue,
                    onValueChange = { mfaValue = it },
                    label = { Text("MFA Code") }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 24.dp)
                    .wrapContentSize(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text("Login")
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text("Cancel")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun MFADialogPreview() {
    RevoltTheme {
        MFADialog()
    }
}