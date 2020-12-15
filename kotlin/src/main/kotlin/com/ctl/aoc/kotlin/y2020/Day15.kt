package com.ctl.aoc.kotlin.y2020

object Day15 {
    fun solve1(input: String): Int {
        return playGame(input, 2020)
    }

    fun solve2(input: String): Int {
        return playGame(input, 30000000)
    }

    private fun playGame(input: String, max: Int): Int {
        val history: Array<Int?> = Array(max + 1) { null }
        val startingNumbers = input.split(",").map { it.toInt() }
        startingNumbers.dropLast(1).forEachIndexed { turn, n -> history[n] = turn + 1 }
        var turn = startingNumbers.size + 1
        var previousNumber = startingNumbers.last()
        while (turn <= max) {
            val previousTurn = turn - 1
            val next = history[previousNumber]?.let { lastSeenTurn -> previousTurn - lastSeenTurn } ?: 0
            history[previousNumber] = previousTurn
            previousNumber = next
            turn++
        }
        return previousNumber
    }
}