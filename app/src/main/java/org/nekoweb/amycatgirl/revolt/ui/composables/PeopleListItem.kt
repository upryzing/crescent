package org.nekoweb.amycatgirl.revolt.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleListItem() {
    ListItem(
        leadingContent = { ProfileImage("Username") },
        headlineContent = { Text("Username#1234") },
        supportingContent = { Text("Content") },
        trailingContent = { Badge(containerColor = MaterialTheme.colorScheme.errorContainer) { Text("100+") } }
    )
}

@Preview
@Composable
fun PeopleListItemPreview() {
    RevoltTheme {
        PeopleListItem()
    }
}