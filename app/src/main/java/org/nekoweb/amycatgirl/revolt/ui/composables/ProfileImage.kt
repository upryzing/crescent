package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import org.nekoweb.amycatgirl.revolt.models.api.Presence
import org.nekoweb.amycatgirl.revolt.ui.theme.Blue500
import org.nekoweb.amycatgirl.revolt.ui.theme.Gray400
import org.nekoweb.amycatgirl.revolt.ui.theme.Green500
import org.nekoweb.amycatgirl.revolt.ui.theme.Red500
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme
import org.nekoweb.amycatgirl.revolt.ui.theme.Yellow700

@Composable
fun ProfileImage(
    fallback: String,
    url: String? = null,
    presence: Presence? = null,
    size: Dp = 32.dp
) {
    // TODO: Add image implementation
    Box(modifier = Modifier.wrapContentSize()) {
        SubcomposeAsyncImage(model = url, contentDescription = "None... yet.") {
            val state = painter.state
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Error,
                is AsyncImagePainter.State.Empty -> {
                    Box(
                        modifier = Modifier
                            .size(size)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(
                            fallback.substring(0, 1),
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 15.sp
                        )
                    }
                }

                is AsyncImagePainter.State.Success -> {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier
                            .size(size)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
        AnimatedVisibility(
            presence != null,
            modifier = Modifier.align(AbsoluteAlignment.BottomRight)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        when (presence) {
                            Presence.ONLINE -> Green500
                            Presence.DO_NOT_DISTURB -> Red500
                            Presence.FOCUS -> Blue500
                            Presence.IDLE -> Yellow700
                            Presence.OFFLINE -> Gray400
                            else -> {
                                Gray400
                            }
                        }
                    )
                    .size(12.dp)
            )
        }
    }
}

@Preview
@Composable
fun ProfileImagePreview() {
    RevoltTheme {
        Column {
            ProfileImage("Username")
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.ONLINE
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.FOCUS
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.DO_NOT_DISTURB
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.IDLE
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.OFFLINE
            )
        }
    }
}
