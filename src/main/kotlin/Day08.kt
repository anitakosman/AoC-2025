fun main() {
    val input = getInputLines("day8").map {
        val split = it.split(",")
        ThreeDimensionalPoint(split[0].toInt(), split[1].toInt(), split[2].toInt())
    }
    val connections = input.flatMap { a -> input.map { b -> Pair(a, b) to a.distance(b) } }
        .filter { it.second != 0L }
        .distinctBy { setOf(it.first.first, it.first.second) }
        .sortedBy { it.second }
        .map { it.first }

    println(part1(connections))
    println(part2(connections))
}

private fun part1(connections: List<Pair<ThreeDimensionalPoint, ThreeDimensionalPoint>>): Int {
    var circuits = emptyList<Set<ThreeDimensionalPoint>>()
    connections.take(1000).forEach {
        circuits = addConnection(circuits, it)
    }
    return circuits.map { it.size }.sorted().reversed().take(3).reduce(Int::times)
}

private fun part2(connections: List<Pair<ThreeDimensionalPoint, ThreeDimensionalPoint>>): Long {
    var circuits = emptyList<Set<ThreeDimensionalPoint>>()
    var currentConnection = connections[0]
    var remainingConnections = connections
    while (circuits.firstOrNull()?.size != 1000){
        currentConnection = remainingConnections[0]
        remainingConnections = remainingConnections.drop(1)
        circuits = addConnection(circuits, currentConnection)
    }
    return currentConnection.first.x.toLong() * currentConnection.second.x.toLong()
}

private fun addConnection(circuits: List<Set<ThreeDimensionalPoint>>, connection: Pair<ThreeDimensionalPoint, ThreeDimensionalPoint>) =
    circuits
        .filter { !it.contains(connection.first) && !it.contains(connection.second) }
        .plusElement(
            circuits
                .filter { it.contains(connection.first) || it.contains(connection.second) }
                .flatten()
                .plus(setOf(connection.first, connection.second)).toSet()
        )

data class ThreeDimensionalPoint(val x: Int, val y: Int, val z: Int) {
    fun distance(other: ThreeDimensionalPoint): Long {
        return sqrt((this.x - other.x).toLong().pow(2) + (this.y - other.y).toLong().pow(2) + (this.z - other.z).toLong().pow(2))
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }
}