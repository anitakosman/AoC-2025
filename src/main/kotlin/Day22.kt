fun main() {
    val input = getInput("day22").readLines().map { it.toLong() }

    val secrets = evolveSecrets(input)
    println(secrets.sumOf { it.drop(2000).first() })

    val prices = secrets.map { buyer -> buyer.map { it.mod(10) } }
    val priceChanges = prices.map { buyer -> buyer.zipWithNext().map { it.second - it.first } }
    val monkeySequences = priceChanges.flatMap { it.windowed(4) }.toSet()

    println(monkeySequences.maxOf { monkeySequence ->
        priceChanges.withIndex().sumOf { (buyer, priceChange) ->
            val indexOf = priceChange.toList().indexOf(monkeySequence)
            if (indexOf >= 0) prices[buyer][indexOf + 4] else 0
        }
    })
}

fun evolveSecrets(input: List<Long>): List<List<Long>> {
    return input.map { seed ->
        generateSequence(seed) { s ->
            val a = s * 64
            var newS = s.xor(a).mod(16777216L)
            val b = newS / 32
            newS = newS.xor(b).mod(16777216L)
            val c = newS * 2048
            newS = newS.xor(c).mod(16777216L)
            newS
        }.take(2001).toList()
    }
}

fun <T> List<T>.indexOf(l: List<T>): Int {
    val indexedL = l.withIndex()
    indices.forEach { i ->
        if (indexedL.all { (j, e) -> i + j < size && this[i + j] == e })
            return i
    }
    return -1
}
