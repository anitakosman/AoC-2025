fun main() {
    val input = getInputLines("day10")
    val allIndicatorLights = input.map { it.substringBefore("] ").drop(1) }
    val allButtons = input.map { line ->
        line.split(" ").drop(1).dropLast(1).map { buttonWiring ->
            buttonWiring.drop(1).dropLast(1).split(",").map { it.toInt() }
        }
    }
    val allJoltages = input.map { line ->
        line.substringAfter(" {").dropLast(1).split(",").map { it.toInt() }
    }

    println(part1(allIndicatorLights, allButtons))
    println(part2(allJoltages, allButtons))
}

private fun part1(allIndicatorLights: List<String>, allButtons: List<List<List<Int>>>): Int {
    val press = mutableMapOf<Pair<String, List<Int>>, String>()
    return allIndicatorLights.indices.sumOf { i ->
        val indicatorLights = allIndicatorLights[i]
        val buttons = allButtons[i]
        var presses = 0
        var possibleLightsConfigurations = setOf(".".repeat(indicatorLights.length))
        while (indicatorLights !in possibleLightsConfigurations) {
            possibleLightsConfigurations = possibleLightsConfigurations.flatMap { possibleLights ->
                buttons.map { button -> press.computeIfAbsent(Pair(possibleLights, button)) { toggleLights(it.first, it.second) } }
            }.toSet()
            presses++
        }
        presses
    }
}

private fun toggleLights(lights: String, button: List<Int>) = lights.mapIndexed { i, c ->
    if (i in button) {
        if (c == '.') '#' else '.'
    } else c
}.joinToString("")

private fun part2(allJoltages: List<List<Int>>, allButtons: List<List<List<Int>>>): Long {
    return allJoltages.indices.sumOf { i ->
        val joltages = allJoltages[i]
        val buttons = allButtons[i]
        var presses = 0
        var allPossibleJoltages = setOf(List(joltages.size) { 0 })
        while (joltages !in allPossibleJoltages) {
            allPossibleJoltages = allPossibleJoltages
                .flatMap { possibleJoltages ->
                    buttons.map { button -> possibleJoltages.mapIndexed { i, joltage -> if (i in button) joltage + 1 else joltage } }
                }.toSet()
                .filter { possibleJoltages ->
                    possibleJoltages.withIndex().all { (i, joltage) -> joltage <= joltages[i] }
                }.toSet()
            presses++
        }
        println("$i: $presses")
        presses.toLong()
    }
}
