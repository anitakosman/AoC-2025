fun main() {
    val input = getInput("day11").readText()
        .split(" ")
        .map { it.toLong() }
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
    var stones = input
    repeat(25) { stones = evolve(stones) }
    println(stones.values.sum())
    repeat(50) { stones = evolve(stones) }
    println(stones.values.sum())
}

fun evolve(stones: Map<Long, Long>): Map<Long, Long> {
    val newStones = mutableMapOf<Long, Long>()
    stones.forEach { (stone, count) ->
        if (stone == 0L) {
            newStones[1L] = newStones.getOrDefault(1L, 0) + count
        } else if (stone.toString().length % 2 == 0) {
            val s = stone.toString()
            val left = s.substring(0, s.length / 2).toLong()
            val right = s.substring(s.length / 2).toLong()
            newStones[left] = newStones.getOrDefault(left, 0) + count
            newStones[right] = newStones.getOrDefault(right, 0) + count
        } else {
            val newStone = stone * 2024
            newStones[newStone] = newStones.getOrDefault(newStone, 0) + count
        }
    }
    return newStones
}
