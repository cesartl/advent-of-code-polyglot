package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day21 {

    private val numericKeypadGrid = """789
        |456
        |123
        | 0A
    """.trimMargin().lineSequence().run {
        parseGrid(this) {
            when (it) {
                ' ' -> null
                else -> it
            }
        }
    }

    private val directionalKeypadGrid = """ ^A
        |<v>
    """.trimMargin().lineSequence().run {
        parseGrid(this) {
            when (it) {
                ' ' -> null
                else -> it
            }
        }
    }

    fun solve1(input: Sequence<String>): Long {
        return input.sumOf { code ->
            println("Code $code")
            val best = findBest(code, 2)
            println("Best $best")
            println("cache size ${cache.size}")
            code.dropLast(1).toInt() * best
        }
    }

    fun solve2(input: Sequence<String>): Long {
        return input.sumOf { code ->
            println("Code $code")
            val best = findBest(code, 25)
            println("Best $best")
            code.dropLast(1).toInt() * best
        }
    }

    private fun findBest(code: String, n: Int): Long {
        return "A$code"
            .asSequence()
            .zipWithNext()
            .map { (from, to) ->
                numericalPaths(from, to)
                    .minOf { path ->
                        "A$path".asSequence()
                            .zipWithNext()
                            .sumOf { (from, to) ->
                                findBestDirectional(from, to, n)
                            }
                    }
            }
            .sum()
    }

    private val cache: MutableMap<Pair<Pair<Char, Char>, Int>, Long> = mutableMapOf()

    private fun findBestDirectional(from: Char, to: Char, n: Int): Long {
        return cache.getOrPut((from to to) to n) {
            val allPaths = directionalPaths(from, to)
            if (n == 1) {
                allPaths.minOf { it.length }.toLong()
            } else {
                allPaths.minOf { path ->
                    "A$path".asSequence()
                        .zipWithNext()
                        .sumOf { (from, to) ->
                            findBestDirectional(from, to, n - 1)
                        }
                }
            }
        }
    }

    private val pathCache: MutableMap<Pair<Char, Char>, List<String>> = mutableMapOf()

    private fun numericalPaths(from: Char, to: Char): List<String> {
        return pathCache.computeIfAbsent(from to to) {
            allPath(numericKeypadGrid, from, to)
        }
    }

    private fun directionalPaths(from: Char, to: Char): List<String> {
        return pathCache.computeIfAbsent(from to to) {
            allPath(directionalKeypadGrid, from, to)
        }
    }

    data class PathingState(val position: Position, val path: List<Orientation>, val visited: Set<Position>)

    private fun allPath(grid: Grid<Char>, from: Char, to: Char): List<String> {
        val startPosition = grid.map.entries.singleOrNull { it.value == from }?.key ?: error("$from not found")
        val start = PathingState(
            startPosition,
            emptyList(),
            mutableSetOf(startPosition)
        )
        val queue = ArrayDeque<PathingState>()
        queue.add(start)
        val all = sequence {
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (grid.map[current.position] == to) {
                    yield(current.path.joinToString(separator = "", postfix = "A") { it.toDirection() })
                    continue
                }
                listOf(N, E, S, W).map { orientation ->
                    orientation to orientation.move(current.position)
                }.filterNot { (_, nextPosition) ->
                    current.visited.contains(nextPosition)
                }.filter { (_, nextPosition) ->
                    grid.inScope(nextPosition) && grid.map.containsKey(nextPosition)
                }.forEach { (orientation, nextPosition) ->
                    val nextState =
                        PathingState(nextPosition, current.path + orientation, current.visited + nextPosition)
                    queue.add(nextState)
                }
            }
        }.toList()
        if (all.isEmpty()) {
            error("No path from '$from' to '$to'")
        }
        val best = all.minOf { it.length }
        return all.filter { it.length == best }
    }
}

private fun Orientation.toDirection(): String = when (this) {
    N -> "^"
    E -> ">"
    S -> "v"
    W -> "<"
}
