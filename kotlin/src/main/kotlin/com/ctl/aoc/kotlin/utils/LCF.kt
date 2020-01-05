package com.ctl.aoc.kotlin.utils

import java.math.BigInteger

data class LCF(val a: ModInt, val b: ModInt) {

    constructor(a: BigInteger, b: BigInteger, m: BigInteger) : this(ModInt(a, m), ModInt(b, m))
    constructor(a: Long, b: Long, m: BigInteger) : this(BigInteger.valueOf(a), BigInteger.valueOf(b), m)

    infix fun andThen(g: LCF): LCF {
        val (c, d) = g
        return LCF(c * a, c * b + d)
    }

    fun apply(x: ModInt): ModInt = a * x + b

    companion object {
        fun identify(m: BigInteger) = LCF(1L, 0, m)
    }
}