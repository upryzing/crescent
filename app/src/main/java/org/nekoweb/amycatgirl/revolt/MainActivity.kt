package org.nekoweb.amycatgirl.revolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import org.nekoweb.amycatgirl.revolt.ui.theme.RevoltTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RevoltTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier
                        .safeDrawingPadding()
                ) {
                    App()
                }
            }
        }
    }
}