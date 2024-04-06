package org.nekoweb.amycatgirl.revolt.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.nekoweb.amycatgirl.revolt.models.app.MainViewmodel
import org.nekoweb.amycatgirl.revolt.ui.composables.ChatBubble
import org.nekoweb.amycatgirl.revolt.ui.composables.CustomTextField
import org.nekoweb.amycatgirl.revolt.ui.composables.ProfileImage
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(
    mainViewmodel: MainViewmodel,
    ulid: String? = "0000000000000000000000",
    goBack: () -> Unit
) {
    val user = mainViewmodel.userList.find { it.id == ulid }
    var messageValue by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileImage(fallback = user?.username ?: "Unknown", size = 26.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("${user?.username ?: "Unknown"}#${user?.discriminator ?: "0000"}")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            ""
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Add, "", tint = MaterialTheme.colorScheme.onSurface)
                }
                CustomTextField(
                    value = messageValue,
                    placeholder = { Text("Write an message") },
                    onValueChange = { messageValue = it },
                    singleLine = false,
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth(.8f)
                        .heightIn(0.dp, 100.dp)
                )
                IconButton(
                    onClick = { /*TODO*/ },
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // TODO: Add LazyColumn so we can fetch the group/person messages :3
                ChatBubble("Amy", "Meow :3")
                ChatBubble("Amy", "Meow :3")
                ChatBubble("Amy", "Meow :3")
            }
        }

    }
}

@Preview
@Composable
fun ChatPagePreview() {
    RevoltTheme {
        val viewmodel = viewModel {
            MainViewmodel()
        }
        ChatPage(mainViewmodel = viewmodel, "0000000000000000000000") {}
    }
}