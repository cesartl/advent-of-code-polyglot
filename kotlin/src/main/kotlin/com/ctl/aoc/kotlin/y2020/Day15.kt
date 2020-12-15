package com.ctl.aoc.kotlin.y2020

object Day15 {
    fun solve1(input: String): Long {
        val n = 2020L
        return playGame(input, n)
    }

    fun solve2(input: String): Long {
        val n = 30000000L
        return playGame(input, n)
    }

    private fun playGame(input: String, n: Long): Long {
        val numbers = mutableMapOf<Long, List<Long>>()
        val startingNumbers = input.split(",").map { it.toLong() }
        startingNumbers.forEachIndexed { turn, n -> numbers[n] = listOf(turn.toLong() + 1) }
        var turn = numbers.size + 1L
        var previous = startingNumbers.last()
        while (turn <= n) {
            val previousTurns = numbers[previous] ?: listOf()
            if (previousTurns.size <= 1) {
                previous = 0
            } else {
                previous = previousTurns[0] - previousTurns[1]
            }

            numbers[previous] = listOf(turn) + (numbers[previous] ?: listOf()).take(1)
            turn++
        }
        return previous
    }
}