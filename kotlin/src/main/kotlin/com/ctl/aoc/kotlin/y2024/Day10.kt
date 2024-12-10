package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day10 {
    fun solve1(input: Sequence<String>): Int {
        val grid = parseGrid(input) { it.digitToInt() }
        return grid.map.asSequence()
            .filter { it.value == 0 }
            .map { entry ->
                val start = entry.key
                val result = path(start, grid)
                val c = result.steps.count { grid.map[it.key] == 9 }
                println("${entry.key} -> $c")
                c
            }
            .sum()
    }

    private fun path(start: Position, grid: Grid<Int>): PathingResult<Position> {
        return Dijkstra.traverse(
            start = start,
            nodeGenerator = { pos ->
                val currentHeight = grid.map[pos] ?: 0
                pos.adjacent()
                    .filter { grid.inScope(it) && grid.map[it] == currentHeight + 1 }
            },
            distance = { _, _ -> 1 },
            end = null
        )
    }

    private fun path2(start: Position, grid: Grid<Int>): Sequence<List<Position>> {
        return traversal(
            startNode = listOf(start),
            storage = Stack(),
            nodeGenerator = { poss ->
                val pos = poss.last()
                val currentHeight = grid.map[pos] ?: 0
                pos.adjacent()
                    .filter { grid.inScope(it) && grid.map[it] == currentHeight + 1 }
                    .map { poss + it }
            },
            index = { it.toString() }
        )
    }

    fun solve2(input: Sequence<String>): Int {
        val grid = parseGrid(input) { it.digitToInt() }
        return grid.map.asSequence()
            .filter { it.value == 0 }
            .map { entry ->
                val start = entry.key
                val paths = path2(start, grid)
                paths.count { grid.map[it.last()] == 9 }
            }
            .sum()
    }
}
