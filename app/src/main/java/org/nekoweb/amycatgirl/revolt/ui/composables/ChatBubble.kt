package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun ChatBubble(author: String? = null) {
    Column(
        modifier = Modifier
            .height(IntrinsicSize.Min)
    ) {

        if (author != null) {
            Text(author)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(5.dp)
        ) {
            Text("meowmeowmeowmeowmeowmeowmeowmeowmeowm")
        }
    }
}

@Preview
@Composable
fun ChatBubblePreview() {
    RevoltTheme {
        Column {
            ChatBubble("Amy")
            ChatBubble()
        }
    }
}