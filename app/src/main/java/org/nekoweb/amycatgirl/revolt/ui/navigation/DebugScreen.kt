package org.nekoweb.amycatgirl.revolt.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.nekoweb.amycatgirl.revolt.models.websocket.AuthenticatedEvent
import org.nekoweb.amycatgirl.revolt.models.websocket.BaseEvent
import org.nekoweb.amycatgirl.revolt.models.websocket.PartialMessageEvent
import org.nekoweb.amycatgirl.revolt.models.websocket.ReadyEvent
import org.nekoweb.amycatgirl.revolt.models.websocket.UnimplementedEvent
import org.nekoweb.amycatgirl.revolt.ui.theme.Green500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen(
    items: List<BaseEvent>,
    goBack: () -> Unit
) {
    val list = remember {
        items
    }
    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Debug") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(list) { it ->
                when (it) {
                    is AuthenticatedEvent -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(), color = Green500,
                        ) {
                            Text("Authenticated")
                        }
                    }

                    is ReadyEvent -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                "Websocket is ready!",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    is PartialMessageEvent -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        ) {
                            Text(
                                "Got message from ${it.authorId}: ${it.content}",
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    is UnimplementedEvent -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.errorContainer,
                        ) {
                            Text(
                                "Event is not implemented...",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    else -> {
                        Text("TODO...")
                    }
                }
            }
        }
    }
    // events
}