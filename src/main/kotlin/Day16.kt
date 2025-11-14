import Direction.*

fun main() {
    var start = Movable(Pos(0, 0), EAST)
    var end = Pos(0, 0)
    val walls = mutableSetOf<Pos>()
    val input = getInput("day16").readLines()
    input.withIndex().forEach { (y, line) ->
        line.withIndex().forEach { (x, c) ->
            when (c) {
                'S' -> start = Movable(Pos(x, y), EAST)
                '#' -> walls.add(Pos(x, y))
                'E' -> end = Pos(x, y)
            }
        }
    }

    var minimalScore = Int.MAX_VALUE
    var bestSeats = emptySet<Pos>()
    var paths = listOf(Path(start, 0, emptySet()))
    while (paths.isNotEmpty()) {
        paths = paths.flatMap { (reindeer, score, visited) ->
            if (reindeer.pos == end) {
                if (score == minimalScore){
                    bestSeats = bestSeats.plus(visited)
                } else if (score < minimalScore){
                    minimalScore = score
                    bestSeats = visited
                }
                emptyList()
            } else {
                val turnLeft = reindeer.direction.turnLeft()
                val left = turnLeft.move(reindeer.pos)
                val forward = reindeer.direction.move(reindeer.pos)
                val turnRight = reindeer.direction.turnRight()
                val right = turnRight.move(reindeer.pos)
                val newVisited = visited.plus(reindeer.pos)
                listOf(
                    Path(Movable(left, turnLeft), score + 1001, newVisited),
                    Path(Movable(forward, reindeer.direction), score + 1, newVisited),
                    Path(Movable(right, turnRight), score + 1001, newVisited)
                ).filter { it.reindeer.pos !in walls && it.score <= minimalScore }
            }
        }.groupBy { it.reindeer }.map { (_, paths) ->
            val minScore = paths.minOf { it.score }
            paths.filter { it.score == minScore }
                .reduce { acc, path -> Path(acc.reindeer, acc.score, acc.visited.plus(path.visited)) }
        }
    }
    println(minimalScore)
    println(bestSeats.size + 1) // +1 because of end tile
}

data class Path(val reindeer: Movable, val score: Int, val visited: Set<Pos>)