fun main() {
    val input = getInput("day3").readText()
    println(executeMultiplyInstructions(input))
    val correctedInput = input.replace(Regex("don't\\(\\).*?(?:do\\(\\)|\$)", RegexOption.DOT_MATCHES_ALL), "")
    println(executeMultiplyInstructions(correctedInput))
}

private fun executeMultiplyInstructions(input: String) = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex().findAll(input)
    .sumOf { it.groups[1]!!.value.toInt() * it.groups[2]!!.value.toInt() }