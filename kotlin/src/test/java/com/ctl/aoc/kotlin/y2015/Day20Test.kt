package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Primes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {

    @Test
    fun presentSequence() {
//        println(Primes.allPrimes().take(10).toList())
        val primes = Primes.allPrimes().take(100).toList()
        println(Primes.primeFactorisation(7 * 5 * 5 * 7 * 101, primes).toList())
        println(Primes.primeFactorisation(7 * 5 * 5, primes).toList())
    }

    @Test
    fun solve1() {
        assertEquals(6, Day20.solve1(120))
        assertEquals(8, Day20.solve1(150))
        println(Day20.solve1(34000000))
    }

    @Test
    internal fun solve2() {
        println(Day20.solve2(340000))
    }
}