package com.ctl.aoc.kotlin.utils

import java.math.BigInteger

typealias Factorisation = Pair<BigInteger, Int>

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
        if (n < BigInteger.TWO) return false
        var i = BigInteger.ZERO
        while ((i * i) <= n) {
            if (n.mod(i) == BigInteger.ZERO) return false
            i = i.add(BigInteger.ONE)
        }
        return true
    }

    fun allPrimes(): Sequence<BigInteger> = sequence {
        var i = BigInteger.valueOf(2)
        while (true) {
            while (!i.isProbablePrime(15)) {
                i++
            }
            yield(i)
            i++
        }
    }

    fun divisorFunction(degree: Int, factorisation: Factorisation): BigInteger {
        val (prime, n) = factorisation
        return prime.pow((n + 1) * degree).subtract(BigInteger.ONE).divide(prime.pow(degree).subtract(BigInteger.ONE))
    }

    fun numberOfDivisor(factorisation: Factorisation): BigInteger = divisorFunction(0, factorisation)

    fun sumOfDivisor(factorisation: Factorisation): BigInteger = divisorFunction(1, factorisation)

    fun primeFactorisation(n: Long, primes: List<BigInteger>): Sequence<Factorisation> = sequence {
        var current = BigInteger.valueOf(n)
        val it = primes.iterator()
        while (current > BigInteger.ONE) {
            require(it.hasNext()) { "Not enough primes" }
            val prime = it.next()
            var count = 0
            while (current > BigInteger.ZERO && (current % prime == BigInteger.ZERO)) {
                current /= prime
                count++
            }
            if (count > 0) {
                yield(prime to count)
            }
        }
    }


}