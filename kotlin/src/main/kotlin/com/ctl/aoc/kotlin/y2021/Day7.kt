package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Lists.median
import kotlin.math.abs


object Day7 {


    fun solve1(input: String): Int {
        val crabs = input.split(",").map { it.toInt() }
        val m = crabs.median()
        return crabs.map { abs(it - m) }.sum()
    }

    fun solve2(input: String): Int {
        val crabs = input.split(",").map { it.toInt() }
        val min = crabs.min() ?: 0
        val max = crabs.max() ?: 0
        return (min..max).map { it to crabs.fuel2(it) }.minBy { it.second }?.second ?: 0
    }

    private fun List<Int>.fuel2(p: Int): Int {
        return this.map { n ->
            val d = abs(n - p)
            d * (d + 1) / 2
        }.sum()
    }
}