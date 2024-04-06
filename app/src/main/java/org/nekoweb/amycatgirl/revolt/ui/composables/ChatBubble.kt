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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun ChatBubble(author: String? = null, message: String? = null) {
    // TODO: Needed a check whenever the authorID needed to be same as the authorID of the message.
    Column(
        modifier = Modifier
            .height(IntrinsicSize.Min)
    ) {

        if (author != null) {
            Text(author, color = MaterialTheme.colorScheme.onBackground)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(
                    // Isn't supposed to allow different cornering shape?
                    RoundedCornerShape(12.dp)
                )
                .background(MaterialTheme.colorScheme.secondary)
                .padding(10.dp)
        ) {
            if (message != null) {
                Text(
                    message,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 16.sp
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
            ChatBubble("Amy", "Meow :3")
            ChatBubble("Amy", "Meow :3")
        }
    }
}