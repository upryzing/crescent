package app.upryzing.crescent.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Square
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.User
import app.upryzing.crescent.models.api.UserStatus
import app.upryzing.crescent.models.api.channels.Channel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PeopleListItem(
    user: User? = null,
    channel: app.upryzing.crescent.api.models.channels.Channel.Group? = null,
    status: Status? = null,
    unreads: Int? = null,
    callback: (() -> Unit)
) {
    // BottomSheet states
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.combinedClickable(
        onClick = callback,
        onLongClick = { showBottomSheet = true}
    )) {
        Column {
            val name = if (user != null) {
                user.displayName ?: "${user.username}#${user.discriminator}"
            } else channel?.name ?: "Unknown"
            val avatar = if (user?.avatar != null) {
                "${ApiClient.S3_ROOT_URL}avatars/${user.avatar.id}?max_side=256"
            } else if (channel?.icon != null) {
                "${ApiClient.S3_ROOT_URL}icons/${channel.icon.id}"
            } else null

            val presence = if (user != null) {
                user.status?.presence
            } else null

            ListItem(
                leadingContent = {
                    ProfileImage(
                        name,
                        avatar,
                        presence
                    )
                },

                headlineContent = {
                    Text(
                        name
                    )
                },

                supportingContent = {
                    AnimatedVisibility(visible = status != null) {
                        status?.let {
                            it.text?.let { it1 ->
                                Text(
                                    it1,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    softWrap = false
                                )
                            }
                        }
                    }
                },

                trailingContent = {
                    AnimatedVisibility(visible = unreads != null) {
                        Badge(containerColor = MaterialTheme.colorScheme.tertiary) {
                            if (unreads != null) {
                                Text(
                                    if (unreads > 100) "99+" else "$unreads",
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        }
                    }
                }
            )
            HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            repeat(5) {
                ListItem(
                    headlineContent = { Text("Item") },
                    leadingContent = { Icon(Icons.Default.Square, "Not Localized yet") },
                    modifier = Modifier.clickable { /* TODO: Do something! */ })
            }
        }
    }
}