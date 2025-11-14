fun main() {
    val inputs = getInput("day25").readText().split(lineSeparator + lineSeparator)
    val keys = mutableListOf<List<Int>>()
    val locks = mutableListOf<List<Int>>()
    inputs.forEach { input ->
        val grid = input.lines()
        if (grid[0][0] == '#') {
            locks.add(grid[0].indices.map { i -> grid.indexOfLast { it[i] == '#' } })
        } else {
            keys.add(grid[0].indices.map { i -> grid.indexOfLast { it[i] == '.' } })
        }
    }
    println(keys.sumOf { key -> locks.count { lock -> lock.indices.all { lock[it] <= key[it] } } })
}