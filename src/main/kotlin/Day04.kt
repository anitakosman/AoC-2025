fun main() {
    val input = getInput("day4").readLines().map { it.toCharArray() }
    println(input.withIndex().sumOf { (y, line) ->
        line.withIndex().sumOf { (x, c) ->
            if (c != 'X') {
                0
            } else {
                Direction.entries.count { spellsXmas(it, x, y, input) }
            }
        }
    })
    println(input.withIndex().sumOf { (y, line) ->
        line.withIndex().sumOf { (x, c) ->
            if (c != 'A') {
                0
            } else {
                Direction.diagonals.count { spellsMas(it, x, y, input) }
            }
        }
    })
}

fun spellsXmas(direction: Direction, x: Int, y: Int, input: List<CharArray>): Boolean {
    var pos = x to y
    val m = input[0].size
    val n = input.size

    pos = direction.move(pos)
    if (pos.first !in 0 until m || pos.second !in 0 until n || input[pos.second][pos.first] != 'M') {
        return false
    }

    pos = direction.move(pos)
    if (pos.first !in 0 until m || pos.second !in 0 until n || input[pos.second][pos.first] != 'A') {
        return false
    }

    pos = direction.move(pos)
    return !(pos.first !in 0 until m || pos.second !in 0 until n || input[pos.second][pos.first] != 'S')
}

fun spellsMas(direction: Direction, x: Int, y: Int, input: List<CharArray>): Boolean {
    val pos = x to y
    val m = input[0].size
    val n = input.size

    val pos1 = direction.moveBack(pos)
    if (pos1.first !in 0 until m || pos1.second !in 0 until n || input[pos1.second][pos1.first] != 'M') {
        return false
    }

    val pos2 = direction.move(pos)
    if (pos2.first !in 0 until m || pos2.second !in 0 until n || input[pos2.second][pos2.first] != 'S') {
        return false
    }

    val pos3 = direction.turnLeft().moveBack(pos)
    if (pos3.first !in 0 until m || pos3.second !in 0 until n || input[pos3.second][pos3.first] != 'M') {
        return false
    }

    val pos4 = direction.turnLeft().move(pos)
    return pos4.first in 0 until m && pos4.second in 0 until n && input[pos4.second][pos4.first] == 'S'
}
