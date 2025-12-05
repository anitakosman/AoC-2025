import java.math.BigInteger
import kotlin.let

fun main() {
    val input = getInputBlocks("day5")
    val ranges = input[0].lines().map { line -> line.split("-").let { it[0].toBigInteger()..it[1].toBigInteger() }}
    val ingredients = input[1].lines().map { it.toBigInteger() }

    println(part1(ranges, ingredients))
    println(part2(ranges))
}

private fun part1(ranges: List<BigIntegerRange>, ingredients: List<BigInteger>): Int {
    return ingredients.count { ingredient -> ranges.any { it.contains(ingredient) } }
}

private fun part2(ranges: List<BigIntegerRange>): BigInteger {
    val sortedRanges = ranges.sortedBy { it.start }
    var freshIngredients = BigInteger.ZERO
    var currentRange = sortedRanges[0]
    sortedRanges.drop(1).forEach { range ->
        if (range.start > currentRange.endInclusive) {
            freshIngredients += currentRange.endInclusive - currentRange.start + BigInteger.ONE
            currentRange = range
        } else {
            currentRange = currentRange.start..(maxOf(currentRange.endInclusive, range.endInclusive))
        }
    }
    freshIngredients += currentRange.endInclusive - currentRange.start + BigInteger.ONE
    return freshIngredients
}
