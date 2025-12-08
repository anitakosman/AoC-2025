import kotlin.io.println
import kotlin.math.pow

fun main() {
    val input = getInputLines("day3").map { line -> line.map { it.toString().toInt() } }

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<List<Int>>): Long {
    return input.sumOf { maxJoltage(it, 2) }
}

private fun part2(input: List<List<Int>>): Long {
    return input.sumOf { maxJoltage(it, 12) }
}

private fun maxJoltage(batteries: List<Int>, numberOfBatteries: Int): Long {
    val x = batteries.dropLast(numberOfBatteries - 1).max()
    if (numberOfBatteries == 1) {
        return x.toLong()
    } else {
        val y = maxJoltage(batteries.drop(batteries.indexOf(x) + 1), numberOfBatteries - 1)
        return 10.0.pow(numberOfBatteries - 1.0).toLong() * x.toLong() + y
    }
}
