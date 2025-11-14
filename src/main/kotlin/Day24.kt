fun main() {
    val input = getInput("day24").readText().split(lineSeparator + lineSeparator)

    val ruleRegex = Regex("(\\w+) (AND|OR|XOR) (\\w+) -> (\\w+)")
    val rules = input[1].lines().associate {
        val groups = ruleRegex.matchEntire(it)!!.groupValues
        groups[4] to Rule(groups[1], groups[3], BooleanOperation.valueOf(groups[2]))
    }

    part1(input, rules)
    part2(rules)
}

private fun part1(input: List<String>, rules: Map<String, Rule>) {
    val startValueRegex = Regex("(\\w+): ([01])")
    val values = input[0].lines().associate {
        val groups = startValueRegex.matchEntire(it)!!.groupValues
        groups[1] to (groups[2] == "1")
    }.toMutableMap()

    rules.keys.forEach { output ->
        values[output] = calcValue(values, output, rules)
    }

    println(
        values.keys
            .filter { it.startsWith("z") }
            .sorted()
            .joinToString("") { if (values[it] == true) "1" else "0" }
            .reversed()
            .toLong(2)
    )
}

private fun calcValue(values: MutableMap<String, Boolean>, output: String, rules: Map<String, Rule>): Boolean {
    return if (values.containsKey(output)) {
        values[output]!!
    } else {
        val rule = rules[output]!!
        rule.operation.calc(calcValue(values, rule.input1, rules), calcValue(values, rule.input2, rules))
    }
}

private fun part2(rules: Map<String, Rule>){
    var carry = findRule(rules, "x00", "y00", BooleanOperation.AND)!!
    val switched = mutableSetOf<String>()

    (1..44).forEach { n ->
        val paddedN = padded(n)
        var xor = findRule(rules, "x$paddedN", "y$paddedN", BooleanOperation.XOR)!!
        var and = findRule(rules, "x$paddedN", "y$paddedN", BooleanOperation.AND)!!
        val currentBit = findRule(rules, carry, xor, BooleanOperation.XOR)
        if (currentBit == null) {
            var rule = findRule(rules, xor, BooleanOperation.XOR)
            if (rule == null) {
                rule = findRule(rules, carry, BooleanOperation.XOR)!!
                val currentBitRule = rule.value
                val actualXor = if (currentBitRule.input1 == carry){
                    currentBitRule.input2
                } else {
                    currentBitRule.input1
                }
                switched.add(xor)
                switched.add(actualXor)
                if (actualXor == and) {
                    and = xor
                }
                xor = actualXor
            } else {
                val currentBitRule = rule.value
                val actualCarry = if (currentBitRule.input1 == xor){
                    currentBitRule.input2
                } else {
                    currentBitRule.input1
                }
                switched.add(carry)
                switched.add(actualCarry)
                if (actualCarry == and) {
                    and = carry
                }
                carry = actualCarry
            }
        } else if (currentBit != "z$paddedN"){
            switched.add(currentBit)
            switched.add("z$paddedN")
        }

        val carryAndXor = findRule(rules, carry, xor, BooleanOperation.AND)!!
        val newCarry = findRule(rules, carryAndXor, and, BooleanOperation.OR)
        if (newCarry == null) {
            var rule = findRule(rules, and, BooleanOperation.OR)
            if (rule == null) {
                rule = findRule(rules, carryAndXor, BooleanOperation.OR)!!
                val carryRule = rule.value
                val actualAnd = if (carryRule.input1 == carryAndXor){
                    carryRule.input2
                } else {
                    carryRule.input1
                }
                switched.add(and)
                switched.add(actualAnd)
            } else {
                val carryRule = rule.value
                val actualCarryAndXor = if (carryRule.input1 == and){
                    carryRule.input2
                } else {
                    carryRule.input1
                }
                switched.add(carryAndXor)
                switched.add(actualCarryAndXor)
            }
            carry = rule.key
        } else {
            carry = newCarry
        }
    }

    println(switched.sorted().joinToString(","))
}

private fun padded(n: Int) = n.toString().padStart(2, '0')

private fun findRule(rules: Map<String, Rule>, input: String, operation: BooleanOperation) =
    rules.filter { it.value.operation == operation && input == it.value.input1 || input == it.value.input2 }.entries.firstOrNull()

private fun findRule(rules: Map<String, Rule>, input1: String, input2: String, operation: BooleanOperation) =
    rules.filter { it.value.operation == operation && setOf(it.value.input1, it.value.input2) == setOf(input1, input2) }.keys.firstOrNull()

enum class BooleanOperation {
    AND, OR, XOR;

    fun calc(input1: Boolean, input2: Boolean) = when (this) {
        AND -> input1 && input2
        OR -> input1 || input2
        XOR -> input1.xor(input2)
    }
}

data class Rule(val input1: String, val input2: String, val operation: BooleanOperation)