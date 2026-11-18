package ai.lust.agent

/**
 * Lightweight orchestration contract for the two local partners.
 *
 * The supplied Phase-1 project does not contain Cactus/Needle native bindings
 * or Qwen/llama.cpp binaries, so this file intentionally contains contracts
 * rather than pretending those runtimes are already linked.
 */
enum class AgentLane {
    NEEDLE_ACTIONS,
    QWEN_LANGUAGE,
    CLOUD_FALLBACK
}

data class AgentDecision(
    val lane: AgentLane,
    val reason: String
)

class PartnerRouter {
    fun choose(input: String): AgentDecision {
        val text = input.trim().lowercase()
        val actionWords = listOf(
            "open ", "launch ", "tap ", "click ", "press ", "type ",
            "send ", "message ", "text ", "scroll ", "swipe ", "back",
            "home", "alarm", "call ", "turn on", "turn off", "set "
        )
        val knowledgeWords = listOf(
            "explain", "what is", "why ", "how does", "summarize",
            "write ", "rewrite ", "translate", "compare", "tell me"
        )

        return when {
            actionWords.any(text::contains) ->
                AgentDecision(AgentLane.NEEDLE_ACTIONS, "Device/tool action detected")
            knowledgeWords.any(text::contains) ->
                AgentDecision(AgentLane.QWEN_LANGUAGE, "Language/reasoning task detected")
            else ->
                AgentDecision(AgentLane.QWEN_LANGUAGE, "General request; language partner first")
        }
    }
}

/**
 * Common contract for a local model worker. A real Cactus/Needle or
 * llama.cpp implementation can implement this without changing the UI.
 */
interface LocalModelWorker {
    val name: String
    suspend fun run(prompt: String): String
    fun isBusy(): Boolean
}

/**
 * Cooperative scheduler: at most one local worker is actively decoding at
 * a time in the Phase-1 app, preventing two heavyweight decoders from
 * competing for the same phone RAM/CPU.
 */
class CooperativePartnerScheduler {
    private var active: LocalModelWorker? = null

    @Synchronized
    fun tryAcquire(worker: LocalModelWorker): Boolean {
        if (active != null && active !== worker && active!!.isBusy()) return false
        active = worker
        return true
    }

    @Synchronized
    fun release(worker: LocalModelWorker) {
        if (active === worker) active = null
    }
}
