package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
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
        onValueChange = {
            if (it.isNotEmpty()) {
                onValueChange(it)
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
        placeholder = placeholder,
        modifier = modifier,
        isError = value.isEmpty(),
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