package com.ctl.aoc.kotlin.utils

import java.math.BigInteger

/**
 * A Linear Congruential Function of the form y = a * x + b
 */
data class LCF(val a: ModInt, val b: ModInt) {

    constructor(a: BigInteger, b: BigInteger, m: BigInteger) : this(ModInt(a, m), ModInt(b, m))
    constructor(a: Long, b: Long, m: BigInteger) : this(BigInteger.valueOf(a), BigInteger.valueOf(b), m)

    infix fun andThen(g: LCF): LCF {
        val (c, d) = g
        return LCF(c * a, c * b + d)
    }

    fun apply(x: ModInt): ModInt = a * x + b

    /**
     * returns the x such that this.apply(x) == y
     */
    fun reverseApply(y: ModInt): ModInt {
        return (y - b) / a
    }

    companion object {
        fun identify(m: BigInteger) = LCF(1L, 0, m)
    }

    fun applyK(k: BigInteger): LCF {
        val identify = identify(a.modulus)
        if (k == BigInteger.ZERO) {
            return identify
        }
        var y = identify
        var x = this
        var n = k
        while (n > BigInteger.ONE) {
            if (n.testBit(0)) {
                //k is odd
                y = y andThen x
                x = x andThen x
                n -= BigInteger.ONE
            } else {
                x = x andThen x
            }
            n /= BigInteger.TWO
        }
        return y andThen x
    }
}