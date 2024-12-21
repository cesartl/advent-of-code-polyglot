package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day20 {
    fun solve1(input: Sequence<String>): Int {
        val distances = mutableMapOf<Position, Long>()
        val grid = parseGrid(input) {
            when (it) {
                '.' -> null
                else -> it
            }
        }
        val start = grid.map.entries.single { it.value == 'S' }.key
        val end = grid.map.entries.single { it.value == 'E' }.key
        val result = pathingResult(start, grid)

        val best = result.steps[end]!!

        result.steps.forEach { (position, distance) ->
            distances[position] = best - distance
        }

        val path = result.findPath(end)

        val cheats = mutableMapOf<Pair<Position, Position>, Long>()
        path.drop(0).forEach { p ->
            p.adjacent()
                .filter { grid.inScope(p) }
                .filter { grid.map[it] == '#' }
                .forEach { cheat1 ->
                    cheat1.adjacent()
                        .filter { grid.inScope(it) && grid.map[it] != '#' }
                        .filter { it != p }
                        .forEach { candidate ->
                            checkCheat(
                                distances = distances,
                                p = p,
                                grid = grid,
                                enter = cheat1,
                                exit = candidate,
                                cheats = cheats,
                                end = end
                            )
                        }
                }
        }
        val grouped = cheats.entries.groupBy { it.value }
        return cheats.entries.count { it.value >= 100 }
    }

    private fun checkCheat(
        distances: MutableMap<Position, Long>,
        p: Position,
        grid: Grid<Char>,
        enter: Position,
        exit: Position,
        cheats: MutableMap<Pair<Position, Position>, Long>,
        end: Position
    ) {
        val currentBest = distances[p] ?: return
        if (grid.inScope(exit) && grid.map[exit] != '#') {
            if (!distances.containsKey(exit)) {
                println("Running pathing for $exit")
                error("Not implemented")
//                val result = pathingResult(exit, grid)
//                val localBest = result.steps[end]!!
//                result.steps.forEach { (position, distance) ->
//                    distances[position] = localBest - distance
//                }
            }
            val distance = distances[exit] ?: error("No distance for $exit")
            val saved = currentBest - distance - p.distance(exit)
            if (saved > 1) {
                cheats[enter to exit] = saved
            }
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

    fun solve2(input: Sequence<String>): Int {
        val distances = mutableMapOf<Position, Long>()
        val grid = parseGrid(input) {
            when (it) {
                '.' -> null
                else -> it
            }
        }
        val start = grid.map.entries.single { it.value == 'S' }.key
        val end = grid.map.entries.single { it.value == 'E' }.key
        val result = pathingResult(start, grid)

        val best = result.steps[end]!!

        result.steps.forEach { (position, distance) ->
            distances[position] = best - distance
        }

        val path = result.findPath(end)

        val cheats = mutableMapOf<Pair<Position, Position>, Long>()
        path.drop(0).forEach { p ->
        }

        TODO()
    }
}
