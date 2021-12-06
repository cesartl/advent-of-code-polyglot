package com.ctl.aoc.kotlin.y2021

object Day6 {

    fun solve1(input: String): Long {
        return count(input, 80)
    }

    fun solve2(input: String): Long {
        return count(input, 256)
    }

    fun count(input: String, days: Int): Long {
        val lanterns = input.splitToSequence(",").map { it.toInt() }
        return lanterns.map { count(it, days, mutableMapOf()) }.sum()
    }


    fun count(lanternTimer: Int, days: Int, cache: MutableMap<Pair<Int, Int>, Long>): Long {
        if(days == 0){
            return 1
        }
        val key = lanternTimer to days
        val cached = cache[key]
        if (cached != null) {
            return cached
        }
        return if (lanternTimer > 0) {
            val c = count(lanternTimer - 1, days - 1, cache)
            cache[key] = c
            c
        } else {
            val c = count(6, days - 1, cache) + count(8, days - 1, cache)
            cache[key] = c
            c
        }
    }
}