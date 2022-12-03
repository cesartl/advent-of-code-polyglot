package com.ctl.aoc.kotlin.y2022

object Day1 {
    fun solve1(input: Sequence<String>): Int {
        return parseCaloriesSequence(input).max()
    }

    fun solve2(input: Sequence<String>): Int {
        return parseCaloriesSequence(input)
            .sortedDescending()
            .take(3)
            .sum()
    }

    private fun parseCalories(input: Sequence<String>): List<Int> {
        return input.fold((listOf<Int>() to 0)) { (acc, current), s ->
            if (s.isBlank()) {
                (acc + listOf(current)) to 0
            } else {
                acc to (current + s.toInt())
            }
        }.let { (acc, current) -> acc + listOf(current) }
    }

    private fun parseCaloriesSequence(input: Sequence<String>): Sequence<Int> = sequence {
        var current = 0
        input.forEach {
            if (it.isBlank()) {
                yield(current)
                current = 0
            } else {
                current += it.toInt()
            }
        }
    }
}
