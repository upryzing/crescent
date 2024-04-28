package org.nekoweb.amycatgirl.revolt.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.R
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.User
import org.nekoweb.amycatgirl.revolt.models.api.channels.Channel
import org.nekoweb.amycatgirl.revolt.models.api.websocket.PartialMessage
import org.nekoweb.amycatgirl.revolt.models.viewmodels.ChatViewmodel
import org.nekoweb.amycatgirl.revolt.ui.composables.ChatBubble
import org.nekoweb.amycatgirl.revolt.ui.composables.CustomTextField
import org.nekoweb.amycatgirl.revolt.ui.composables.ProfileImage
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(
    viewmodel: ChatViewmodel, ulid: String, goBack: () -> Unit
) {
    val user = ApiClient.cache[ulid]

    println("User: $user")

    var messageValue by remember { mutableStateOf("") }
    val messages = remember { viewmodel.messages }

    val avatar = when (user) {
        is Channel.Group -> "${ApiClient.S3_ROOT_URL}icons/${user.icon?.id}?max_side=256"
        is User -> "${ApiClient.S3_ROOT_URL}avatars/${user.avatar?.id}?max_side=256"
        else -> null
    }


    val scope = rememberCoroutineScope()

    LaunchedEffect(ulid) {
        EventBus.subscribe<PartialMessage> {
            if (it.channelId == ulid) {
                messages.add(0, it)
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        topBar = {
        CenterAlignedTopAppBar(title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage(
                    fallback = when (user) {
                        is User -> user.username
                        is Channel.Group -> user.name
                        else -> "Unknown"
                    }, url = avatar, size = 26.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when (user) {
                        is Channel.Group -> user.name
                        is User -> user.displayName ?: "${user.username}#${user.discriminator}"

                        else -> "Unknown"
                    }, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        }, navigationIcon = {
            IconButton(onClick = { goBack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.ui_go_back)
                )
            }
        })
    }, bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .imePadding()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .width(IntrinsicSize.Min),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(
                    painterResource(R.drawable.material_symbols_library_add),
                    "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = messageValue,
                    placeholder = { Text(stringResource(R.string.chat_send_message)) },
                    onValueChange = { messageValue = it },
                    singleLine = false,
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth(.8f)
                        .heightIn(0.dp, 100.dp)
                )
                AnimatedVisibility(
                    visible = messageValue.isNotBlank(),
                    enter = scaleIn(animationSpec = tween(300)),
                    exit = scaleOut(animationSpec = tween(200))
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                ApiClient.sendMessage(ulid, messageValue)
                                messageValue = ""
                            }
                        },
                        modifier = Modifier.size(42.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            painterResource(R.drawable.material_symbols_send),
                            "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = innerPadding,
            reverseLayout = true
        ) {
            items(messages) { message ->
                val isSelf = message.authorId == ApiClient.currentSession?.userId

                Box(modifier = Modifier.fillMaxWidth()) {
                    ChatBubble(
                        message,
                        modifier = if (isSelf)
                            Modifier.align(Alignment.BottomEnd)
                        else
                            Modifier.align(Alignment.BottomStart),
                        isSelf
                    )
                }
            }

        }

    }
}

@Preview
@Composable
fun ChatPagePreview() {
    RevoltTheme {
        val viewmodel = viewModel {
            ChatViewmodel("")
        }
        ChatPage(viewmodel, "1") {}
    }
}