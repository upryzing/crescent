package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.User
import org.nekoweb.amycatgirl.revolt.models.api.UserStatus
import org.nekoweb.amycatgirl.revolt.models.api.channels.Channel

@OptIn(ExperimentalMaterial3Api::class)
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
                "${ApiClient.S3_ROOT_URL}avatars/${user.avatar.id}"
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
                        Badge(containerColor = MaterialTheme.colorScheme.errorContainer) {
                            if (unreads != null) {
                                Text(
                                    if (unreads > 100) "100+" else "$unreads"
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