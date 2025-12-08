fun main() {
    val input = getInputLines("day6")

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long {
    val operations = input.last().trim().split(Regex("\\s+"))
    val values = input.dropLast(1).map { line -> line.trim().split(Regex("\\s+")).map { it.toLong() } }
    return operations.indices.sumOf {
        if (operations[it] == "+") {
            values.sumOf { value -> value[it] }
        } else {
            values.map { value -> value[it] }.reduce { a, b -> a * b }
        }
    }
}

private fun part2(input: List<String>): Long {
    val operations = input.last().split(Regex("\\s(?=[*+])"))
    var remainingValues = input.dropLast(1)
    return operations.indices.sumOf { i ->
        val currentOperation = operations[i]
        val currentColumns = remainingValues.map { it.take(currentOperation.length) }
        val currentValues = currentOperation.indices
            .map { j -> currentColumns.map { it[j] }.joinToString("").trim().toLong() }
        remainingValues = remainingValues.map { it.drop(currentOperation.length + 1) }
        if (currentOperation.startsWith("+")) {
            currentValues.sumOf {it}
        } else {
            currentValues.reduce { a, b -> a * b }
        }
    }
}
