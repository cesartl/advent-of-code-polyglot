package com.ctl.aoc.kotlin.y2025

object Day3 {
    fun solve1(input: Sequence<String>): Long {
        return input
            .map { line -> line.map { it.digitToInt() } }
            .map { bank ->
                findHighestJoltage(bank, 2)
            }
            .sum()
    }

    fun solve2(input: Sequence<String>): Long {
        return input
            .map { line -> line.map { it.digitToInt() } }
            .map { bank ->
                findHighestJoltage(bank, 12)
            }
            .sum()
    }

    fun findHighestJoltage(bank: List<Int>, n: Int): Long {
        val length = bank.size
        val batteries = mutableListOf<Int>()
        val sortedByValue = bank.withIndex().sortedByDescending { it.value }
        var lastFound = -1
        repeat(n){ i ->
            val next = sortedByValue.first { it.index <=  length - n + i && it.index > lastFound }
            lastFound = next.index
            batteries.add(next.value)
        }
        return batteries.joinToString("").toLong()
    }
}
