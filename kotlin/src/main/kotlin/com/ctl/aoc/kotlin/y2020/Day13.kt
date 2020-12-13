package com.ctl.aoc.kotlin.y2020

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

    fun solve2(input: Sequence<String>): Long? {
        val times = input.drop(1).first()
                .split(",")
                .withIndex()
                .filter { !it.value.contains("x") }.map { BusRequirement(it.value.toLong(), it.index.toLong()) }
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
}