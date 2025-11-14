import kotlin.math.pow

fun main() {
    val input = getInput("day17").readText()
    val inputGroups = Regex(
        "Register A: (\\d+)" + lineSeparator +
                "Register B: (\\d+)" + lineSeparator +
                "Register C: (\\d+)" + lineSeparator + lineSeparator +
                "Program: ([\\d,]+)"
    ).matchEntire(input)!!.groups
    val program = ProgramState(
        inputGroups[1]!!.value.toLong(),
        inputGroups[2]!!.value.toLong(),
        inputGroups[3]!!.value.toLong(),
        inputGroups[4]!!.value.split(",").map { it.toInt() })

    println(run(program).joinToString(","))

    var a = listOf(0L)
    val n = program.program.size
    repeat(n) { i ->
        a = a.map { it * 8L }
        a = (0..7)
            .flatMap { k -> a.map { it + k } }
            .filter { run(program.setA(it))[0] == program.program[n - i - 1] }
    }
    println(a.sorted()[0])
}

private fun run(program: ProgramState): List<Int> {
    var programState = program
    while (!programState.isFinished()) {
        programState = programState.doOperation()
    }
    return programState.output
}

data class ProgramState(
    val a: Long, val b: Long, val c: Long, val program: List<Int>,
    val output: List<Int> = emptyList(), val instructionPointer: Int = 0
) {
    fun doOperation(): ProgramState {
        val opcode = program[instructionPointer]
        val operand = program[instructionPointer + 1]
        return when (opcode) {
            0 -> {
                val n = comboOperand(operand).toInt()
                val newA = a / (2.0.pow(n)).toInt()
                ProgramState(newA, b, c, program, output, instructionPointer + 2)
            }

            1 -> {
                val newB = b.xor(operand.toLong())
                ProgramState(a, newB, c, program, output, instructionPointer + 2)
            }

            2 -> {
                val newB = comboOperand(operand).mod(8).toLong()
                ProgramState(a, newB, c, program, output, instructionPointer + 2)
            }

            3 -> {
                ProgramState(a, b, c, program, output, if (a != 0L) operand else instructionPointer + 2)
            }

            4 -> {
                val newB = b.xor(c)
                ProgramState(a, newB, c, program, output, instructionPointer + 2)
            }

            6 -> {
                val n = comboOperand(operand).toInt()
                val newB = a / (2.0.pow(n)).toInt()
                ProgramState(a, newB, c, program, output, instructionPointer + 2)
            }

            7 -> {
                val n = comboOperand(operand).toInt()
                val newC = a / (2.0.pow(n)).toInt()
                ProgramState(a, b, newC, program, output, instructionPointer + 2)
            }

            5 -> {
                val outputElement = comboOperand(operand).mod(8)
                ProgramState(a, b, c, program, output.plus(outputElement), instructionPointer + 2)
            }

            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    private fun comboOperand(operand: Int): Long {
        return when (operand) {
            in 0..3 -> operand.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> throw IllegalArgumentException()
        }
    }

    fun isFinished() = instructionPointer >= program.size

    fun setA(newA: Long) = ProgramState(newA, b, c, program, output, instructionPointer = instructionPointer)

}