import java.math.BigInteger
import kotlin.io.println

fun main() {
    val input = getInputLines("day3").map { line -> line.map { it.toString().toInt() } }

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<List<Int>>): BigInteger {
    return input.sumOf { maxJoltage(it, 2) }
}

private fun part2(input: List<List<Int>>): BigInteger {
    return input.sumOf { maxJoltage(it, 12) }
}

private fun maxJoltage(batteries: List<Int>, numberOfBatteries: Int): BigInteger {
    val x = batteries.dropLast(numberOfBatteries - 1).max()
    if (numberOfBatteries == 1) {
        return x.toBigInteger()
    } else {
        val y = maxJoltage(batteries.drop(batteries.indexOf(x) + 1), numberOfBatteries - 1)
        return BigInteger.valueOf(10).pow(numberOfBatteries - 1) * x.toBigInteger() + y
    }
}
