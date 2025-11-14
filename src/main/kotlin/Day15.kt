fun main() {
    val input = getInput("day15").readText().split(lineSeparator + lineSeparator)

    val moves = input[1].lines().flatMap { it.toList() }
    var robot = Pos(-1, -1)
    val walls = mutableSetOf<Pos>()
    val boxes = mutableSetOf<Pos>()
    input[0].lines().forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            when (c) {
                '@' -> robot = Pos(x, y)
                '#' -> walls.add(Pos(x, y))
                'O' -> boxes.add(Pos(x, y))
            }
        }
    }

    val walls2 = walls.map { Pos(it.x * 2, it.y) }.toSet()
    val boxes2 = boxes.map { Pos(it.x * 2, it.y) }.toMutableSet()
    val robot2 = Pos(robot.x * 2, robot.y)

    walk(robot, walls, boxes, moves)
    println(boxes.sumOf { 100L * it.y + it.x })

    walk2(robot2, walls2, boxes2, moves)
    println(boxes2.sumOf { 100L * it.y + it.x })
}

private fun walk(start: Pos, walls: Set<Pos>, boxes: MutableSet<Pos>, moves: List<Char>) {
    var robot = start
    moves.forEach { move ->
        val d = getDirection(move)
        when (val moveTo = d.move(robot)) {
            in walls -> {
                //Do nothing
            }

            in boxes -> {
                var pushTo = d.move(moveTo)
                while (pushTo in boxes) {
                    pushTo = d.move(pushTo)
                }
                if (pushTo !in walls) {
                    robot = moveTo
                    boxes.remove(moveTo)
                    boxes.add(pushTo)
                }
            }

            else -> {
                robot = moveTo
            }
        }
    }
}

private fun walk2(start: Pos, walls: Set<Pos>, boxes: MutableSet<Pos>, moves: List<Char>) {
    var robot = start
    moves.forEach { move ->
        val d = getDirection(move)
        val moveTo = d.move(robot)
        val leftOfMoveTo = Direction.WEST.move(moveTo)
        val boxesToMove = listOf(moveTo, leftOfMoveTo).filter { it in boxes }
        if (moveTo in walls || leftOfMoveTo in walls) {
            //Do nothing
        } else if (boxesToMove.isNotEmpty()) {
            if (moveBoxes(boxesToMove, d, boxes, walls)) {
                robot = moveTo
            }
        } else {
            robot = moveTo
        }
    }
}

private fun moveBoxes(boxesToMove: List<Pos>, d: Direction, boxes: MutableSet<Pos>, walls: Set<Pos>): Boolean {
    val moveToSpaces = boxesToMove.flatMap {
        val moveTo = d.move(it)
        if (d == Direction.WEST || d == Direction.EAST) {
            listOf(d.move(moveTo))
        } else {
            listOf(moveTo, Direction.EAST.move(moveTo), Direction.WEST.move(moveTo))
        }
    }

    if (moveToSpaces.any { it in walls }) {
        return false
    }

    val newBoxesToMove = moveToSpaces.filter { it in boxes }
    if (newBoxesToMove.isNotEmpty()) {
        if (!moveBoxes(newBoxesToMove, d, boxes, walls)) {
            return false
        }
    }

    boxes.removeAll(boxesToMove.toSet())
    boxes.addAll(boxesToMove.map { d.move(it) })
    return true
}

private fun getDirection(move: Char): Direction {
    val d = when (move) {
        '>' -> Direction.EAST
        '^' -> Direction.NORTH
        '<' -> Direction.WEST
        'v' -> Direction.SOUTH
        else -> throw IllegalArgumentException()
    }
    return d
}
