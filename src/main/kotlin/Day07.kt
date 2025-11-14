fun main() {
    val input = getInput("day7").readLines().map { line ->
        with(line.split(": ")) {
            this[0].toLong() to this[1].split(" ").map { it.toLong() }
        }
    }
    println(input.filter { equatable(it.first, it.second) }.sumOf { it.first })
    println(input.filter { equatable(it.first, it.second, true) }.sumOf { it.first })
}

fun equatable(result: Long, values: List<Long>, concatenationAvailable: Boolean = false) = result in values.drop(1)
    .fold(listOf(values[0])) { possibleResults, v -> possibleResults.flatMap {
        val additionAndMultiplication = listOf(it + v, it * v)
        if (concatenationAvailable){
            additionAndMultiplication.plus((it.toString() + v.toString()).toLong())
        } else {
            additionAndMultiplication
        }
    } }

