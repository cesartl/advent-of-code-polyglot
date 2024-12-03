package com.ctl.aoc.kotlin.y2024

object Day3 {

    private val regex = """mul\((\d+),(\d+)\)""".toRegex()


    fun solve1(input: String): Long {
        return regex
            .findAll(input).map {
                val (a, b) = it.destructured
                a.toLong() * b.toLong()
            }
            .sum()
    }

    private data class Acc(val enabled: Boolean, val value: Long)

    private val regex2 = """do\(\)|don't\(\)|mul\((\d+),(\d+)\)""".toRegex()
    fun solve2(input: String): Long {
        return regex2.findAll(input).fold(Acc(true, 0)) { acc, match ->
            if (match.value.contains("do()")) {
                acc.copy(enabled = true)
            } else if (match.value.contains("don't()")) {
                acc.copy(enabled = false)
            } else if (acc.enabled) {
                val (a, b) = match.destructured
                acc.copy(value = acc.value + a.toLong() * b.toLong())
            } else {
                acc
            }
        }.value
    }
}
