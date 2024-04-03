package org.nekoweb.amycatgirl.revolt.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nekoweb.amycatgirl.revolt.ui.composables.ChatBubble
import org.nekoweb.amycatgirl.revolt.ui.composables.CustomTextField
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage() {
    var messageValue by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chatting with what?") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Add, "")
                    }
                    CustomTextField(
                        value = messageValue,
                        placeholder = { /*TODO*/ },
                        onValueChange = { messageValue = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.86f)
                    )
                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, "")
                    }
                },
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            ChatBubble("Amy", "Meow :3")
        }
    }
}

@Preview
@Composable
fun ChatPagePreview() {
    RevoltTheme {
        ChatPage()
    }
}