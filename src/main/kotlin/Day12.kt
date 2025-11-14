fun main() {
    val input = getInput("day12").readLines()
    val m = input[0].length
    val n = input.size
    val plots = mutableSetOf<Set<Pos>>()
    val todo = (0 until n).flatMap { y -> (0 until m).map { x -> Pos(x,y) } }.toMutableSet()
    while (todo.size > 0) {
        val plot = getPlot(todo.first(), input, m, n)
        plots.add(plot)
        todo.removeAll(plot)
    }
    println(plots.sumOf { plot -> plot.size * perimeter(plot) })
    println(plots.sumOf { plot -> plot.size * sides(plot) })
}

private fun perimeter(plot: Set<Pos>) = plot.sumOf { p -> Direction.orthogonals.count { it.move(p) !in plot } }
private fun sides(plot: Set<Pos>) = plot.sumOf { p -> Direction.orthogonals.count {
    if (it.move(p) in plot) {
        return@count false
    }
    val left = it.turnLeft().move(p)
    if (left !in plot) {
        return@count true
    }
    it.move(left) in plot
} }

fun getPlot(start:Pos, input: List<String>, m: Int, n: Int): Set<Pos> {
    val c = input[start.y][start.x]
    val plot = mutableSetOf(start)
    var newPlot = setOf(start)
    while (newPlot.isNotEmpty()) {
        newPlot = plot.flatMap { p ->
            Direction.orthogonals.mapNotNull {
                val q = it.move(p)
                if (q !in plot && q.x in 0 until m && q.y in 0 until n && input[q.y][q.x] == c) {
                    q
                } else {
                    null
                }
            }
        }.toSet()
        plot.addAll(newPlot)
    }
    return plot
}

