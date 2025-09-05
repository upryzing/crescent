package app.upryzing.crescent.navigation.routes

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import app.upryzing.crescent.R
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.Flags
import app.upryzing.crescent.models.api.User
import app.upryzing.crescent.models.api.channels.Channel
import app.upryzing.crescent.models.viewmodels.ChatViewmodel
import app.upryzing.crescent.models.viewmodels.HomeViewmodel
import app.upryzing.crescent.ui.composables.FloatingActionButtonListItem
import app.upryzing.crescent.ui.composables.FloatingActionButtonWithOptions
import app.upryzing.crescent.ui.composables.PeopleListItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomePage(
    homeViewmodel: HomeViewmodel,
    windowSizeClass: WindowSizeClass,
    navigateToChat: (location: String) -> Unit,
    navigateToDebug: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToStartConversation: () -> Unit,
) {
    val singlePaneTopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var selectedChannelId by rememberSaveable { mutableStateOf<String?>(null) }
    val isTwoPane = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium

    @Composable
    fun MyAppBar(scrollBehavior: TopAppBarScrollBehavior) {
        MediumTopAppBar(
            title = {
                Text(
                    stringResource(R.string.app_name),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }, actions = {
                IconButton(onClick = { navigateToDebug() }) {
                    Icon(
                        painterResource(R.drawable.material_symbols_adb),
                        contentDescription = "Open Debug login screen"
                    )
                }
                IconButton(onClick = {
                    if (isTwoPane && selectedChannelId != null) {
                        selectedChannelId = null
                    }
                    navigateToSettings()
                }) {
                    Icon(
                        painterResource(R.drawable.material_symbols_settings),
                        contentDescription = null
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }

    @Composable
    fun MyFloatingActionButton() {
        FloatingActionButtonWithOptions(
            options = listOf(
                FloatingActionButtonListItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.material_symbols_person_add),
                            contentDescription = "Add"
                        )
                    },
                    text = stringResource(R.string.chat_fab_options_new_chat),
                    onClick = {
                        if (isTwoPane && selectedChannelId != null) {
                            selectedChannelId = null // Clear detail view when starting new chat in two-pane
                        }
                        navigateToStartConversation()
                    }
                ),
                FloatingActionButtonListItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.material_symbols_group_add),
                            contentDescription = "Add"
                        )
                    },
                    text = stringResource(R.string.chat_fab_options_new_group),
                    onClick = {
                        Log.d("Debug", "New group clicked")
                        // Potentially clear selectedChannelId here too if needed
                    }
                ),
            )
        )
    }

    @Composable
    fun ChatListContent(modifier: Modifier, contentPadding: PaddingValues) {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding
        ) {
            items(homeViewmodel.channels) { channel ->
                val onItemClickActual = {
                    if (isTwoPane) {
                        selectedChannelId = channel.id
                    } else {
                        navigateToChat(channel.id)
                    }
                }
                when (channel) {
                    is Channel.DirectMessage -> {
                        val author = remember(channel.recipients, ApiClient.cache.keys) {
                            ApiClient.cache.asIterable()
                                .filterIsInstance<Map.Entry<String, User>>()
                                .find { entry ->
                                    ApiClient.currentSession?.userId != entry.value.id &&
                                            channel.recipients.contains(entry.value.id)
                                }?.value
                        }

                        if (author != null && channel.active && author.flags != Flags.DELETED.ordinal) {
                            PeopleListItem(
                                user = author,
                                status = author.status,
                                callback = onItemClickActual
                            )
                        } else if (author == null) {
                            Log.w("HomePage", "DM Channel ${channel.id} has no suitable author found in cache.")
                        }
                    }

                    is Channel.Group -> {
                        PeopleListItem(
                            channel = channel,
                            callback = onItemClickActual
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    // Loading indicator overlay
    Box(Modifier.zIndex(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = !homeViewmodel.channels.isNotEmpty(),
            enter = scaleIn() + slideIn(
                animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
                initialOffset = { IntOffset(0, -60) }
            ),
            exit = scaleOut() + slideOut(
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                targetOffset = { IntOffset(0, -60) }
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingIndicator(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .then(
                // Only apply nested scroll for single pane's top app bar
                if (!isTwoPane) Modifier.nestedScroll(singlePaneTopAppBarScrollBehavior.nestedScrollConnection) else Modifier
            ),
        topBar = {
            if (!isTwoPane) {
                MyAppBar(scrollBehavior = singlePaneTopAppBarScrollBehavior)
            }
        },
        floatingActionButton = {
            if (!isTwoPane) {
                MyFloatingActionButton()
            }
        }
    ) { outerScaffoldPadding ->
        if (isTwoPane) {
            Row(
                Modifier
                    .padding(outerScaffoldPadding) // Apply main scaffold's padding to the Row
                    .fillMaxSize()
            ) {
                // Left Pane (Chat List with its own AppBar and FAB)
                val listPaneScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
                Scaffold(
                    modifier = Modifier
                        .weight(0.4f)
                        .nestedScroll(listPaneScrollBehavior.nestedScrollConnection),
                    topBar = { MyAppBar(scrollBehavior = listPaneScrollBehavior) },
                    floatingActionButton = { MyFloatingActionButton() } // FAB for list pane
                ) { listPaneInnerScaffoldPadding ->
                    ChatListContent(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = listPaneInnerScaffoldPadding
                    )
                }

                // Right Pane (Chat Detail)
                if (selectedChannelId != null) {
                    Box(modifier = Modifier.weight(0.6f).fillMaxSize()) {
                        val detailChatViewModel: ChatViewmodel = viewModel(
                            key = "detail_chat_vm_${selectedChannelId}",
                        ) {
                            ChatViewmodel(selectedChannelId!!)
                        }
                        ChatPage(
                            viewmodel = detailChatViewModel,
                            ulid = selectedChannelId!!,
                            goBack = { selectedChannelId = null }
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.weight(0.6f).fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.home_select_chat_text))
                    }
                }
            }
        } else { // Single pane
            ChatListContent(
                modifier = Modifier.fillMaxSize(),
                contentPadding = outerScaffoldPadding
            )
        }
    }
}
