package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun ProfileImage(fallback: String) {
    // TODO: Add image implementation
    Box(modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(2.dp),
        content = { Text(fallback.substring(0, 1), modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary, fontSize = 15.sp) })
}

@Preview
@Composable
fun ProfileImagePreview() {
    RevoltTheme {
        ProfileImage("Username")
    }
}
