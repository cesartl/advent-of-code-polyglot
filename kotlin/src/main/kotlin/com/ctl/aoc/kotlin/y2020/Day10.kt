package com.ctl.aoc.kotlin.y2020

object Day10 {
    fun solve1(input: Sequence<String>): Int {
        val (ones, threes) = count(input.map { it.toInt() })
        println("ones $ones")
        println("threes $threes")
        return ones * threes
    }

    private fun count(chain: Sequence<Int>): Pair<Int, Int> {
        val list = listOf(0) + chain.sorted().toList() + ((chain.max() ?: 0) + 3)
        val frequencies = list
                .zipWithNext()
                .map { (current, next) -> next - current }
                .fold(mapOf<Int, Int>()) { freq, count ->
                    freq + mapOf(count to (freq[count] ?: 0) + 1)
                }
        return (frequencies[1] ?: 0) to (frequencies[3] ?: 0)
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