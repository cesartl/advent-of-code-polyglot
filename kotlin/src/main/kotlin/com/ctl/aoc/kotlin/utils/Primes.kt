package com.ctl.aoc.kotlin.utils

import java.math.BigInteger

object Primes {

    fun isPrime(n: Long): Boolean {
        if (n < 2) return false
        var i = 0L
        while (i * i <= n) {
            if ((n % i) == 0L) return false
            i++
        }
        return true
    }

    fun isPrime(n: Int): Boolean = isPrime(n.toLong())

    fun isPrime(n: BigInteger): Boolean {
        if (n < 2) return false
        var i = BigInteger.ZERO
        while ((i * i) <= n) {
            if (n.mod(i) == BigInteger.ZERO) return false
            i = i.add(BigInteger.ONE)
        }
        return true
    }



}