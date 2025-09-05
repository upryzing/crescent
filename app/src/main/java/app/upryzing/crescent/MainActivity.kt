package app.upryzing.crescent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.ui.theme.RevoltTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.initialize(this)
        enableEdgeToEdge()

        setContent {
            RevoltTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                // Pass windowSizeClass to your App composable
                App(windowSizeClass = windowSizeClass)
            }
        }
    }
}