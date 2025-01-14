package app.upryzing.crescent.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.User
import app.upryzing.crescent.models.api.websocket.PartialMessage
import app.upryzing.crescent.ui.theme.RevoltTheme
import com.vdurmont.emoji.EmojiParser

@Composable
fun ChatBubble(message: PartialMessage, modifier: Modifier = Modifier, isSelf: Boolean = false) {
    val author = remember(message.authorId) {
        ApiClient.cache[message.authorId] as User?
    }

    fun checkIfMessageOnlyContainsEmoji(): Boolean {
        if (message.content == null) return false
        return EmojiParser.removeAllEmojis(message.content).isEmpty()
    }

    val isOnlyEmoji = remember { checkIfMessageOnlyContainsEmoji() }

    Log.d("emoji", "Is emoji beeg: $isOnlyEmoji")

    Column(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .widthIn(0.dp, 300.dp),
        horizontalAlignment = if (isSelf) Alignment.End else Alignment.Start
    ) {
        if (author != null) {
            Text(
                if (isSelf) "You" else author.displayName ?: author.username,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Surface(
            color = if (isOnlyEmoji)
                Color.Transparent else if (isSelf)
                MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(18.dp)
        ) {
            SelectionContainer {
                Text(
                    text = if (!message.content.isNullOrBlank()) message.content else if (message.attachments != null) "Message has no content, but it has attachments, and we can't render them yet." else "Unhandled",
                    color = if (isSelf) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                    fontSize = if (isOnlyEmoji) 48.sp else 16.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ChatBubblePreview() {
    RevoltTheme {
        Column {
            ChatBubble(PartialMessage(), isSelf = true)
            ChatBubble(PartialMessage())
            ChatBubble(PartialMessage(content = "🥺"))
        }
    }
}