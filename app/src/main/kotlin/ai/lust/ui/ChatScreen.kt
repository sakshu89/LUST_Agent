package ai.lust.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject

private data class ChatMessage(val role: String, val text: String)

@Composable
fun ChatScreen() {
    val context = LocalContext.current
    val messages = remember {
        mutableStateListOf<ChatMessage>().apply {
            addAll(loadMessages(context))
        }
    }
    var input by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = .18f)
            ) {
                Text(
                    "L",
                    Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("LUST", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "Your Android assistant · Phase 1",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        LazyColumn(
            Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { message ->
                val isUser = message.role == "user"
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        color = if (isUser) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.widthIn(max = 320.dp)
                    ) {
                        Text(
                            message.text,
                            Modifier.padding(14.dp),
                            color = if (isUser) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message LUST…") },
                maxLines = 4,
                shape = RoundedCornerShape(22.dp)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = {
                    val text = input.trim()
                    if (text.isNotEmpty()) {
                        messages.add(ChatMessage("user", text))
                        messages.add(ChatMessage("assistant", mockReply(text)))
                        saveMessages(context, messages)
                        input = ""
                    }
                },
                enabled = input.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }

        Spacer(Modifier.height(4.dp))
        Text(
            "Development build · Mock provider · No cloud requests",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun mockReply(input: String): String = when {
    input.contains("hello", true) || input.contains("hi", true) ->
        "Hello! I'm LUST. The Phase 1 foundation is running."

    input.contains("time", true) ->
        "I can add a local date and time tool in the next implementation stage."

    else ->
        "I received: \"${input.take(180)}\". I'm currently using the deterministic Phase 1 mock provider; real Hermes/model integration comes next."
}

private fun loadMessages(context: Context): List<ChatMessage> = runCatching {
    val raw = context.getSharedPreferences("lust_chat", Context.MODE_PRIVATE)
        .getString("messages", "[]") ?: "[]"
    val json = JSONArray(raw)
    (0 until json.length()).map { i ->
        json.getJSONObject(i).let {
            ChatMessage(it.getString("role"), it.getString("text"))
        }
    }
}.getOrDefault(
    listOf(ChatMessage("assistant", "Hi! I'm LUST. What would you like to work on?"))
)

private fun saveMessages(context: Context, messages: List<ChatMessage>) {
    val json = JSONArray()
    messages.takeLast(100).forEach { message ->
        json.put(
            JSONObject()
                .put("role", message.role)
                .put("text", message.text)
        )
    }
    context.getSharedPreferences("lust_chat", Context.MODE_PRIVATE)
        .edit()
        .putString("messages", json.toString())
        .apply()
}
