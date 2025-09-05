package app.upryzing.crescent.ui.composables

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import app.upryzing.crescent.ui.theme.RevoltTheme

interface Items {
    val icon: @Composable () -> Unit
    val text: String
    val onClick: () -> Unit
}

data class FloatingActionButtonListItem(
    override val icon: @Composable () -> Unit,
    override val text: String,
    override val onClick: () -> Unit
) : Items

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingActionButtonWithOptions(
    options: List<Items>
) {
    val listState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }

    FloatingActionButtonMenu(
        expanded = fabMenuExpanded,
        button = {
            ToggleFloatingActionButton(
                modifier = Modifier
                    .semantics {
                        traversalIndex = -1f
                        stateDescription = if (fabMenuExpanded) "Expanded" else "Collapsed"
                        contentDescription = "Toggle Menu"
                    }
                    .animateFloatingActionButton(
                        visible = fabVisible || fabMenuExpanded,
                        alignment = Alignment.BottomEnd
                    ),
                checked = fabMenuExpanded,
                onCheckedChange = { fabMenuExpanded = !fabMenuExpanded }
            ) {
                val rotation by animateFloatAsState(
                    targetValue = if (fabMenuExpanded) {
                        45f
                    } else {
                        0f
                    },
                    animationSpec = motionScheme.defaultSpatialSpec(),
                    label = "plusRotate"
                )

                Icon(
                    Icons.Outlined.Add,
                    contentDescription = "Add",
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
    ) {
        options.forEachIndexed { i, item ->
            FloatingActionButtonMenuItem(
                modifier =
                    Modifier.semantics {
                        isTraversalGroup = true
                        if (i == options.size - 1) {
                            customActions =
                                listOf(
                                    CustomAccessibilityAction(
                                        label = "Close menu",
                                        action = {
                                            fabMenuExpanded = false
                                            true
                                        }
                                    )
                                )
                        }
                    },
                onClick = {
                    fabMenuExpanded = false
                    item.onClick()
                },
                icon = { item.icon() },
                text = { Text(item.text) },
            )
        }
    }
}

@Preview
@Composable
fun FABWOptionsPreview() {
    RevoltTheme {
        FloatingActionButtonWithOptions(
            options = listOf(
                FloatingActionButtonListItem(
                    icon = {
                        Icon(Icons.Outlined.Add, contentDescription = "Add")
                    },
                    text = "Add",
                    onClick = { Log.d("Debug", "hi") }
                ),
                FloatingActionButtonListItem(
                    icon = {
                        Icon(Icons.Outlined.Add, contentDescription = "Add")
                    },
                    text = "Add",
                    onClick = { Log.d("Debug", "hi") }
                ),
            ))
    }
}
