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
    val circuits = mutableSetOf<MutableSet<ThreeDimensionalPoint>>()
    connections.take(1000).forEach {
        addConnection(circuits, it)
    }
    return circuits.map { it.size }.sorted().reversed().take(3).reduce(Int::times)
}

private fun part2(connections: List<Pair<ThreeDimensionalPoint, ThreeDimensionalPoint>>): Long {
    val circuits = mutableSetOf<MutableSet<ThreeDimensionalPoint>>()
    var currentConnection = connections[0]
    var remainingConnections = connections
    while (circuits.firstOrNull()?.size != 1000){
        currentConnection = remainingConnections[0]
        remainingConnections = remainingConnections.drop(1)
        addConnection(circuits, currentConnection)
    }
    return currentConnection.first.x.toLong() * currentConnection.second.x.toLong()
}

private fun addConnection(circuits: MutableSet<MutableSet<ThreeDimensionalPoint>>, connection: Pair<ThreeDimensionalPoint, ThreeDimensionalPoint>) {
    val (a, b) = connection
    val containingA = circuits.find { it.contains(a) }
    val containingB = circuits.find { it.contains(b) }
    if (containingA == null && containingB == null) {
        circuits.add(mutableSetOf(a, b))
    } else if (containingA == null) {
        containingB!!.add(a)
    } else if (containingB == null) {
        containingA.add(b)
    } else if (containingA != containingB) {
        containingA.addAll(containingB)
        containingB.retainAll(emptyList())
    }
    circuits.removeIf { it.isEmpty() }
}

data class ThreeDimensionalPoint(val x: Int, val y: Int, val z: Int) {
    fun distance(other: ThreeDimensionalPoint): Long {
        return sqrt((this.x - other.x).toLong().pow(2) + (this.y - other.y).toLong().pow(2) + (this.z - other.z).toLong().pow(2))
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }
}