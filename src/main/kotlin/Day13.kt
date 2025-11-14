import java.math.BigInteger

fun main() {
    val input = getInput("day13").readText().split(lineSeparator + lineSeparator)
        .map {
            Regex(
                "Button A: X\\+(\\d+), Y\\+(\\d+)" + lineSeparator +
                        "Button B: X\\+(\\d+), Y\\+(\\d+)" + lineSeparator +
                        "Prize: X=(\\d+), Y=(\\d+)"
            ).matchEntire(it)!!.groups
        }
        .map {
            ClawMachine(
                it[1]!!.value.toBigInteger(), it[2]!!.value.toBigInteger(), it[3]!!.value.toBigInteger(),
                it[4]!!.value.toBigInteger(), it[5]!!.value.toBigInteger(), it[6]!!.value.toBigInteger()
            )
        }
    println(input.sumOf { tokensNeeded(it) })
    println(input.map { ClawMachine(it.ax, it.ay, it.bx, it.by, it.x + (10000000000000).toBigInteger(), it.y + (10000000000000).toBigInteger()) }.sumOf { tokensNeeded(it) })
}

fun tokensNeeded(clawMachine: ClawMachine): BigInteger {
    if (Rational(clawMachine.ax, clawMachine.bx) == Rational(clawMachine.ay, clawMachine.by)) {
        return if (clawMachine.x.mod(clawMachine.bx) != BigInteger.ZERO || clawMachine.y.mod(clawMachine.by) != BigInteger.ZERO) {
            BigInteger.ZERO
        } else if (Rational(clawMachine.ax, clawMachine.bx) > Rational(BigInteger.valueOf(3))) {
            clawMachine.x / clawMachine.bx
        } else {
            clawMachine.x.mod(clawMachine.ax) / clawMachine.bx + BigInteger.valueOf(3) * clawMachine.x / clawMachine.ax
        }
    } else if (Rational(clawMachine.x, clawMachine.ax) == Rational(clawMachine.y, clawMachine.ay)) {
        return if (clawMachine.x.mod(clawMachine.ax) != BigInteger.ZERO) {
            BigInteger.ZERO
        } else {
            BigInteger.valueOf(3) * clawMachine.x / clawMachine.ax
        }
    }  else if (Rational(clawMachine.x, clawMachine.bx) == Rational(clawMachine.y, clawMachine.by)) {
        return if (clawMachine.x.mod(clawMachine.bx) != BigInteger.ZERO) {
            BigInteger.ZERO
        } else {
            clawMachine.x / clawMachine.bx
        }
    } else {
        val u = Rational(clawMachine.bx * clawMachine.y - clawMachine.by * clawMachine.x)
        val v = Rational(clawMachine.ax * clawMachine.bx * clawMachine.y - clawMachine.ay * clawMachine.bx * clawMachine.x)
        val w = Rational(clawMachine.ax) - v / u
        val a = Rational(clawMachine.x) / w
        val z = -a * (clawMachine.ax * clawMachine.y - clawMachine.ay * clawMachine.x)
        val b = z / u
        return if (a.denominator == BigInteger.ONE && b.denominator == BigInteger.ONE) BigInteger.valueOf(3) * a.nominator + b.nominator else BigInteger.ZERO
    }
}

data class ClawMachine(val ax: BigInteger, val ay: BigInteger, val bx: BigInteger, val by: BigInteger, val x: BigInteger, val y: BigInteger)

// a * ax + b * bx = x
// a * ay + b * by = y
// =>
// a * (ax * y - ay * x) + b * (bx * y - by * x) = 0
// =>
// b * (bx * y - by * x) = -a * (ax * y - ay * x)
// =>
// b = -a * (ax * y - ay * x) / (bx * y - by * x)
// =>
// a * ax - a * (ax * y - ay * x) / (bx * y - by * x) * bx = x
// =>
// a * (ax - (ax * y - ay * x) / (bx * y - by * x) * bx) = x
// =>
// a = x / (ax - (ax * y - ay * x) / (bx * y - by * x) * bx)
// =>
// a = x / (ax - (ax * bx * y - ay * bx * x) / (bx * y - by * x))

