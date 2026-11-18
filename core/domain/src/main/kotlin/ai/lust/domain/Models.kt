package ai.lust.domain

data class AgentRequest(
    val prompt: String,
    val maxTokens: Int = 256,
    val temperature: Float = 0.7f
)
