fun main() {
    val input = getInputLines("day7")

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    var beams = setOf(input[0].indexOf('S'))
    var splits = 0
    input.drop(1).forEach { line ->
        val splitters = line.withIndex().filter { it.value == '^' }.map { it.index }
        splits += beams.count { it in splitters }
        beams = beams.flatMap { if (it in splitters) setOf(it - 1, it + 1) else setOf(it) }.toSet()
    }
    return splits
}

private fun part2(input: List<String>): Long {
    var beams = mapOf(input[0].indexOf('S') to 1L)
    input.drop(1).forEach { line ->
        val splitters = line.withIndex().filter { it.value == '^' }.map { it.index }
        beams = beams.map { (beam, count) ->
            if (beam in splitters)
                listOf(beam - 1 to count, beam + 1 to count)
            else
                listOf(beam to count)
        }.flatten().groupBy { it.first }.map { (beam, counts) -> beam to counts.sumOf { it.second } }.toMap()

    }
    return beams.values.sum()
}
