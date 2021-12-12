package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.frequency

object Day10 {
    fun solve1(input: Sequence<String>): Int {
        val raw = input.map { it.toInt() }.sorted().toList()
        val all = listOf(0) + raw + ((raw.maxOrNull() ?: 0) + 3)
        val frequencies = all
                .zipWithNext()
                .map { (current, next) -> next - current }
                .frequency()
        return (frequencies[1] ?: 0) * (frequencies[3] ?: 0)
    }

    fun solve2(input: Sequence<String>): Long {
        val outlets = input.map { it.toInt() }.sortedDescending().toList() + 0
        println(outlets)
        val dp = mutableMapOf<Int, Long>()
        dp[outlets.first()] = 1
        outlets.drop(1).forEach { jolt ->
            var ways = 0L
            (jolt + 1..jolt + 3).forEach { adapter ->
                val way = dp[adapter] ?: 0
                ways += way
            }
            dp[jolt] = ways
        }
        return dp[0] ?: -1
    }
}