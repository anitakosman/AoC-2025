fun main() {
    val input = getInput("day5").readText().split(lineSeparator + lineSeparator)
    val rules = mutableMapOf<String, MutableSet<String>>()
    input[0].lines().map {
        with(it.split("|")) {
            this[0] to this[1]
        }
    }.forEach { (k, v) ->
        rules.computeIfAbsent(k) { _ -> mutableSetOf() }.add(v)
    }
    val updates = input[1].lines().map { it.split(",") }

    println(updates.filter { validUpdate(it, rules) }.sumOf { it[it.size / 2].toInt() })
    println(updates.filter { !validUpdate(it, rules) }.map { correct(it, rules) }.sumOf { it[it.size / 2].toInt() })
}

fun validUpdate(update: List<String>, rules: Map<String, Set<String>>): Boolean {
    val seen = mutableSetOf(update[0])
    update.drop(1).forEach { x ->
        if (seen.intersect((rules[x] ?: emptySet()).toSet()).isNotEmpty()) {
            return false
        }
        seen.add(x)
    }
    return true
}

fun correct(update: List<String>, rules: Map<String, Set<String>>): List<String> {
    val extendedRules = getExtendedRules(rules.filterKeys { it in update }.mapValues { it.value.intersect(update) })
    val corrected = mutableListOf(update[0])
    update.drop(1).forEach { x ->
        val i = corrected.indexOfFirst { it in (extendedRules[x] ?: emptySet()) }
        if (i == -1) {
            corrected.add(x)
        } else {
            corrected.add(i, x)
        }
    }
    return corrected
}

private fun getExtendedRules(rules: Map<String, Set<String>>): Map<String, Set<String>> {
    val extendedRules: MutableMap<String, Set<String>> = rules.toMutableMap()
    var needExtending = true
    while (needExtending) {
        needExtending = false
        extendedRules.forEach { (k, v) ->
            val extendedV = v.map { extendedRules[it] ?: emptySet() }.fold(emptySet<String>()) { a, b -> a.union(b)}.union(v)
            if (v != extendedV) {
                extendedRules[k] = extendedV
                needExtending = true
            }
        }
    }
    return extendedRules
}
