package app.upryzing.crescent.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.User
import app.upryzing.crescent.models.api.UserStatus
import app.upryzing.crescent.models.api.channels.Channel

@Composable
fun PeopleListItem(
    user: User? = null,
    channel: Channel.Group? = null,
    status: UserStatus? = null,
    unreads: Int? = null,
    callback: (() -> Unit)
) {
    Surface(onClick = { callback() }) {
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
}