package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day20 {
    fun solve1(input: Sequence<String>): Int {
        val cheats = buildCheats(input, 2)
        return cheats.entries.count { it.value >= 100 }
    }

    fun solve2(input: Sequence<String>): Int {
        val cheats = buildCheats(input, 20)
        return cheats.entries.count { it.value >= 100 }
    }

    private fun buildCheats(
        input: Sequence<String>,
        n: Int
    ): MutableMap<Pair<Position, Position>, Long> {
        val distances = mutableMapOf<Position, Long>()
        val grid = parseGrid(input)
        val start = grid.map.entries.single { it.value == 'S' }.key
        val end = grid.map.entries.single { it.value == 'E' }.key
        val result = pathingResult(start, grid)
        val best = result.steps[end]!!
        result.steps.forEach { (position, distance) ->
            distances[position] = best - distance
        }

        val path = result.findPath(end)
        val cheats = mutableMapOf<Pair<Position, Position>, Long>()
        path.forEach { cheatStart ->
            (2..n).forEach { cheatDuration ->
                cheatStart.atDistance(cheatDuration)
                    .filter { grid.inScope(it) }
                    .filter { grid.map[it] != '#' }
                    .forEach { cheatEnd ->
                        checkCheat(
                            distances = distances,
                            start = cheatStart,
                            end = cheatEnd,
                            cheats = cheats
                        )
                    }
            }
        }
        return cheats
    }

    private fun checkCheat(
        distances: MutableMap<Position, Long>,
        start: Position,
        end: Position,
        cheats: MutableMap<Pair<Position, Position>, Long>
    ) {
        val currentBest = distances[start] ?: return
        val distance = distances[end] ?: error("No distance for $end")
        val saved = currentBest - distance - start.distance(end)
        if (saved > 1) {
            cheats[start to end] = saved
        }
    }

    private fun pathingResult(
        start: Position,
        grid: Grid<Char>
    ) = Dijkstra.traverse(
        start = start,
        end = null,
        nodeGenerator = {
            it.adjacent()
                .filter { p -> grid.inScope(p) }
                .filter { p -> grid.map[p] != '#' }
        },
        distance = { _, _ -> 1 },
    )
}
