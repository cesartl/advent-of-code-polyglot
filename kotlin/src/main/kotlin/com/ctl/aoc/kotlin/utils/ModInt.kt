package com.ctl.aoc.kotlin.utils

import java.math.BigInteger

data class ModInt(val value: BigInteger, val modulus: BigInteger) {
    private fun assertModulus(other: ModInt) {
        if (modulus != other.modulus) {
            throw IllegalArgumentException("Modulus are different")
        }
    }

    operator fun plus(other: ModInt): ModInt {
        assertModulus(other)
        return ModInt(value.plus(other.value).mod(modulus), modulus)
    }

    operator fun minus(other: ModInt): ModInt {
        assertModulus(other)
        return ModInt(value.min(other.value).mod(modulus), modulus)
    }

    operator fun times(other: ModInt): ModInt {
        assertModulus(other)
        return ModInt(value.times(other.value).mod(modulus), modulus)
    }

    operator fun div(other: ModInt): ModInt {
        assertModulus(other)
        return ModInt(value.times(other.value.modInverse(modulus)).mod(modulus), modulus)
    }
}