package com.ctl.aoc.kotlin.y2020

object Day10 {
    fun solve1(input: Sequence<String>): Int {
        val outlets = input.map { it.toInt() }.toSet()
        val chain = find(0, listOf(), outlets, outlets.size)
        println(listOf(0) + chain)

        var (one, three) = count(chain)
        return one * three
    }

    private fun count(chain: List<Int>): Pair<Int, Int> {
        var one = 0
        var three = 0
        (chain).zipWithNext().forEach { (n, next) ->
            if (next - n == 3) {
                three++
            } else {
                one++
            }
        }
        three++
        println(one)
        println(three)
        return Pair(one, three)
    }

    fun find(start: Int, path: List<Int>, remaining: Set<Int>, targetLength: Int): List<Int> {
        if (remaining.isEmpty()) {
            return path
        }
        return listOf(start + 1, start + 2, start + 3)
                .asSequence()
                .filter { remaining.contains(it) }
                .map { newStart -> find(newStart, path + listOf(newStart), remaining - newStart, targetLength) }
                .firstOrNull() { it.size == targetLength }
                ?: listOf()
    }

    fun solve2(input: Sequence<String>): Long {
        val outlets = input.map { it.toInt() }.sortedDescending().toList() + 0
        println(outlets)
        val dp = mutableMapOf<Int, Long>()
        dp[outlets.first()] = 1

        outlets.drop(1).forEach { jolt ->
            println("doing $jolt dp=$dp")
            var ways = 0L
            (jolt + 1..jolt + 3).forEach { adapter ->
                val way = dp[adapter] ?: 0
                println("Checking ways for $adapter, there $way way(s)")
                ways += way
            }
            dp[jolt] = ways
        }
        return dp[0] ?: -1
    }
}