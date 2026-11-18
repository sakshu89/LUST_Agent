package ai.lust

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.lust.domain.AgentRequest
import ai.lust.providers.ModelProvider
import ai.lust.providers.mock.MockProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val provider: ModelProvider = MockProvider()
        setContent { LustChat(provider) }
    }
}

@Composable
private fun LustChat(provider: ModelProvider) {
    val messages = remember { mutableStateListOf<Pair<String,String>>() }
    var input by remember { mutableStateOf("") }
    MaterialTheme {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("LUST", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                items(messages) { (role, text) ->
                    Text("$role: $text", Modifier.padding(vertical = 6.dp))
                }
            }
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(input, { input = it }, Modifier.weight(1f), placeholder = { Text("Message…") })
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    val text = input.trim()
                    if (text.isNotEmpty()) {
                        messages.add("You" to text)
                        input = ""
                        messages.add("LUST" to provider.generate(AgentRequest(text)).getOrThrow())
                    }
                }) { Text("Send") }
            }
        }
    }
}
