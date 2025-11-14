fun main() {
    val input = getInput("day19").readText().split(lineSeparator + lineSeparator)
    val towels = input[0].split(", ").sortedByDescending { it.length }
    val patterns = input[1].lines()

    val patternPossibilities = mutableMapOf("" to 1L)
    patterns.forEach { computePossibilities(it, towels, patternPossibilities) }

    println(patterns.count { patternPossibilities[it]!! > 0 })
    println(patterns.sumOf { patternPossibilities[it]!! })
}

fun computePossibilities(pattern: String, towels: List<String>, patternPossibilities: MutableMap<String, Long>): Long {
    if (patternPossibilities.containsKey(pattern))
        return patternPossibilities[pattern]!!

    val possibilities = towels
        .filter { pattern.startsWith(it) }
        .sumOf { computePossibilities(pattern.substring(it.length), towels, patternPossibilities) }
    patternPossibilities[pattern] = possibilities
    return possibilities
}
