enum class Direction(private val dx: Int, private val dy: Int) {
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    NORTH(0, -1),
    NORTHEAST(1, -1),
    SOUTHEAST(1, 1),
    SOUTHWEST(-1, 1),
    NORTHWEST(-1, -1);

    companion object {
        val orthogonals = listOf(EAST, SOUTH, WEST, NORTH)
        val diagonals = listOf(NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
    }

    fun move(pos: Pos) = (pos.x + dx) to (pos.y + dy)

    fun moveBack(pos: Pos) = (pos.x - dx) to (pos.y - dy)

    fun turnLeft() = when (this) {
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
        NORTH -> WEST
        NORTHEAST -> NORTHWEST
        SOUTHEAST -> NORTHEAST
        SOUTHWEST -> SOUTHEAST
        NORTHWEST -> SOUTHWEST
    }

    fun turnRight() = when (this) {
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
        NORTH -> EAST
        NORTHEAST -> SOUTHEAST
        SOUTHEAST -> SOUTHWEST
        SOUTHWEST -> NORTHWEST
        NORTHWEST -> NORTHEAST
    }
}

typealias Pos = Pair<Int, Int>
val Pos.x: Int
    get() = first
val Pos.y: Int
    get() = second
fun Pos.orthogonals() = Direction.orthogonals.map { it.move(this) }
fun Pos.diagonals() = Direction.diagonals.map { it.move(this) }
fun Pos.neighbours() = this.orthogonals() + this.diagonals()

typealias Move = Pair<Int, Int>
val Move.dx: Int
    get() = first
val Move.dy: Int
    get() = second

fun getGridPositions(grid: List<String>, char: Char): Set<Pos> {
    return grid.flatMapIndexed { y, line ->
        line.indices.filter { line[it] == char }.map { it to y }
    }.toSet()
}
