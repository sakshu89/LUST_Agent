package ai.lust

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import ai.lust.ui.ChatScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF9B7BFF),
                    secondary = Color(0xFF72A7FF),
                    background = Color(0xFF0B0D14),
                    surface = Color(0xFF151925)
                )
            ) { ChatScreen() }
        }
    }
}
