package com.ctl.aoc.kotlin.y2022

object Day6 {
    fun solve1(input: String): Int {
        return findFirstDistinctN(input, 4)
    }

    private fun findFirstDistinctN(input: String, n: Int): Int {
        return input
            .asSequence()
            .windowed(size = n, step = 1)
            .withIndex()
            .first { (index, list) -> list.toSet().size == n }
            .index + n
    }

    fun solve2(input: String): Int {
        return findFirstDistinctN(input, 14)
    }
}
