package org.nekoweb.amycatgirl.revolt.ui.composables

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

interface Option {
    val icon: @Composable () -> Unit
    val onClick: () -> Unit
}

data class FloatingActionButtonListItem(
    override val icon: @Composable () -> Unit,
    override val onClick: () -> Unit
) : Option

@Composable
fun FloatingActionButtonWithOptions(
    options: List<Option>
) {


    var showAllOptions = remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (showAllOptions.value) {
            45f
        } else {
            0f
        },
        animationSpec = tween(300),
        label = "plusRotate"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            for (option in options) {
                AnimatedVisibility(showAllOptions.value) {
                    SmallFloatingActionButton(onClick = option.onClick) {
                        option.icon()
                    }
                }
            }
        }

        AnimatedVisibility(showAllOptions.value) {
            Spacer(modifier = Modifier.height(24.dp))
        }

        FloatingActionButton(onClick = {
            showAllOptions.apply { value = !value }
            Log.d("Debug", "clicked")
        }) {
            Icon(Icons.Outlined.Add, contentDescription = "Add", modifier = Modifier.rotate(rotation))
        }
    }
}

@Preview
@Composable
fun FABWOptionsPreview() {
    RevoltTheme {
        FloatingActionButtonWithOptions(options = listOf(
            FloatingActionButtonListItem(
                icon = {
                    Icon(Icons.Outlined.Add, contentDescription = "Add")
                },
                onClick = { Log.d("Debug", "hi") }
            ),
            FloatingActionButtonListItem(
                icon = {
                    Icon(Icons.Outlined.Add, contentDescription = "Add")
                },
                onClick = { Log.d("Debug", "hi") }
            ),
            FloatingActionButtonListItem(
                icon = {
                    Icon(Icons.Outlined.Add, contentDescription = "Add")
                },
                onClick = { Log.d("Debug", "hi") }
            )
        ))
    }
}
