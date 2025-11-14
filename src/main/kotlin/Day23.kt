fun main() {
    val network = getInput("day23").readLines()
        .map { it.split("-") }
    val computers = network.flatten().toSet()
    println(computers.mapNotNull { c ->
        if (c.startsWith('t')) {
            val connectedComputers = connectedComputers(c, network)
            distinctPairs(connectedComputers).filter { pair -> network.any { pair == it.toSet() } }.map { it.plus(c) }
        } else {
            null
        }
    }.flatten().distinct().size)
    println(computers
        .flatMap { c -> subsets(connectedComputers(c, network)).map { it.plus(c) } }
        .groupingBy { it }
        .eachCount()
        .filter { it.key.size == it.value }
        .maxBy { it.value }
        .key
        .sorted()
        .joinToString(",", "", "")
    )
}

fun connectedComputers(computer: String, network: List<List<String>>): List<String> {
    return network.mapNotNull { connection ->
        when (computer) {
            connection[0] -> connection[1]
            connection[1] -> connection[0]
            else -> null
        }
    }
}

fun distinctPairs(s: List<String>) = s.flatMap { a -> s.map { b -> setOf(a, b) } }.toSet().filter { it.size == 2 }

fun subsets(s: List<String>, res: MutableSet<Set<String>> = mutableSetOf(), start: Int = 0, temp: Set<String> = mutableSetOf()): Set<Set<String>> {
    res.add(temp)
    for (i in start until s.size) {
        subsets(s, res, i + 1, temp.plus(s[i]))
    }
    return res
}
