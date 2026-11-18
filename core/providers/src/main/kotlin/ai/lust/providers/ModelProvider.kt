package ai.lust.providers

import ai.lust.domain.AgentRequest

enum class ProviderCapability { TEXT, TEXT_STREAMING, VISION, EMBEDDINGS, TOOLS }

interface ModelProvider {
    val id: String
    val capabilities: Set<ProviderCapability>
    fun generate(request: AgentRequest): Result<String>
    fun stream(request: AgentRequest, callback: (String) -> Unit): Result<Unit>
    fun cancel(): Result<Unit> = Result.success(Unit)
    fun release(): Result<Unit> = Result.success(Unit)
}
