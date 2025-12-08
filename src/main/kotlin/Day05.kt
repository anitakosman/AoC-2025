import kotlin.let

fun main() {
    val input = getInputBlocks("day5")
    val ranges = input[0].lines().map { line -> line.split("-").let { it[0].toLong()..it[1].toLong() }}
    val ingredients = input[1].lines().map { it.toLong() }

    println(part1(ranges, ingredients))
    println(part2(ranges))
}

private fun part1(ranges: List<LongRange>, ingredients: List<Long>): Int {
    return ingredients.count { ingredient -> ranges.any { it.contains(ingredient) } }
}

private fun part2(ranges: List<LongRange>): Long {
    val sortedRanges = ranges.sortedBy { it.first }
    var freshIngredients = 0L
    var currentRange = sortedRanges[0]
    sortedRanges.drop(1).forEach { range ->
        if (range.first > currentRange.last) {
            freshIngredients += currentRange.last - currentRange.first + 1L
            currentRange = range
        } else {
            currentRange = currentRange.first..(maxOf(currentRange.last, range.last))
        }
    }
    freshIngredients += currentRange.last - currentRange.first + 1L
    return freshIngredients
}
