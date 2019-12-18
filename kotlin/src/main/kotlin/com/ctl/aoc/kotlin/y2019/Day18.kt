package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.PathingResult
import com.ctl.aoc.kotlin.utils.findPath
import java.util.*
import kotlin.math.abs

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

        fun distance(other: Point): Int = abs(x - other.x) + abs(y - other.y)
    }

    private val keyRegex = """[a-z]""".toRegex()
    private val doorRegex = """[A-Z]""".toRegex()


    data class Grid(val tiles: Map<Point, Tile>, val keys: Map<String, Point>)

    data class State(val position: Point, val grid: Grid, val keys: Set<String> = setOf(), val totalPath: List<Point> = listOf(), val stepsCount: Int = 0) {

        private val allKeys = grid.tiles.values.filterIsInstance(Tile.Key::class.java).map { it.id }.toSet()

        val remainingKeys = allKeys.minus(keys)

        fun allKeysFound() = remainingKeys.isEmpty()


        private fun generateNodes(point: Point): Sequence<Point> {
            return point.adjacents().map { adj -> grid.tiles[adj]?.let { it to adj } }
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

        fun pathToKey(key: String): Pair<Point, PathingResult<Point>> {
            val keyLocation = grid.keys[key] ?: error("Could not find key $key")
            return keyLocation to Dijkstra.traverse(start = position, end = keyLocation,
                    nodeGenerator = { generateNodes(it) }, distance = { _, _ -> 1L },
                    heuristic = { it.distance(keyLocation).toLong() })
        }

        fun doDijkstra(): PathingResult<Point> {
            return Dijkstra.traverse(start = position, end = null,
                    nodeGenerator = {
                        val generateNodes = generateNodes(it)
                        generateNodes
                    },
                    distance = { _, _ -> 1L }
            )
        }

        fun goTo(point: Point, path: List<Point>): State {
            val collectedKeys = path.mapNotNull { grid.tiles[it] }.filterIsInstance(Tile.Key::class.java).map { it.id }
            return this.copy(position = point, stepsCount = stepsCount + path.size, keys = keys + collectedKeys)
        }
    }


    private fun collectAllKeys(state: State): Int {
        val queue: Deque<State> = ArrayDeque()
        queue.add(state)
        val results = mutableListOf<State>()
        var best = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val current = queue.removeLast()

            val pre = current.remainingKeys
                    .map { it to (state.grid.keys[it] ?: error("")).distance(current.position) }
                    .sortedBy { it.second }
                    .take(10)


            val candidates = pre
                    .asSequence()
                    .map { it.first }
                    .map { it to current.pathToKey(it) }
                    .filter { (_, result) -> result.second.steps[result.first] != null }
                    .sortedBy { (_, result) -> -(result.second.steps[result.first] ?: 0) }
                    .toList()

            candidates.forEach { (key, pair) ->
                val (keyLocation, result) = pair
                val path = result.findPath(keyLocation).drop(1)
                val newState = current.goTo(keyLocation, path)
                if (newState.allKeysFound()) {
//                    results.add(newState)
                    if (newState.stepsCount < best) {
                        best = newState.stepsCount
                        println("Best result $best")
                    }
                } else if (newState.stepsCount < 4682) {
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
        val tiles = mutableMapOf<Point, Tile>()
        val keys = mutableMapOf<String, Point>()
        lateinit var position: Point
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val tile: Tile =
                        when {
                            c == '#' -> Tile.Wall
                            c == '.' -> Tile.Empty
                            keyRegex.matches(c.toString()) -> {
                                keys[c.toString()] = Point(x, y)
                                Tile.Key(c.toString())
                            }
                            doorRegex.matches(c.toString()) -> Tile.Door(c.toString())
                            c == '@' -> {
                                position = Point(x, y)
                                Tile.Empty
                            }
                            else -> {
                                throw IllegalArgumentException("unknown car $c")
                            }
                        }
                tiles[Point(x, y)] = tile
            }
        }
        return State(position, Grid(tiles, keys))
    }
}