fun main() {
    val input = getInputLines("day11").associate { line ->
        line.split(": ").let { it[0] to it[1].split(" ") }
    }
    val pathsToOut = mutableMapOf("out" to PathCount(1L, 0L, 0L, 0L))

    println(part1(input, pathsToOut))
    println(part2(input, pathsToOut))
}

private fun part1(input: Map<String, List<String>>, pathsToOut: MutableMap<String, PathCount>): Long {
    return numberOfPathsToOut("you", pathsToOut, input).allPaths
}

private fun part2(input: Map<String, List<String>>, pathsToOut: MutableMap<String, PathCount>): Long {
    return numberOfPathsToOut("svr", pathsToOut, input).pathsThroughDacAndFft
}

private fun numberOfPathsToOut(start: String, pathsToOut: MutableMap<String, PathCount>, flows: Map<String, List<String>>): PathCount {
    if (pathsToOut.containsKey(start)) {
        return pathsToOut[start]!!
    } else {
        var pathCount = flows[start]!!.map { numberOfPathsToOut(it, pathsToOut, flows) }.reduce(PathCount::plus)
        if (start == "dac") {
            pathCount = PathCount(pathCount.allPaths, pathCount.allPaths, pathCount.pathsThroughFft, pathCount.pathsThroughFft)
        } else if (start == "fft") {
            pathCount = PathCount(pathCount.allPaths, pathCount.pathsThroughDac, pathCount.allPaths, pathCount.pathsThroughDac)
        }
        pathsToOut[start] = pathCount
        return pathCount
    }
}

data class PathCount(val allPaths: Long, val pathsThroughDac: Long, val pathsThroughFft: Long, val pathsThroughDacAndFft: Long) {
    operator fun plus(other: PathCount) = PathCount(
        this.allPaths + other.allPaths,
        this.pathsThroughDac + other.pathsThroughDac,
        this.pathsThroughFft + other.pathsThroughFft,
        this.pathsThroughDacAndFft + other.pathsThroughDacAndFft
    )
}
