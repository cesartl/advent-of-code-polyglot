package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Primes
import java.math.BigInteger

object Day20 {
    fun presentSequenceOld(start: Int = 0): Sequence<Int> = sequence {
        val nats = (1..start).toMutableList()
        println("nats $nats")
        var current = start

        while (true) {
            current++
            nats.add(current)
            val value = nats.filter { current % it == 0 }.map { it * 10 }.sum()
//            println("doing $current ($value)")
            yield(value)
        }
    }

    fun presentSequence(start: Long = 0): Sequence<Long> = sequence {
        var current = start
        while (true) {
            val value = sumDivisor(current) * 10
//            println("doing $current ($value)")
            yield(value)
            current++
        }
    }


    val primes = Primes.allPrimes().take(100000).toList()


    fun sumDivisor(n: Long): Long {
        if(n == 1L) return 1L
        return Primes.primeFactorisation(n, primes)
                .map { (prime, n) ->
                    val bigPrime = BigInteger.valueOf(prime)
                    bigPrime.pow(n + 1).subtract(BigInteger.ONE).divide(bigPrime.subtract(BigInteger.ONE))
                }.reduce(BigInteger::multiply).toLong()
    }

    fun solve1(n: Long): Int {
        val start = Math.sqrt((n / 10).toDouble()).toLong()
        println("start $start")
        return presentSequence(start).withIndex().dropWhile { it.value < n }.first().index  + start.toInt()
    }
}