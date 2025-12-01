import kotlin.math.abs

fun main() {
    val input = getInputLines("day1")

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    var dial = 50
    var code = 0
    input.forEach { s ->
        dial = if (s.startsWith('L')) (dial - s.substring(1).toInt()).mod(100) else (dial + s.substring(1).toInt()).mod(100)
        if (dial == 0) {
            code++
        }
    }
    return code
}

private fun part2(input: List<String>): Int {
    var dial = 50
    var code = 0
    input.forEach { s ->
        val oldDial = dial
        dial = if (s.startsWith('L')) dial - s.substring(1).toInt() else dial + s.substring(1).toInt()
        if (dial < 0 && oldDial == 0) {
            dial += 100
        }
        if (dial !in 1..99) {
            code += if (dial <= 0) (abs(dial) / 100) + 1 else dial / 100
        }
        dial = dial.mod(100)
    }
    return code
}
