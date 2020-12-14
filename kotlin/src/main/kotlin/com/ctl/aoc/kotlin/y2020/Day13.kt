package com.ctl.aoc.kotlin.y2020

import java.math.BigInteger

object Day13 {
    fun solve1(input: Sequence<String>): Long {
        val start = input.first().toLong()
        val times = input.drop(1).first().split(",").filter { !it.contains("x") }.map { it.toLong() }.toList()

        val (bus, time) = times.map { t ->
            val q = start / t
            t to ((q + 1) * t)
        }.minBy { it.second }!!
        println("bus $bus, $time")
        val wait = time - start
        println("Wait: $wait")
        return bus * wait
    }

    data class BusRequirement(val id: Long, val offset: Long)

    fun solve2(input: Sequence<String>): Long {
        val times = input.drop(1).first()
                .split(",")
                .withIndex()
                .filter { !it.value.contains("x") }
                .map { BusRequirement(it.value.toLong(), it.index.toLong()) }
                .toList()
        println(times)

        var timestamp = 0L
        var factor = 1L
        times.forEach { (id, offset) ->
            while ((timestamp + offset) % id != 0L) {
                timestamp += factor
            }
            factor *= id
        }
        return timestamp
    }

    fun solve2CRT(input: Sequence<String>): Long {
        val times = input.drop(1).first()
                .split(",")
                .withIndex()
                .filter { !it.value.contains("x") }.map { BusRequirement(it.value.toLong(), it.index.toLong()) }
                .toList()
        val M = times.fold(BigInteger.ONE) { acc, r -> acc * r.id.toBigInteger() }
        println("M $M")
        val x = times.fold(BigInteger.ZERO) { acc, r ->
            val m = r.id.toBigInteger()
            val a = r.offset.toBigInteger()
            val b = M / m
            val bi = b.modInverse(m)
            (a * b * bi + acc).mod(M)
        }
        return (M - x).mod(M).toLong()

    }
}