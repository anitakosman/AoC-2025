fun main() {
    val input = getInput("day14").readLines()
        .map { Regex("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)").matchEntire(it)!!.groups }
        .map { Pos(it[1]!!.value.toInt(), it[2]!!.value.toInt()) to Dir(it[3]!!.value.toInt(), it[4]!!.value.toInt()) }

    val t100 = tn(input, 100)
    println(t100.count { it.x in 0..49 && it.y in 0..50 }
            * t100.count { it.x in 0..49 && it.y in 52..102 }
            * t100.count { it.x in 51..100 && it.y in 0..50 }
            * t100.count { it.x in 51..100 && it.y in 52..102 })

    var n = 5000
    while (true) {
        val tn = tn(input, n)
        if (tn.sumOf { p -> Direction.entries.count { it.move(p) in tn } } > 450) {
            println(n)
            (0..102).forEach { y ->
                (0..100).forEach { x ->
                    print(if (Pos(x, y) in tn) 'X' else '.')
                }
                println()
            }
            readln()
        }
        n++
    }
}

private fun tn(input: List<Pair<Pos, Dir>>, n: Int) =
    input.map { (p, v) -> Pos((p.x + n * v.dx).mod(101), (p.y + n * v.dy).mod(103)) }