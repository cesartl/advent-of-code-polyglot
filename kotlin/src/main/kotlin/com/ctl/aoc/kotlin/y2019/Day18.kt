package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.CustomConstraint
import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.PathingResult
import com.ctl.aoc.kotlin.utils.findPath
import java.lang.IllegalArgumentException
import java.util.*

object Day18 {

    sealed class Tile {
        object Wall : Tile()
        object Empty : Tile()
        data class Key(val id: String) : Tile()
        data class Door(val id: String) : Tile()
    }

    data class Point(val x: Int, val y: Int) {
        fun adjacents(): Sequence<Point> = sequence {
            yield(copy(x = x - 1))
            yield(copy(x = x + 1))
            yield(copy(y = y - 1))
            yield(copy(y = y + 1))
        }
    }

    private val keyRegex = """[a-z]""".toRegex()
    private val doorRegex = """[A-Z]""".toRegex()

    data class State(val position: Point, val grid: Map<Point, Tile>, val keys: Set<String> = setOf(), val totalPath: List<Point> = listOf()) {

        private val allKeys = grid.values.filterIsInstance(Tile.Key::class.java).map { it.id }.toSet()

        private val remainingKeys = allKeys.minus(keys)

        fun allKeysFound() = remainingKeys.isEmpty()

        fun allKeysPathed(steps: Map<Point, Long>): Boolean {
            return remainingKeys.minus(steps.keys.mapNotNull { grid[it] }.filterIsInstance(Tile.Key::class.java).map { it.id }).isEmpty()
        }

        private fun generateNodes(point: Point): Sequence<Point> {
            return point.adjacents().map { adj -> grid[adj]?.let { it to adj } }
                    .filterNotNull()
                    .filter {
                        when (val tile = it.first) {
                            is Tile.Wall -> false
                            is Tile.Empty -> true
                            is Tile.Key -> true
                            is Tile.Door -> keys.contains(tile.id.toLowerCase())
                        }
                    }
                    .map { it.second }
        }

        fun doDijkstra(): PathingResult<Point> {
            return Dijkstra.traverse(start = position, end = null,
                    nodeGenerator = {
                        val generateNodes = generateNodes(it)
                        generateNodes
                    },
                    distance = { _, _ -> 1L }
//                    constraints = listOf(CustomConstraint { _, steps -> !allKeysPathed(steps) })
            )
        }

        fun goTo(point: Point, path: List<Point>): State {
            val collectedKeys = path.mapNotNull { grid[it] }.filterIsInstance(Tile.Key::class.java).map { it.id }
            return this.copy(position = point, totalPath = totalPath + path, keys = keys + collectedKeys)
        }
    }


    fun collectAllKeys(state: State): Int {
        val queue: Deque<State> = ArrayDeque()
        queue.add(state)
        val results = mutableListOf<State>()
        var best = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val result = current.doDijkstra()
            val (steps, previous) = result
            val accessible = steps.keys.mapNotNull { p -> state.grid[p]?.let { p to it } }
            val candidates = accessible
                    .filter {
                        when (val tile = it.second) {
                            is Tile.Key -> {
                                !current.keys.contains(tile.id)
                            }
                            else -> false
                        }
                    }
            candidates.forEach { (point, tile) ->
                val path = result.findPath(point).drop(1)
                val newState = current.goTo(point, path)
                if (newState.allKeysFound()) {
//                    results.add(newState)
                    if(newState.totalPath.size < best){
                        best = newState.totalPath.size
                        println("Best result ${newState.totalPath.size}")
                    }
                } else if(newState.totalPath.size < 6576) {
                    queue.addFirst(newState)
                }
            }
        }
        return best
    }

    fun solve1(lines: Sequence<String>): Int {
        val state = buildState(lines)
        return collectAllKeys(state)
    }

    private fun buildState(lines: Sequence<String>): State {
        val grid = mutableMapOf<Point, Tile>()
        lateinit var position: Point
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val tile: Tile =
                        when {
                            c == '#' -> Tile.Wall
                            c == '.' -> Tile.Empty
                            keyRegex.matches(c.toString()) -> Tile.Key(c.toString())
                            doorRegex.matches(c.toString()) -> Tile.Door(c.toString())
                            c == '@' -> {
                                position = Point(x, y)
                                Tile.Empty
                            }
                            else -> {
                                throw IllegalArgumentException("unknown car $c")
                            }
                        }
                grid[Point(x, y)] = tile
            }
        }
        return State(position, grid)
    }
}