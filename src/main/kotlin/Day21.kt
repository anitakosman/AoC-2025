fun main() {
    val input = getInput("day21").readLines()
    println(input.sumOf { code -> code.take(3).toInt() * numOptions(code).minOf { numberOfPresses(it, 2) } })
    println(input.sumOf { code -> code.take(3).toInt() * numOptions(code).minOf { numberOfPresses(it, 25) } })
}

val costs = mutableMapOf<Int, MutableMap<String, Long>>()
val sequences = mutableMapOf<String, List<String>>()

// @formatter:off
val directionalPad = mapOf(
    '^' to Pos(1, 0), 'A' to Pos(2, 0),
    '<' to Pos(0, 1), 'v' to Pos(1, 1), '>' to Pos(2, 1)
)
val numericalPad = mapOf(
    '7' to Pos(0, 0), '8' to Pos(1, 0), '9' to Pos(2, 0),
    '4' to Pos(0, 1), '5' to Pos(1, 1), '6' to Pos(2, 1),
    '1' to Pos(0, 2), '2' to Pos(1, 2), '3' to Pos(2, 2),
                      '0' to Pos(1, 3), 'A' to Pos(2, 3)
)
// @formatter:on

fun options(sequence: String): List<String> {
    return "A$sequence".zipWithNext().map {
        val pos1 = directionalPad[it.first]!!
        val pos2 = directionalPad[it.second]!!
        when {
            it == 'A' to '<' -> listOf("v<<")
            it == '^' to '<' -> listOf("v<")
            it == '<' to 'A' -> listOf(">>^")
            it == '<' to '^' -> listOf(">^")
            it.first == it.second -> listOf("")
            pos1.x == pos2.x && pos1.y < pos2.y -> listOf("v")
            pos1.x == pos2.x && pos1.y > pos2.y -> listOf("^")
            pos1.y == pos2.y && pos1.x < pos2.x -> listOf(">")
            pos1.y == pos2.y && pos1.x > pos2.x -> listOf("<")
            else -> listOf(
                (if (pos1.x < pos2.x) ">" else "<") + (if (pos1.y < pos2.y) "v" else "^"),
                (if (pos1.y < pos2.y) "v" else "^") + (if (pos1.x < pos2.x) ">" else "<")
            )
        }
    }.fold(listOf("")) { acc, strings -> strings.flatMap { s -> acc.map { it + s + "A" } } }
}

fun numOptions(code: String): List<String> {
    return "A$code".zipWithNext().map {
        val pos1 = numericalPad[it.first]!!
        val pos2 = numericalPad[it.second]!!
        when {
            it.first == it.second -> listOf("")
            pos1.x == pos2.x && pos1.y < pos2.y -> listOf("v".repeat(pos2.y - pos1.y))
            pos1.x == pos2.x && pos1.y > pos2.y -> listOf("^".repeat(pos1.y - pos2.y))
            pos1.y == pos2.y && pos1.x < pos2.x -> listOf(">".repeat(pos2.x - pos1.x))
            pos1.y == pos2.y && pos1.x > pos2.x -> listOf("<".repeat(pos1.x - pos2.x))
            pos1.x == 0 && pos2.y == 3 -> listOf(">".repeat(pos2.x - pos1.x) + "v".repeat(pos2.y - pos1.y))
            pos2.x == 0 && pos1.y == 3 -> listOf("^".repeat(pos1.y - pos2.y) + "<".repeat(pos1.x - pos2.x))
            else -> listOf(
                (if (pos1.x < pos2.x) ">".repeat(pos2.x - pos1.x) else "<".repeat(pos1.x - pos2.x))
                        + (if (pos1.y < pos2.y) "v".repeat(pos2.y - pos1.y) else "^".repeat(pos1.y - pos2.y)),
                (if (pos1.y < pos2.y) "v".repeat(pos2.y - pos1.y) else "^".repeat(pos1.y - pos2.y))
                        + (if (pos1.x < pos2.x) ">".repeat(pos2.x - pos1.x) else "<".repeat(pos1.x - pos2.x))
            )
        }
    }.fold(listOf("")) { acc, strings -> strings.flatMap { s -> acc.map { it + s + "A" } } }
}

fun numberOfPresses(sequence: String, depth: Int): Long {
    return if (depth == 0) {
        sequence.length.toLong()
    } else {
        sequence.split('A').dropLast(1).sumOf { splitPart ->
            val subSequence = splitPart + "A"
            sequences.getOrPut(subSequence) { options(subSequence) }
                .minOf { option ->
                    costs.getOrPut(depth, ::mutableMapOf)
                        .getOrPut(option) { numberOfPresses(option, depth - 1) }
                }
        }
    }
}
