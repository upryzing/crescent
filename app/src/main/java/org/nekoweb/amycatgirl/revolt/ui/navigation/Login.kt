package org.nekoweb.amycatgirl.revolt.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nekoweb.amycatgirl.revolt.R
import org.nekoweb.amycatgirl.revolt.ui.composables.AccountDisabledDialog
import org.nekoweb.amycatgirl.revolt.ui.composables.MFADialog
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun LoginPage() {
    var emailValue by rememberSaveable { mutableStateOf("") }
    var passwordValue by rememberSaveable { mutableStateOf("") }
    // TODO: Please implement when the login gets an error or it's requires MFA code. And plus, implement login functionality too.
    // TODO: fuck off :trl:
    // TODO: Switch to resource strings instead of hardcoded ones. It will make life easier
    AnimatedVisibility(visible = false) {
        MFADialog() {}
    }
    AnimatedVisibility(visible = false) {
        AccountDisabledDialog {}
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)
            OutlinedTextField(
                value = emailValue,
                onValueChange = { emailValue = it },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                label = { Text("Username") }
            )
            OutlinedTextField(
                value = passwordValue,
                onValueChange = { passwordValue = it },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                label = { Text("Password") }
            )
            FilledTonalButton(onClick = { /*TODO*/ }) {
                Text("Login")
            }
        }
    }
}

@Preview
@Composable
fun LoginPagePreview() {
    RevoltTheme {
        LoginPage()
    }
}
