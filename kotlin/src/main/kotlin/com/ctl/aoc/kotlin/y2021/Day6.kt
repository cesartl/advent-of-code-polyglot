package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.timedMs

object Day6 {

    fun solve1(input: String): Long {
        return count(input, 80)
    }

    fun solve2(input: String): Long {
        return count(input, 256)
    }

    fun solve2Bis(input: String): Long {
        return countBis(input, 256)
    }

    private fun countBis(input: String, days: Int): Long {
        val count = timedMs {
            generateSequence(input.toPopulation()) { it.next() }
                    .drop(days)
                    .first()
                    .count
        }
        println(count.first)
        return count.second
    }

    fun count(input: String, days: Int): Long {
        val lanterns = input.splitToSequence(",").map { it.toInt() }
        val sum = timedMs { lanterns.map { count(it, days, mutableMapOf()) }.sum() }
        println(sum.first)
        return sum.second
    }


    fun count(lanternTimer: Int, days: Int, cache: MutableMap<Pair<Int, Int>, Long>): Long {
        if (days == 0) {
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

    fun String.toPopulation(): Population {
        val pop = this.split(",").map { it.toInt() }.fold(LongArray(9) { 0L }) { array, n ->
            array[n]++
            array
        }
        return Population(pop)
    }


    data class Population(val generations: LongArray) {

        val count: Long by lazy {
            generations.sum()
        }

        fun next(): Population {
            val birthers = generations.first()
            val newGeneration = (generations.drop(1) + birthers).toLongArray()
            newGeneration[6] += birthers
            return Population(newGeneration)
        }
    }
}