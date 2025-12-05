fun main() {
    val input = getInputLines("day4")
    val paperRolls = getGridPositions(input, '@')

    println(part1(paperRolls))
    println(part2(paperRolls))
}

private fun part1(paperRolls: Set<Pos>): Int {
    return paperRolls.count { roll -> roll.neighbours().count { it in paperRolls } < 4 }
}

private fun part2(paperRolls: Set<Pos>): Int {
    var rollsLeft = paperRolls
    var oldCount: Int
    var newCount = paperRolls.size
    do {
        oldCount = newCount
        rollsLeft = rollsLeft.filter { roll -> roll.neighbours().count { it in rollsLeft } >= 4 }.toSet()
        newCount = rollsLeft.count()
    } while (oldCount > newCount)
    return paperRolls.size - oldCount
}
