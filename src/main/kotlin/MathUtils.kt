import java.math.BigInteger

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

operator fun BigInteger.rangeTo(other: BigInteger) = BigIntegerRange(this, other)

class BigIntegerRange(override val start: BigInteger, override val endInclusive: BigInteger) : ClosedRange<BigInteger>, Iterable<BigInteger> {
    override operator fun iterator(): Iterator<BigInteger> = BigIntegerRangeIterator(this)
}

class BigIntegerRangeIterator(private val range: ClosedRange<BigInteger>) : Iterator<BigInteger> {
    private var current = range.start

    override fun hasNext(): Boolean = current <= range.endInclusive

    override fun next(): BigInteger {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return current++
    }
}


fun gcd(x: BigInteger, y: BigInteger): BigInteger = x.gcd(y)

fun lcm(x: BigInteger, y: BigInteger) = ((x * y) / gcd(x, y))