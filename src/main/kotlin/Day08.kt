fun main() {
    val antennas = mutableMapOf<Char, MutableSet<Pos>>()
    val input = getInput("day8").readLines()
    input.withIndex().forEach { (y, line) ->
        line.withIndex().forEach { (x, c) ->
            if (c != '.')
            antennas.getOrPut(c, ::mutableSetOf).add(x to y)
        }
    }
    val m = input[0].length
    val n = input.size

    println(getAntinodes(antennas, m, n).size)

    val lines = getLines(antennas)
    println((0 until m).sumOf { x ->
        (0 until n).count { y ->
            lines.any { (a, b) -> a * x.toBigInteger() + b == Rational(y.toBigInteger()) }
        }
    })
}

fun getAntinodes(antennas: Map<Char, Set<Pos>>, m: Int, n: Int): Set<Pos> {
    return antennas.keys.flatMap { c ->
        antennas[c]!!.pairs().flatMap { (a, b) ->
            val (ax, ay) = a
            val (bx, by) = b
            val antinodes = mutableSetOf<Pos>()
            antinodes.add((2 * ax - bx) to (2 * ay - by))
            antinodes.add((2 * bx - ax) to (2 * by - ay))
            if ((ax - bx).mod(3) == 0 && (ay - by).mod(3) == 0){
                antinodes.add((bx - (bx - ax) / 3) to (by - (by - ay) / 3))
                antinodes.add((ax - (ax - bx) / 3) to (ay - (ay - by) / 3))
            }
            antinodes
        }
    }.filter { it.x in 0 until m && it.y in 0 until n }.toSet()
}

fun getLines(antennas: Map<Char, Set<Pos>>): Set<Pair<Rational, Rational>> {
    return antennas.keys.flatMap { c ->
        antennas[c]!!.pairs().map { (p1, p2) ->
            val a = Rational((p2.y - p1.y).toBigInteger(), (p2.x - p1.x).toBigInteger()).simplify()
            val b = Rational(p1.y.toBigInteger()) - a * p1.x.toBigInteger()
            Pair(a, b) // ax + b
        }
    }.toSet()
}

private fun <E> Collection<E>.pairs(): Collection<Pair<E, E>> {
    return this
        .flatMap { a -> this.map { b -> a to b } }
        .filter { (a, b) -> a != b }
        .distinctBy { (a, b) -> setOf(a, b) }
}
