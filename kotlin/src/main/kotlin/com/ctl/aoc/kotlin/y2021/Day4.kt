package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position

object Day4 {

    data class Grid(val numbers: Map<Position, Long>, val marked: Set<Position> = setOf()) {

        private val reversedNumber: Map<Long, Position> = numbers.map { it.value to it.key }.toMap()

        private val size: Int = numbers.keys.maxBy { it.x }?.x ?: 0 - 1

        val unmarkedSum: Long by lazy {
            numbers.filterNot { marked.contains(it.key) }
                    .map { it.value }.sum()
        }

        fun play(number: Long): Grid {
            return reversedNumber[number]?.let {
                mark(it)
            } ?: this
        }

        private fun mark(p: Position): Grid {
            return this.copy(marked = marked + p)
        }

        fun winning(): Boolean = checkRows() || checkColumn()


        private fun checkRows(): Boolean {
            return numbers.entries
                    .groupBy { it.key.y }
                    .any { row -> row.value.map { it.key }.all { marked.contains(it) } }
        }

        private fun checkColumn(): Boolean {
            return numbers.entries
                    .groupBy { it.key.x }
                    .any { row -> row.value.map { it.key }.all { marked.contains(it) } }
        }

        companion object {
            fun parse(lines: Sequence<String>): Grid {
                val numbers = lines.map {
                    it.trim().splitToSequence(" ")
                            .filter { it != "" }
                            .map { it.toLong() }.withIndex()
                }.mapIndexed { y, row ->
                    row.map { (x, n) -> Position(x, y) to n }
                }.flatten().toMap()
                return Grid(numbers)
            }
        }
    }

    data class Grids(val grids: List<Grid>) {
        fun play(number: Long): Grids {
            return this.copy(grids = grids.map { it.play(number) })
        }

        fun winningGrid(): Grid = grids.find { it.winning() }!!

        fun loosingGrid(): Grid = grids.find { !it.winning() }!!
    }

    data class State(val grids: Grids, val leftToDraw: List<Long>) {
        fun isWinning(): Boolean {
            return grids.grids.any { it.winning() }
        }

        fun allWinning(): Boolean {
            return grids.grids.all { it.winning() }
        }

        fun play(): Pair<State, Long> {
            val number = leftToDraw.first()
            val newState = this.copy(grids = grids.play(number), leftToDraw = leftToDraw.drop(1))
            return newState to number
        }

        companion object {
            fun parse(input: String): State {
                val parts = input.split("\n\n".toRegex())
                val leftToDraw = parts[0].split(",").map { it.toLong() }
                val grids = parts.drop(1)
                        .map { it.splitToSequence("\n") }
                        .map { Grid.parse(it) }
                        .toList()
                return State(Grids(grids), leftToDraw)
            }
        }
    }

    fun solve1(input: String): Long {
        var state = State.parse(input)
        var number = 0L
        while (!state.isWinning()) {
            val play = state.play()
            number = play.second
            state = play.first
        }
        val winning = state.grids.winningGrid()
        val unmarkedSum = winning.unmarkedSum
        return number * unmarkedSum
    }


    fun solve2(input: String): Long {
        var state = State.parse(input)
        var previous = state
        var number = 0L
        while (!state.allWinning()) {
            previous = state
            val play = state.play()
            number = play.second
            state = play.first
        }
        val last = previous.grids.loosingGrid().play(number)
        val unmarkedSum = last.unmarkedSum
        return number * unmarkedSum
    }
}