
fun main() {
    val input = getInputText("day2").split(",")

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long {
    return sumInvalid(input, ::doublePattern)
}

private fun part2(input: List<String>): Long {
    return sumInvalid(input, ::anyPattern)
}

private fun sumInvalid(input: List<String>, function: (s: String) -> Boolean): Long {
    return input.sumOf { s ->
        val split = s.split("-")
        val (a, b) = (split[0].toLong() to split[1].toLong())
        (a..b).sumOf { n ->
            if (function(n.toString())) n else 0L
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
