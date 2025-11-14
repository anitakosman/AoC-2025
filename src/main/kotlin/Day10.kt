fun main() {
    val input = getInput("day10").readLines().flatMapIndexed { y, line ->
        line.mapIndexed() { x, c -> Pos(x, y) to c.toString().toInt() }
    }.toMap()
    var scores = input.filterValues { it == 9 }.mapValues { listOf(it) }
    (8 downTo 0).forEach { n ->
        scores = input.filterValues { it == n }.keys.associateWith { p ->
            Direction.orthogonals.flatMap {
                val q = it.move(p)
                if (input[q] == n + 1) {
                    scores.getOrDefault(q, emptyList())
                } else {
                    emptyList()
                }
            }
        }
    }
    println(input.filterValues { it == 0 }.keys.sumOf { scores.getOrDefault(it, emptyList()).distinct().size })
    println(input.filterValues { it == 0 }.keys.sumOf { scores.getOrDefault(it, emptyList()).size })
}