package ai.lust.providers.mock

import ai.lust.domain.AgentRequest
import ai.lust.providers.*

class MockProvider : ModelProvider {
    override val id = "mock"
    override val capabilities = setOf(ProviderCapability.TEXT, ProviderCapability.TEXT_STREAMING, ProviderCapability.TOOLS)

    override fun generate(request: AgentRequest): Result<String> {
        val words = request.prompt.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.take(6)
        return Result.success(if (words.isEmpty()) "Hello from LUST." else "Mock echo: ${words.joinToString(" ")}")
    }

    override fun stream(request: AgentRequest, callback: (String) -> Unit): Result<Unit> {
        request.prompt.split(Regex("\\s+")).filter { it.isNotBlank() }.forEach { callback(it) }
        return Result.success(Unit)
    }
}
