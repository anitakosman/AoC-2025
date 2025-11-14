import kotlin.math.abs

fun main() {
    val input = getInput("day1").readText()
        .lines()
        .map { note -> note.split("   ") }
    val listA = input.map { it[0].toInt() }.sorted()
    val listB = input.map { it[1].toInt() }.sorted()
    val countB = listB.groupingBy { it }.eachCount()

    println(listA.zip(listB).sumOf { (a, b) -> abs(a - b) })
    println(listA.sumOf { it * countB.getOrDefault(it, 0) })
}