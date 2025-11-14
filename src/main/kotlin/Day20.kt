fun main() {
    var start = Pos(0, 0)
    var end = Pos(0, 0)
    val walls = mutableSetOf<Pos>()
    val input = getInput("day20").readLines()
    input.withIndex().forEach { (y, line) ->
        line.withIndex().forEach { (x, c) ->
            when (c) {
                'S' -> start = Pos(x, y)
                '#' -> walls.add(Pos(x, y))
                'E' -> end = Pos(x, y)
            }
        }
    }
    val m = input[0].length
    val n = input.size

    val shortestPathToEnd = shortestPathsTo(end, walls)
    val shortestPathToStart = shortestPathsTo(start, walls)
    val reachablePositions = shortestPathToStart.keys
    val possibleCheats = getPossibleCheats(reachablePositions, walls, m, n)

    val startToFinish = shortestPathToEnd[start]!!
    println(reachablePositions.sumOf { cheatStart ->
        possibleCheats[cheatStart]?.count { (cheatEnd, cheatLength) ->
            cheatLength == 2 && if (shortestPathToEnd[cheatStart]!! < startToFinish) {
                shortestPathToEnd[cheatStart]!! - shortestPathToEnd[cheatEnd]!! - cheatLength >= 100
            } else {
                startToFinish - shortestPathToStart[cheatStart]!! - shortestPathToEnd[cheatEnd]!! - cheatLength >= 100
            }
        } ?: 0
    })
    println(reachablePositions.sumOf { cheatStart ->
        possibleCheats[cheatStart]?.count { (cheatEnd, cheatLength) ->
            if (shortestPathToEnd[cheatStart]!! < startToFinish) {
                shortestPathToEnd[cheatStart]!! - shortestPathToEnd[cheatEnd]!! - cheatLength >= 100
            } else {
                startToFinish - shortestPathToStart[cheatStart]!! - shortestPathToEnd[cheatEnd]!! - cheatLength >= 100
            }
        } ?: 0
    })
}

private fun shortestPathsTo(end: Pos, walls: Set<Pos>): Map<Pos, Int> {
    val shortestPathToEnd = mutableMapOf<Pos, Int>()
    val visited = mutableListOf<Pos>()
    var positions = listOf(end)
    var i = 0
    while (positions.isNotEmpty()) {
        positions.forEach { shortestPathToEnd.putIfAbsent(it, i) }
        i++
        visited.addAll(positions)
        positions = positions.flatMap { p -> Direction.orthogonals.map { it.move(p) } }.filter { it !in visited && it !in walls }
    }
    return shortestPathToEnd
}

private fun getPossibleCheats(reachablePositions: Set<Pos>, walls: Set<Pos>, m: Int, n: Int): Map<Pos, MutableMap<Pos, Int>> {
    val possibleCheats = mutableMapOf<Pos, MutableMap<Pos, Int>>()
    reachablePositions.forEach { cheatStart ->
        val visitedCheatPositions = mutableSetOf<Pos>()
        var currentCheatPositions = Direction.orthogonals.map { it.move(cheatStart) }.toSet()
        var cheatLength = 1
        while (currentCheatPositions.isNotEmpty() && cheatLength < 21) {
            visitedCheatPositions.addAll(currentCheatPositions)
            currentCheatPositions.forEach { if (it !in walls) possibleCheats.getOrPut(cheatStart, ::mutableMapOf).putIfAbsent(it, cheatLength) }
            currentCheatPositions = currentCheatPositions
                .flatMap { p -> Direction.orthogonals.map { it.move(p) } }
                .filter { it !in visitedCheatPositions && inGrid(it, m, n) }.toSet()
            cheatLength++
        }
    }
    return possibleCheats
}

fun inGrid(p: Pos, m: Int, n: Int) = p.x in 0 until m && p.y in 0 until n