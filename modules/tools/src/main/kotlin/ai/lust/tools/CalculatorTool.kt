package ai.lust.tools

object CalculatorTool {
    fun calculate(expression: String): Result<Double> = runCatching {
        val cleaned = expression.replace(" ", "")
        require(cleaned.matches(Regex("[0-9+*/().-]+"))) { "Unsupported characters" }
        // Intentionally conservative Phase-1 tool: only simple binary operations.
        val m = Regex("^(-?\\d+(?:\\.\\d+)?)([+*/-])(-?\\d+(?:\\.\\d+)?)$").matchEntire(cleaned)
            ?: error("Only simple a op b expressions are supported")
        val a = m.groupValues[1].toDouble()
        val b = m.groupValues[3].toDouble()
        when (m.groupValues[2]) { "+" -> a+b; "-" -> a-b; "*" -> a*b; "/" -> { require(b != 0.0) { "Division by zero" }; a/b }; else -> error("operator") }
    }
}
