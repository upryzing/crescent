package app.upryzing.crescent.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
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
import app.upryzing.crescent.R
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.User
import app.upryzing.crescent.models.api.channels.Channel
import app.upryzing.crescent.models.api.websocket.PartialMessage
import app.upryzing.crescent.models.viewmodels.ChatViewmodel
import app.upryzing.crescent.ui.composables.ChatBubble
import app.upryzing.crescent.ui.composables.CustomTextField
import app.upryzing.crescent.ui.composables.ProfileImage
import app.upryzing.crescent.ui.composables.SystemMessageDisplay
import app.upryzing.crescent.ui.theme.RevoltTheme
import app.upryzing.crescent.utilities.EventBus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ChatPage(
    viewmodel: ChatViewmodel,
    ulid: String,
    navigateToUserProfile: () -> Unit = {},
    goBack: () -> Unit
) {
    val user = ApiClient.cache[ulid]

    println("User: $user")

    var messageValue by remember { mutableStateOf("") }
    val messages = remember { viewmodel.messages }
    val navigator = rememberSupportingPaneScaffoldNavigator()

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

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileImage(
                        fallback = when (user) {
                            is User -> user.username
                            is Channel.Group -> user.name
                            else -> "Unknown"
                        }, url = avatar, size = 26.dp
                    )
                    Text(
                        text = when (user) {
                            is Channel.Group -> user.name
                            is User -> user.displayName ?: "${user.username}#${user.discriminator}"

                            else -> "Unknown"
                        }, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            },
                actions = {
                    IconButton(onClick = {
                        if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden) {
                            navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                        } else {
                            navigator.navigateBack()
                        }
                    }) {
                        if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden) {
                            Icon(painterResource(R.drawable.material_symbols_info), stringResource(R.string.chat_show_user_profile))
                        } else {
                            Icon(painterResource(R.drawable.material_symbols_filled_info), stringResource(R.string.chat_hide_user_profile))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.ui_go_back)
                        )
                    }

                })
        }, bottomBar = {
            Row(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .imePadding()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navigateToUserProfile() },
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
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(end = 7.dp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        value = messageValue,
                        placeholder = stringResource(R.string.chat_send_message),
                        onValueChange = { messageValue = it },
                        singleLine = false,
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .weight(1f)
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
                            modifier = Modifier
                                .size(42.dp)
                                .weight(1f),
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

    ) {
        SupportingPaneScaffold(
            modifier = Modifier.safeContentPadding().padding(it),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            mainPane = {
                AnimatedPane (Modifier.safeContentPadding()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        reverseLayout = true
                    ) {
                        items(messages) { message ->
                            val isSelf = message.authorId == ApiClient.currentSession?.userId

                            Box(modifier = Modifier.fillMaxWidth()) {
                                when (message.system != null) {
                                    true -> SystemMessageDisplay(message.system)
                                    false -> ChatBubble(
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
            },
            supportingPane = {
                AnimatedPane (Modifier.safeContentPadding()) {
                    Column(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        when (user) {
                            is User -> {
                                ProfileImage(
                                    fallback = user.username,
                                    url = avatar,
                                    presence = user.status?.presence
                                )
                                user.displayName?.let { it1 ->
                                    Text(
                                        it1,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                            else -> Text("Unknown", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun ChatPagePreview() {
    RevoltTheme {
        val viewmodel = viewModel {
            ChatViewmodel("")
        }
        ChatPage(viewmodel, "1", {}) {}
    }
}