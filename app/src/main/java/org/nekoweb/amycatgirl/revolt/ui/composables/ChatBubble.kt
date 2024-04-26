package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.User
import org.nekoweb.amycatgirl.revolt.models.api.websocket.PartialMessage
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun ChatBubble(message: PartialMessage) {
    val author = remember(message.authorId) {
        ApiClient.cache[message.authorId] as User
    }
    // TODO: Needed a check whenever the authorID needed to be same as the authorID of the message.
    Column(
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {

        if (author != null) {
            Text(
                author.displayName ?: author.username,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(10.dp)
        ) {
            Text(
                text = if (!message.content.isNullOrBlank()) message.content else if (message.attachments != null) "Message has no content, but it has attachments, and we can't render them yet." else "Unhandled",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun ChatBubblePreview() {
    RevoltTheme {
        Column {
            ChatBubble(PartialMessage())
            ChatBubble(PartialMessage())
        }
    }
}