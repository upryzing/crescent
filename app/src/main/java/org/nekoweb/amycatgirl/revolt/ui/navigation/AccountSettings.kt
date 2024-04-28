package org.nekoweb.amycatgirl.revolt.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.nekoweb.amycatgirl.revolt.R
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsPage() {
    var userName by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settings_account))
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.ui_go_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ListItem(
                headlineContent = { Text("Username", modifier = Modifier.padding(end = 12.dp)) },
                trailingContent = {
                    OutlinedTextField(
                        value = userName,
                        placeholder = { Text("Username") },
                        onValueChange = { userName = it },
                        trailingIcon = {
                            Text(
                                "#1234",
                                fontSize = 17.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(.7f)
                    )
                })

            ListItem(
                headlineContent = { Text("Email", modifier = Modifier.padding(end = 12.dp)) },
                supportingContent = { Text("example@domain.com") },
                trailingContent = {
                    Button(
                        onClick = { /*TODO*/ },
                    ) {
                        Text("Change Email")
                    }
                })

            ListItem(
                headlineContent = { Text("Password", modifier = Modifier.padding(end = 12.dp)) },
                supportingContent = {
//                  TODO: Find a way to convert the password to a star character.
                    Text("**********")
                },
                trailingContent = {
                    Button(
                        onClick = { /*TODO*/ },
                    ) {
                        Text("Change Password")
                    }
                })
        }
    }
}

@Preview
@Composable
fun AccountSettingsPagePreview() {
    RevoltTheme {
        AccountSettingsPage()
    }
}