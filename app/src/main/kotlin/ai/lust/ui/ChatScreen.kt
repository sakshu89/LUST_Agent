package ai.lust.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import ai.lust.agent.AgentLane
import ai.lust.agent.PartnerRouter
import org.json.JSONArray
import org.json.JSONObject

private data class ChatMessage(val role: String, val text: String)

@Composable
fun ChatScreen() {
    val context = LocalContext.current
    val messages = remember {
        mutableStateListOf<ChatMessage>().apply { addAll(loadMessages(context)) }
    }
    var input by remember { mutableStateOf("") }
    var freeHand by remember { mutableStateOf(false) }
    var listening by remember { mutableStateOf(false) }

    val router = remember { PartnerRouter() }

    fun sendText() {
        val text = input.trim()
        if (text.isEmpty()) return
        val decision = router.choose(text)
        messages.add(ChatMessage("user", text))
        messages.add(
            ChatMessage(
                "assistant",
                when (decision.lane) {
                    AgentLane.NEEDLE_ACTIONS ->
                        "Queued for the action partner: ${decision.reason}."
                    AgentLane.QWEN_LANGUAGE ->
                        "Queued for the language partner: ${decision.reason}."
                    AgentLane.CLOUD_FALLBACK ->
                        "Queued for cloud fallback."
                }
            )
        )
        saveMessages(context, messages)
        input = ""
    }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spoken = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
            .orEmpty()
        if (spoken.isNotBlank()) input = spoken
        listening = false
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            listening = true
            speechLauncher.launch(
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to LUST")
                }
            )
        }
    }

    fun startVoice() {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            listening = true
            speechLauncher.launch(
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                }
            )
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = .18f)
            ) {
                Text(
                    "L",
                    Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    color = primary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("LUST", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    if (freeHand) "Free Hand Agent · ready" else "Your Android assistant",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { freeHand = !freeHand }) {
                Icon(
                    if (freeHand) Icons.Default.Stop else Icons.Default.SmartToy,
                    contentDescription = if (freeHand) "Stop free hand" else "Start free hand"
                )
            }
        }

        if (freeHand) {
            FreeHandPanel(listening = listening)
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(messages) { message ->
                val isUser = message.role == "user"
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        color = if (isUser)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.widthIn(max = 340.dp)
                    ) {
                        Text(
                            message.text,
                            Modifier.padding(12.dp),
                            color = if (isUser)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message LUST…") },
                maxLines = 4,
                shape = RoundedCornerShape(20.dp)
            )
            Spacer(Modifier.width(4.dp))
            FilledTonalIconButton(onClick = { startVoice() }) {
                Icon(
                    if (listening) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = if (listening) "Stop voice" else "Voice message"
                )
            }
            Spacer(Modifier.width(4.dp))
            FilledIconButton(
                onClick = { sendText() },
                enabled = input.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }

        Spacer(Modifier.height(3.dp))
        Text(
            if (freeHand)
                "Free Hand active · voice control ready"
            else
                "Local-first UI · partner routing ready",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.alpha(.85f)
        )
    }
}

@Composable
private fun FreeHandPanel(listening: Boolean) {
    val transition = rememberInfiniteTransition(label = "free-hand")
    val pulse by transition.animateFloat(
        initialValue = .82f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            tween(900),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary

    Surface(
        modifier = Modifier.fillMaxWidth().height(118.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .45f)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Canvas(
                Modifier
                    .size(88.dp)
                    .alpha(if (listening) 1f else .7f)
            ) {
                val radius = size.minDimension / 2f
                drawCircle(
                    color = primary,
                    radius = radius * pulse.coerceIn(.85f, 1.1f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
                drawCircle(
                    color = secondary,
                    radius = radius * .45f
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(90.dp))
                Text(
                    if (listening) "Listening…" else "Free Hand",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
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
