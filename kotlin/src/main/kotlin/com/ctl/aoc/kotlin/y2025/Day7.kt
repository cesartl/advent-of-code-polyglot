package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Grid
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.parseGrid

object Day7 {
    fun solve1(input: Sequence<String>): Int {
        val grid = parseGrid(input) {
            when (it) {
                '^' -> true
                else -> null
            }
        }
        val start = input.first().withIndex().first { it.value == 'S' }.index.let { Position(it, 0) }

        var current = setOf(start)
        var splitCount = 0
        do {
            val next = current.asSequence()
                .map { it + Position(0, 1) }
                .filter { grid.inScope(it) }
                .flatMap { p ->
                    if (grid.map.contains(p)) {
                        splitCount++
                        sequenceOf(p + Position(-1, 0), p + Position(1, 0))
                    } else {
                        sequenceOf(p)
                    }
                }
                .toSet()
            if (next.isNotEmpty()) {
                current = next
            }
        } while (next.isNotEmpty())
        return splitCount
    }

    fun solve2(input: Sequence<String>): Long {
        val grid = parseGrid(input) {
            when (it) {
                '^' -> true
                else -> null
            }
        }
        val start = input.first().withIndex().first { it.value == 'S' }.index.let { Position(it, 0) }
        val cache = mutableMapOf<Position, Long>()
        return countTimeline(grid, start, cache).also {
            println("cache size: ${cache.size}")
        }
    }


    private fun countTimeline(grid: Grid<Boolean>, current: Position, cache: MutableMap<Position, Long>): Long {
        cache[current]?.let { return it }
        val next = current + Position(0, 1)
        if (!grid.inScope(next)) {
            return 1L
        }
        val count = if (grid.map.contains(next)) {
            countTimeline(grid, next + Position(-1, 0), cache) + countTimeline(grid, next + Position(1, 0), cache)
        } else {
            countTimeline(grid, next, cache)
        }
        cache[current] = count
        return count
    }
}
