package com.ctl.aoc.kotlin.y2023

object Day9 {

    private fun generateDiffs(l: List<Long>): Sequence<List<Long>> {
        return generateSequence(l) {
            it.zipWithNext { a, b -> b - a }
        }.takeWhile { list -> list.any { it != 0L } }
    }

    fun extrapolateRight(l: List<Long>): Long {
        return generateDiffs(l)
            .map { it.last() }
            .sum()
    }

    private fun extrapolateLeft(l: List<Long>): Long {
        return generateDiffs(l)
            .map { it.first() }
            .toList()
            .reversed()
            .fold(0) { acc, i -> i - acc }
    }


    fun solve1(input: Sequence<String>): Long {
        return input.map { line -> line.splitNumbers() }
            .map { extrapolateRight(it) }
            .sum()
    }

    fun solve2(input: Sequence<String>): Long {
        return input.map { line -> line.splitNumbers() }
            .map { extrapolateLeft(it) }
            .sum()
    }
}

private fun String.splitNumbers(character: String = " "): List<Long> {
    return this.splitToSequence(character).filterNot { it.isBlank() }.map { it.toLong() }.toList()
}
