package com.ctl.aoc.kotlin.y2021

object Day1 {
    fun solve1(input: Sequence<String>): Int {
        return input.map { it.toInt() }
                .zipWithNext()
                .map { it.second - it.first }
                .filter { it > 0 }
                .count()
    }

    fun solve2(input: Sequence<String>): Int {
        return input.map { it.toInt() }
                .windowed(3, 1)
                .zipWithNext()
                .map { it.second.sum() - it.first.sum() }
                .filter { it > 0 }
                .count()
    }
}