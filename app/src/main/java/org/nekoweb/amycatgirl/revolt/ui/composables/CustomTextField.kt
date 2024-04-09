package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@Composable
fun CustomTextField(
    value: String,
    placeholder: @Composable () -> Unit,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.secondary,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            errorIndicatorColor = Color.Transparent,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
        ),
        placeholder = {
            AnimatedVisibility(visible = value.isEmpty()) {
                placeholder()
            }
        },
        modifier = modifier,
        singleLine = singleLine
    )
}

@Preview
@Composable
fun CustomTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    RevoltTheme {
        CustomTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Placeholder") },
            singleLine = true
        )
    }
}