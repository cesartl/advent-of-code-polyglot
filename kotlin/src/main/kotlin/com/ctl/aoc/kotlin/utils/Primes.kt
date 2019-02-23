package com.ctl.aoc.kotlin.utils

import java.math.BigInteger

object Primes {

    fun isPrime(n: Long): Boolean {
        if (n < 2) return false
        var i = 1L
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

    fun allPrimes(): Sequence<Long> = sequence {
        var i = 2L
        while (true) {
            while (!BigInteger.valueOf(i).isProbablePrime(15)) {
                i++
            }
            yield(i)
            i++
        }
    }

    fun primeFactorisation(n: Long, primes: List<Long>): Sequence<Pair<Long, Int>> = sequence {
        var current = n
        val it = primes.iterator()
        while (current > 1) {
            if (!it.hasNext()) {
                throw IllegalArgumentException("Not enough primes")
            }
            var prime = it.next()
            var count = 0
            while (current > 0 && (current % prime == 0L)) {
                current /= prime
                count++
            }
            if (count > 0) {
                yield(prime to count)
            }
        }
    }


}