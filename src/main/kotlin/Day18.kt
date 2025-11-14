fun main() {
    val corruptedBytes = getInput("day18").readLines()
        .map { Regex("(\\d+),(\\d+)").matchEntire(it)!!.groups }
        .map { Pos(it[1]!!.value.toInt(), it[2]!!.value.toInt()) }

    println(shortestPath(corruptedBytes.take(1024)))

    var l = 1025
    var r = corruptedBytes.size -1
    while (l < r - 1) {
        val x = (l + r) / 2
        if (shortestPath(corruptedBytes.take(x)) != -1) {
            l = x
        } else {
            r = x
        }
    }
    val blocker = corruptedBytes[l]
    println("${blocker.x},${blocker.y}")
}

fun shortestPath(corruptedBytes: List<Pos>): Int {
    var currentSteps = 0
    var currentReachablePositions = setOf(Pos(0, 0))

    while (true) {
        val newReachablePositions = currentReachablePositions
            .flatMap { p -> Direction.orthogonals.map { it.move(p) } }
            .filter { it.x in 0..n && it.y in 0..n && it !in corruptedBytes }
            .toSet()
            .plus(currentReachablePositions)
        currentSteps++

        if (Pos(n, n) in newReachablePositions) {
            return currentSteps
        }
        if (currentReachablePositions.containsAll(newReachablePositions)) {
            return -1
        }
        currentReachablePositions = newReachablePositions
    }
}

val n = 70