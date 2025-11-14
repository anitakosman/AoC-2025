import kotlin.math.sign

fun main() {
    val reports = getInput("day2").readText()
        .lines()
        .map { note -> note.split(" ").map { it.toInt() } }

    println(reports.count { safe(it) })
    println(reports.count { safe(it, true) })
}

fun safe(report: List<Int>, problemDampener: Boolean = false): Boolean {
    val diffs = report.zipWithNext().map { it.second - it.first }
    val nonGradualLevelIndex = diffs.indexOfFirst { it == 0 || it > 3 || it < -3 }

    val firstDecrease = diffs.indexOfFirst { it.sign == -1 }
    val firstIncrease = diffs.indexOfFirst { it.sign == 1 }

    return (nonGradualLevelIndex == -1 && (firstDecrease == -1 || firstIncrease == -1)) || (problemDampener
            && setOf(
        nonGradualLevelIndex,
        nonGradualLevelIndex + 1,
        firstDecrease,
        firstDecrease + 1,
        firstIncrease,
        firstIncrease + 1
    )
        .filter { it in report.indices }
        .any { safe(report.withoutItemAt(it)) })
}

fun <T> Iterable<T>.withoutItemAt(index: Int): List<T> =
    take(index) + drop(index + 1)