import kotlin.math.abs

fun main() {
    val input = getInputLines("day9").map { line -> line.split(",").let { Pos(it[0].toInt(), it[1].toInt()) } }

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<Pos>): Long {
    return input.maxOf { a -> input.maxOf { b -> area(a, b) } }
}

private fun part2(input: List<Pos>): Long {
    val areas = input.flatMap { a -> input.map { b -> Triple(area(a, b), a, b) } }.sortedBy { it.first }.reversed()
    val xs = input.map { it.x }.toSet()
    val previousXs = xs.sorted().windowed(2).associate { it[1] to it[0] }
    val nextXs = xs.sorted().windowed(2).associate { it[0] to it[1] }
    val ys = input.map { it.y }.toSet()
    val previousYs = ys.sorted().windowed(2).associate { it[1] to it[0] }
    val nextYs = ys.sorted().windowed(2).associate { it[0] to it[1] }
    val walls = getWalls(input)
    val t = System.currentTimeMillis()
    val coloredTiles = getInside(walls, previousXs, nextXs, previousYs, nextYs) + walls
    println("Inside calculated in ${(System.currentTimeMillis() - t)/1000}s")
    val biggestContainedArea = areas.first { acceptableRectangle(it.second, it.third, coloredTiles, xs, ys) }
    printGrid(input, ys, xs, biggestContainedArea, walls, coloredTiles)
    return biggestContainedArea.first
}

fun area(a: Pos, b: Pos): Long = abs(a.x - b.x + 1).toLong() * abs(a.y - b.y + 1).toLong()

private fun getWalls(input: List<Pos>): List<Pair<Int, Int>> = input.plus(input.first()).windowed(2).flatMap { corners ->
    val a = corners[0]
    val b = corners[1]
    if (a.x < b.x) {
        (a.x..b.x).map { Pos(it, a.y) }
    } else if (b.x < a.x) {
        (b.x..a.x).map { Pos(it, a.y) }
    } else if (a.y < b.y) {
        (a.y..b.y).map { Pos(a.x, it) }
    } else {
        (b.y..a.y).map { Pos(a.x, it) }
    }
}

fun getInside(walls: List<Pos>, previousXs: Map<Int, Int>, nextXs: Map<Int, Int>, previousYs: Map<Int, Int>, nextYs: Map<Int, Int>): Set<Pos> {
    val minimum = walls.minBy { it.x + it.y }
    val start = Pos(nextXs[minimum.x]!!, nextYs[minimum.y]!!)
    val inside = mutableSetOf<Pos>()
    var possibleMoves = setOf(start)
    while (possibleMoves.isNotEmpty()) {
        inside.addAll(possibleMoves)
        possibleMoves = possibleMoves.flatMap { p ->
            listOf(
                Pos(previousXs[p.x]!!, p.y),
                Pos(nextXs[p.x]!!, p.y),
                Pos(p.x, previousYs[p.y]!!),
                Pos(p.x, nextYs[p.y]!!)
            ).filter { it !in walls && it !in inside }
        }.toSet()
    }
    return inside
}

fun acceptableRectangle(a: Pos, b: Pos, coloredTiles: Set<Pos>, xs: Set<Int>, ys: Set<Int>): Boolean {
    return xs.filter { minOf(a.x, b.x) <= it && it <= maxOf(a.x, b.x) }.all { x ->
        coloredTiles.contains(Pos(x, a.y)) && coloredTiles.contains(Pos(x, b.y))
    } && ys.filter { minOf(a.y, b.y) <= it && it <= maxOf(a.y, b.y) }.all { y ->
        coloredTiles.contains(Pos(a.x, y)) && coloredTiles.contains(Pos(b.x, y))
    }
}

private fun printGrid(
    input: List<Pos>,
    ys: Set<Int>,
    xs: Set<Int>,
    biggestContainedArea: Triple<Long, Pos, Pos>,
    walls: List<Pair<Int, Int>>,
    coloredTiles: Set<Pos>
) {
    (input.minOf { it.y }..input.maxOf { it.y }).forEach { y ->
        if (ys.contains(y)) {
            (input.minOf { it.x }..input.maxOf { it.x }).forEach { x ->
                if (xs.contains(x)) {
                    val pos = Pos(x, y)
                    print(
                        when (pos) {
                            biggestContainedArea.second, biggestContainedArea.third -> 'O'
                            in input -> '#'
                            in walls -> 'X'
                            in coloredTiles -> '.'
                            else -> ' '
                        }
                    )
                }
            }
            println()
        }
    }
}
