import java.io.File

fun getInput(fileName: String) = File(object {}.javaClass.getResource("/${fileName}.txt")!!.file)

fun getInputLines(fileName: String) = getInput(fileName).readLines()

fun getInputText(fileName: String) = getInput(fileName).readText()

fun getInputBlocks(fileName: String) = getInputText(fileName).split(lineSeparator + lineSeparator)

val lineSeparator: String = System.lineSeparator()