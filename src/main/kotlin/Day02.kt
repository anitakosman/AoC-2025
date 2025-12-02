import java.math.BigInteger

fun main() {
    val input = getInputText("day2").split(",")

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): BigInteger {
    return sumInvalid(input, ::doublePattern)
}

private fun part2(input: List<String>): BigInteger {
    return sumInvalid(input, ::anyPattern)
}

private fun sumInvalid(input: List<String>, function: (s: String) -> Boolean): BigInteger {
    return input.sumOf { s ->
        val split = s.split("-")
        val (a, b) = (split[0].toBigInteger() to split[1].toBigInteger())
        (a..b).sumOf { n ->
            if (function(n.toString())) n else BigInteger.ZERO
        }
    }
}

private fun doublePattern(s: String): Boolean {
    val l = s.length
    return l % 2 == 0 && s.take(l / 2) == s.drop(l / 2)
}

private fun anyPattern(s: String): Boolean {
    val l = s.length
    return (1..l / 2).any { i -> l % i == 0 && s.windowed(i, i).distinct().size == 1 }
}
