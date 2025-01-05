package com.ctl.aoc.kotlin.y2024

object Day11 {
    fun solve1(input: Sequence<String>): Long {
        return blink(input, 25)
    }

    private fun blink(input: Sequence<String>, n: Int): Long {
        return input.map { countStones(it, n) }.sum()
    }

    private val cache = mutableMapOf<Pair<String, Int>, Long>()

    private fun countStones(stone: String, remaining: Int): Long {
        return cache.getOrPut(Pair(stone, remaining)) {
            if (remaining == 0) {
                1
            } else if (stone == "0") {
                countStones("1", remaining - 1)
            } else if (stone.length % 2 == 0) {
                val firstHalf = stone.substring(0, stone.length / 2).toLong().toString()
                val secondHalf = stone.substring(stone.length / 2).toLong().toString()
                countStones(firstHalf, remaining - 1) + countStones(secondHalf, remaining - 1)
            } else {
                val stoneValue = stone.toLong()
                countStones((stoneValue * 2024).toString(), remaining - 1)
            }
        }
    }

    fun solve2(input: Sequence<String>): Long {
        return blink(input, 75)
    }
}
