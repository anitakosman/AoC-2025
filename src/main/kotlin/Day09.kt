import kotlin.math.min

fun main() {
    val input = getInput("day9").readText().map { it.toString().toInt() }.withIndex()
    part1(input)
    part2(input)
}

private fun part1(input: Iterable<IndexedValue<Int>>) {
    val files = input.filter { (index, _) -> index % 2 == 0 }
    val reverseFiles = files.reversed()
    val totalSize = files.sumOf { it.value }

    val blocks = mutableMapOf<Int, Int>()
    var blockIndex = 0
    var freeSpaceFilled = 0
    var indexOfFileToInsert = 0
    var blocksInsertedOfCurrentFileToInsert = 0
    val inputIterator = input.iterator()
    while (blockIndex < totalSize) {
        val (index, size) = inputIterator.next()
        if (index % 2 == 0) {
            val fileEnd = min(blockIndex + size, totalSize)
            (blockIndex until fileEnd).forEach { blocks[it] = index / 2 }
            blockIndex += size
        } else {
            var blocksFilled = 0
            while (blocksFilled < size) {
                val fileToInsert = reverseFiles[indexOfFileToInsert]
                val blocksToInsert = min(fileToInsert.value - blocksInsertedOfCurrentFileToInsert, size - blocksFilled)
                val insertEnd = min(blockIndex + blocksToInsert, totalSize)
                (blockIndex until insertEnd).forEach { blocks[it] = fileToInsert.index / 2 }
                blocksInsertedOfCurrentFileToInsert += blocksToInsert
                blocksFilled += blocksToInsert
                blockIndex += blocksToInsert

                if (blocksInsertedOfCurrentFileToInsert == fileToInsert.value) {
                    indexOfFileToInsert++
                    blocksInsertedOfCurrentFileToInsert = 0
                }
            }
            freeSpaceFilled += size
        }
    }

    println((0..totalSize).sumOf { it.toLong() * blocks.getOrDefault(it, 0) })
}

private fun part2(input: Iterable<IndexedValue<Int>>) {
    var blockIndex = 0
    val blocks = mutableMapOf<Int, Int>()
    input.forEach { (index, size) ->
        if (index % 2 == 0) {
            (blockIndex until blockIndex + size).forEach { blocks[it] = index / 2 }
        }
        blockIndex += size
    }
    input.filter { it.index % 2 == 0 }.reversed().forEach { (index, size) ->
        val fileIndex = index / 2
        val fileStart = blocks.filterValues { it == fileIndex }.keys.min()
        (0 until fileStart).find { i -> (i until i + size).none { blocks.containsKey(it) } }?.let { newFileStart ->
            (0 until size).forEach { i ->
                blocks[newFileStart + i] = fileIndex
                blocks.remove(fileStart + i)
            }
        }
    }

    println((0..blocks.keys.max()).sumOf { it.toLong() * blocks.getOrDefault(it, 0) })
}