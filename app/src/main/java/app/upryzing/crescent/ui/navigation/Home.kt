package app.upryzing.crescent.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import app.upryzing.crescent.R
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.Flags
import app.upryzing.crescent.models.api.User
import app.upryzing.crescent.models.api.channels.Channel
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
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Box(Modifier.zIndex(1f)) {
        AnimatedVisibility(
            visible = !homeViewmodel.channels.isNotEmpty(),
            modifier = Modifier.align(Alignment.Center),
            enter = scaleIn() + slideIn(
                animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
                initialOffset = {IntOffset(0, -60)}
            ),
            exit = scaleOut() + slideOut(
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                targetOffset = {IntOffset(0, -60)}
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
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
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
                    IconButton(onClick = { navigateToSettings() }) {
                        Icon(
                            painterResource(R.drawable.material_symbols_settings),
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
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
                        onClick = { navigateToStartConversation() }
                    ),
                    FloatingActionButtonListItem(
                        icon = {
                            Icon(
                                painterResource(R.drawable.material_symbols_group_add),
                                contentDescription = "Add"
                            )
                        },
                        text = stringResource(R.string.chat_fab_options_new_group),
                        onClick = { Log.d("Debug", "hi") }
                    ),
                ))


        }) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        ) {
            items(homeViewmodel.channels) { channel ->
                when (channel) {
                    is Channel.DirectMessage -> {
                        val author = remember {
                            ApiClient.cache.asIterable()
                                .filterIsInstance<Map.Entry<String, User>>()
                                .find { entry ->
                                    ApiClient.currentSession?.userId != entry.value.id &&
                                            channel.recipients.contains(entry.value.id)
                                }!!.value
                        }

                        Log.d("Cache", "Found author: $author in ${ApiClient.cache}")
                        if (channel.active && author.flags != Flags.DELETED.ordinal) {
                            PeopleListItem(
                                user = author,
                                status = author.status,
                                callback = { navigateToChat(channel.id) })
                        }
                    }

                    is Channel.Group -> {
                        PeopleListItem(
                            channel = channel,
                            callback = { navigateToChat(channel.id) })
                    }

                    else -> {}
                }
            }
        }
    }
}