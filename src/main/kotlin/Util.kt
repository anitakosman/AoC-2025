import java.io.File
import java.math.BigInteger

fun getInput(fileName: String): File {
    return File(object {}.javaClass.getResource("/${fileName}.txt")!!.file)
}

val lineSeparator: String = System.lineSeparator()

enum class Direction(private val dx: Int, private val dy: Int) {
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    NORTH(0, -1),
    NORTHEAST(1, -1),
    SOUTHEAST(1, 1),
    SOUTHWEST(-1, 1),
    NORTHWEST(-1, -1);

    companion object {
        val orthogonals = listOf(EAST, SOUTH, WEST, NORTH)
        val diagonals = listOf(NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
    }

    fun move(pos: Pos) = (pos.x + dx) to (pos.y + dy)

    fun moveBack(pos: Pos) = (pos.x - dx) to (pos.y - dy)

    fun turnLeft() = when (this) {
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
        NORTH -> WEST
        NORTHEAST -> NORTHWEST
        SOUTHEAST -> NORTHEAST
        SOUTHWEST -> SOUTHEAST
        NORTHWEST -> SOUTHWEST
    }

    fun turnRight() = when (this) {
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
        NORTH -> EAST
        NORTHEAST -> SOUTHEAST
        SOUTHEAST -> SOUTHWEST
        SOUTHWEST -> NORTHWEST
        NORTHWEST -> NORTHEAST
    }
}

typealias Pos = Pair<Int, Int>
val Pos.x: Int
    get() = first
val Pos.y: Int
    get() = second

typealias Dir = Pair<Int, Int>
val Dir.dx: Int
    get() = first
val Dir.dy: Int
    get() = second

data class Rational(val nominator: BigInteger, val denominator: BigInteger = BigInteger.ONE) {
    fun simplify(): Rational {
        val gcd = gcd(nominator, denominator)
        return Rational(nominator * denominator.signum().toBigInteger() / gcd, denominator * denominator.signum().toBigInteger() / gcd)
    }

    operator fun plus(other: Rational): Rational {
        val lcm = lcm(this.denominator, other.denominator)
        return Rational(this.nominator * lcm / this.denominator + other.nominator * lcm / other.denominator, lcm).simplify()
    }

    operator fun minus(other: Rational) = this + Rational(-other.nominator, other.denominator)

    operator fun times(other: Rational) = Rational(nominator * other.nominator, denominator * other.denominator).simplify()

    operator fun times(x: BigInteger) = this * Rational(x)

    operator fun div(other: Rational) = this * Rational(other.denominator, other.nominator)

    operator fun unaryMinus() = this * Rational(BigInteger.valueOf(-1))

    operator fun compareTo(other: Rational): Int =
        (this.nominator * other.denominator).compareTo(other.nominator * this.denominator)

    override fun equals(other: Any?): Boolean {
        when (other) {
            is Rational -> {
                val simplified = this.simplify()
                val otherSimplified = other.simplify()
                return simplified.nominator == otherSimplified.nominator && simplified.denominator == otherSimplified.denominator
            }

            is BigInteger -> {
                val simplified = this.simplify()
                return simplified.nominator == other && simplified.denominator == BigInteger.ONE
            }

            else -> {
                return false
            }
        }
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

operator fun BigInteger.times(rational: Rational) = rational.times(this)

fun gcd(x: BigInteger, y: BigInteger): BigInteger = x.gcd(y)

fun lcm(x: BigInteger, y: BigInteger) = ((x * y) / gcd(x, y))
