package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Grid
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.pairs
import com.ctl.aoc.kotlin.utils.parseGrid

object Day8 {
    fun solve1(input: Sequence<String>): Int {
        val grid = parseAntennas(input)
        val antennas = mutableMapOf<Char, MutableList<Position>>()
        grid.map.entries.forEach {
            antennas.computeIfAbsent(it.value){ mutableListOf() }.add(it.key)
        }
        val antinodes = antennas
            .values
            .asSequence()
            .flatMap { positions ->
                positions.pairs().flatMap { (a, b) ->
                    val diff = b - a
                    sequence {
                        yield(b + diff)
                        yield(a - diff)
                    }
                }
            }
            .filter { grid.inScope(it) }
            .toSet()
        return antinodes.size
    }


    fun solve2(input: Sequence<String>): Int {
        val grid = parseAntennas(input)
        val antennas = mutableMapOf<Char, MutableList<Position>>()
        grid.map.entries.forEach {
            antennas.computeIfAbsent(it.value){ mutableListOf() }.add(it.key)
        }
        val antinodes = antennas
            .values
            .asSequence()
            .flatMap { positions ->
                positions.pairs().flatMap { (a, b) ->
                    val diff = b - a
                    sequence {
                        generateSequence(b) { it + diff }
                            .takeWhile { grid.inScope(it) }
                            .let { yieldAll(it) }
                        generateSequence(a) { it - diff }
                            .takeWhile { grid.inScope(it) }
                            .let { yieldAll(it) }
                    }
                }
            }
            .filter { grid.inScope(it) }
            .toSet()
        return antinodes.size
    }

    private fun parseAntennas(input: Sequence<String>): Grid<Char> {
        val grid = parseGrid(input) {
            if (it.isDigit() || it.isLetter()) {
                it
            } else {
                null
            }
        }
        return grid
    }
}
