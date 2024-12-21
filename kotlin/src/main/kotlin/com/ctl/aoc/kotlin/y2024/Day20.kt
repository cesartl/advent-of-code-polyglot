package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day20 {
    fun solve1(input: Sequence<String>): Int {
        val cheats = buildCheats(input, 2)
        val grouped = cheats.entries.groupBy { it.value }
        return cheats.entries.count { it.value >= 100 }
    }

    fun solve2(input: Sequence<String>): Int {
        val cheats = buildCheats(input, 20)
//        val grouped = cheats.entries.filter { it.value >= 50 }.groupBy { it.value }
        return cheats.entries.count { it.value >= 100 }
    }

    private fun checkCheat(
        distances: MutableMap<Position, Long>,
        p: Position,
        grid: Grid<Char>,
        enter: Position,
        exitState: State,
        cheats: MutableMap<Pair<Position, Position>, Long>
    ) {
        val currentBest = distances[p] ?: return
        val exit = exitState.position
        if (grid.inScope(exit) && grid.map[exit] != '#') {
            if (!distances.containsKey(exit)) {
                error("Not implemented")
            }
            val distance = distances[exit] ?: error("No distance for $exit")
            val saved = currentBest - distance - exitState.distance
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
        path.forEach { p ->
            p.adjacent()
                .filter { grid.inScope(p) }
                .filter { grid.map[it] == '#' }
                .forEach { entry ->
                    //                    println("Checking from $entry")
                    findExitNodes(grid, entry, n).forEach { exit ->
                        checkCheat(
                            distances = distances,
                            p = p,
                            grid = grid,
                            enter = entry,
                            exitState = exit,
                            cheats = cheats
                        )
                    }
                }
        }
        return cheats
    }

    data class State(val position: Position, val remaining: Int, val distance: Int)

    private fun findExitNodes(grid: Grid<Char>, entry: Position, cheatTime: Int): Sequence<State> =
        sequence {
            val visited = mutableSetOf<Position>()
            val queue = ArrayDeque<State>()
            queue.add(State(entry, cheatTime - 1, 1))
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (visited.contains(current.position)) {
                    continue
                }
//                println("Distance: ${current}. queue: ${queue.size}. Visited: ${visited.size}")
                current.position
                    .adjacent()
                    .filter { grid.inScope(it) }
                    .forEach { next ->
                        val nextState = State(next, current.remaining - 1, current.distance + 1)
                        when (grid.map[next]) {
                            '.' -> {
                                yield(nextState)
                                if (nextState.remaining > 0) {
                                    queue.add(nextState)
                                }
                            }
                            'E' -> yield(nextState)
                            '#' -> {
                                if (nextState.remaining > 0) {
                                    queue.add(nextState)
                                }
                            }
                        }
//                        if (nextState.remaining > 0) {
//                            queue.add(nextState)
//                        }
                    }

                visited.add(current.position)
            }
        }
}
