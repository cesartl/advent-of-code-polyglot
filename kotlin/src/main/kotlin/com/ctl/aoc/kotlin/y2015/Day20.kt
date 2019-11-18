package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Primes
import java.math.BigInteger
import kotlin.math.sqrt

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

    private fun presentSequence(start: Long = 0, valueFunction: (Long) -> Long): Sequence<Long> = sequence {
        var current = start
        while (true) {
            val value = valueFunction(current)
//            println("doing $current ($value)")
            yield(value)
            current++
        }
    }


    val primes = Primes.allPrimes().take(100000).toList()


    fun sumDivisor(n: Long): Long {
        if (n == 1L) return 1L
        return Primes.primeFactorisation(n, primes)
                .map { Primes.sumOfDivisor(it) }.reduce(BigInteger::multiply).toLong()
    }

    fun sumDivisor2(n: Long, maxDivisor: Int): Long {
        if (n == 1L) return 1L
        return Primes.primeFactorisation(n, primes)
                .map { Primes.sumOfDivisor(it) }.reduce(BigInteger::multiply).toLong()
    }

    fun solve1(n: Long): Int {
        val start = sqrt((n / 10).toDouble()).toLong()
        println("start $start")
        return presentSequence(start) { sumDivisor(it) * 10 }.withIndex().dropWhile { it.value < n }.first().index + start.toInt()
    }

    fun solve2(presentLimit: Long): Int {
        val start = sqrt((presentLimit / 10).toDouble()).toLong()
        println("start $start")
        return presentSequence(start) { sumDivisor(it) * 10 }.withIndex().dropWhile { it.value < presentLimit }.first().index + start.toInt()
    }

    const val maxDelivery: Long = 50;

    data class Elf(val n: Long) {
        var deliveries: Int = 0

        fun deliverPresent(): Long {
            val toDeliver = n * 11
            deliveries++
            return toDeliver
        }

        fun shouldDeliver(houseNumber: Long): Boolean {
            return houseNumber % n == 0L
        }
    }

    fun solve2Old(presentLimit: Long): Int {
        var elves = mutableSetOf<Elf>()
        var current = 1L
        var delivered = 0L;
        var currentElf: Elf
        while (delivered < presentLimit) {
            elves.add(Elf(current))
            delivered = 0L
            val it = elves.iterator();
            while (it.hasNext()) {
                currentElf = it.next()
                if (currentElf.shouldDeliver(current)) {
                    delivered += currentElf.deliverPresent()
                    if (currentElf.deliveries >= maxDelivery) {
                        it.remove()
                    }
                }
            }
//            println("Current $current delivered: $delivered elves ${elves.size}")
            current++
        }
        return current.toInt() - 1
    }
}