package org.nekoweb.amycatgirl.revolt.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.nekoweb.amycatgirl.revolt.ui.composables.PeopleListItem
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("RevoltMini") }, actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = "More options")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            for (i in 1..100) {
                PeopleListItem()
            }
        }
    }
}

@Preview
@Composable
fun HomePagePreview() {
    RevoltTheme {
        HomePage()
    }
}