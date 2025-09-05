package app.upryzing.crescent.navigation.routes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun ChatPage(
    viewmodel: ChatViewmodel,
    ulid: String,
    navigateToUserProfile: () -> Unit = {},
    goBack: () -> Unit
) {
    val user by remember(ulid, ApiClient.cache.keys) { mutableStateOf(ApiClient.cache[ulid]) }

    var messageValue by remember { mutableStateOf("") }
    val messages = viewmodel.messages

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val chatAvatarUrl by remember(user) {
        mutableStateOf(
            when (val u = user) {
                is Channel.Group -> u.icon?.id?.let { iconId -> "${ApiClient.S3_ROOT_URL}icons/$iconId?max_side=256" }
                is User -> u.avatar?.id?.let { avatarId -> "${ApiClient.S3_ROOT_URL}avatars/$avatarId?max_side=256" }
                else -> null
            }
        )
    }

    val chatDisplayName by remember(user) {
        mutableStateOf(
            when (val u = user) {
                is User -> u.displayName ?: "${u.username}#${u.discriminator}"
                is Channel.Group -> u.name
                else -> "Unknown"
            }
        )
    }

    val chatProfileFallbackName by remember(user) {
        mutableStateOf(
            when (val u = user) {
                is User -> u.username
                is Channel.Group -> u.name
                else -> "Unknown"
            }
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(ulid, viewmodel) {
        viewmodel.setCurrentChatId(ulid)
    }

    DisposableEffect(ulid, viewmodel) {
        val messageHandler: (PartialMessage) -> Unit = { message ->
            if (message.channelId == ulid) {
                viewmodel.messages.add(0, message)
            }
        }
        EventBus.subscribe(messageHandler)
        onDispose {
            EventBus.unsubscribe(messageHandler)
        }
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
                            fallback = chatProfileFallbackName,
                            url = chatAvatarUrl,
                            size = 26.dp
                        )
                        Text(
                            text = chatDisplayName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            painterResource(R.drawable.material_symbols_info),
                            stringResource(R.string.chat_show_user_profile)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.ui_go_back)
                        )
                    }
                }
            )
        }, bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
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
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
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
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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
                        enter = scaleIn(animationSpec = motionScheme.slowEffectsSpec()),
                        exit = scaleOut(animationSpec = motionScheme.slowSpatialSpec())
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
                                .weight(1f), // This was .weight(1f), but IconButton is fixed size. Check if this is intended.
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
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
                            modifier = if (isSelf) Modifier.align(Alignment.BottomEnd)
                            else Modifier.align(Alignment.BottomStart),
                            isSelf
                        )
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                when (val u = user) {
                    is User -> {
                        ProfileImage(
                            fallback = chatProfileFallbackName,
                            url = chatAvatarUrl,
                            presence = u.status?.presence
                        )
                        Text(
                            chatDisplayName,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    is Channel.Group -> {
                        ProfileImage(
                            fallback = chatProfileFallbackName,
                            url = chatAvatarUrl
                        )
                        Text(
                            chatDisplayName,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    else -> Text("Information not available", style = MaterialTheme.typography.headlineMedium)
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
        ChatPage(viewmodel, "1", {}) {}
    }
}
