package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.Stack
import com.ctl.aoc.kotlin.utils.traversal
import java.util.*

object Day9 {

    data class Grid(val heights: Map<Position, Int>) {

        fun lowPoints(): Sequence<Position> {
            return heights.asSequence().filter { (p, h) ->
                p.adjacent().all { heights[it]?.let { hh -> hh > h } ?: true }
            }.map { it.key }
        }

        fun searchBasin(start: Position): Set<Position> {
            val deque = ArrayDeque<Position>()
            val visited: MutableSet<Position> = mutableSetOf()
            deque.push(start)

            while (deque.isNotEmpty()) {
                val current = deque.pop()
                if (!visited.contains(current)) {
                    visited.add(current)
                    current.adjacent()
                            .filterNot { visited.contains(it) }
                            .filter { p ->
                                val h = heights[p]
                                h != null && h < 9
                            }.forEach { deque.push(it) }
                }
            }
            return visited
        }

        fun searchBasin2(start: Position): Sequence<Position> = traversal(start, Stack(), { it.toString() }) { p ->
            p.adjacent().filter { heights[it]?.let { it < 9 } ?: false }
        }

        companion object {
            fun parse(lines: Sequence<String>): Grid {
                val heights = lines.withIndex().flatMap { (y, line) ->
                    line.trim().splitToSequence("").filter { it != "" }.withIndex().map { (x, h) ->
                        Position(x, y) to h.toInt()
                    }
                }.toMap()
                return Grid(heights)
            }
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val grid = Grid.parse(input)
        return grid.lowPoints()
                .map { 1 + (grid.heights[it] ?: error("Missing $it")) }
                .sum()
    }

    fun solve2(input: Sequence<String>): Int {
        val grid = Grid.parse(input)
        val basins = grid.lowPoints()
                .map { grid.searchBasin(it).count() }
                .toList()
        return basins
                .sortedDescending()
                .take(3)
                .fold(1) { x, y -> x * y }
    }

    fun solve2Bis(input: Sequence<String>): Int {
        val grid = Grid.parse(input)
        val basins = grid.lowPoints()
                .map { grid.searchBasin2(it).count() }
                .toList()
        return basins
                .sortedDescending()
                .take(3)
                .fold(1) { x, y -> x * y }
    }
}