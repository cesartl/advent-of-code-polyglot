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

    private val numericKeyPad: Map<Char, Position> = numericKeypadGrid
        .map
        .entries
        .associate { it.value to it.key }

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

    private val directionalKeyPad: Map<Char, Position> = directionalKeypadGrid
        .map
        .entries
        .associate { it.value to it.key }

    fun solve1(input: Sequence<String>): Int {
        return input.sumOf { code ->
            println("Code $code")
            val best = findBest(code)
            println("Best $best")
            code.dropLast(1).toInt() * best
        }
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }

    private fun findBest(code: String): Int {
        return allNumerical(code).flatMap { path ->
            allDirectional(path)
        }.flatMap {
            allDirectional(it)
        }.minOf { it.length }
    }

    private val cache: MutableMap<Pair<Char, Char>, Int> = mutableMapOf()


    private fun allNumerical(code: String): List<String> {
        var paths = listOf("")
        "A$code".asSequence()
            .zipWithNext()
            .forEach { (from, to) ->
                val newPaths = mutableListOf<String>()
                paths.forEach { prefix ->
                    numericalPaths(from, to).forEach { next ->
                        newPaths.add("$prefix$next")
                    }
                }
                paths = newPaths
            }
        return paths
    }

    private fun allDirectional(path: String): List<String> {
        var paths = listOf("")
        "A$path".asSequence()
            .zipWithNext()
            .forEach { (from, to) ->
                val newPaths = mutableListOf<String>()
                paths.forEach { prefix ->
                    directionalPaths(from, to).forEach { next ->
                        newPaths.add("$prefix$next")
                    }
                }
                val best = newPaths.minOf { it.length }
                paths = newPaths.filter { it.length == best }
            }
        return paths
    }

    private fun numericalPaths(from: Char, to: Char): List<String> {
        return allPath(numericKeypadGrid, from, to)
    }

    private fun directionalPaths(from: Char, to: Char): List<String> {
        return allPath(directionalKeypadGrid, from, to)
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
