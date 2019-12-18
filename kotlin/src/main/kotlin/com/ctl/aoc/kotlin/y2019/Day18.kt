package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.*
import com.ctl.aoc.util.JavaPriorityQueue
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

    data class Node(val point: Point, val keys: Set<String>)

    fun pointPathing(node: Node, grid: Grid): PathingResult<Point> {
        return Dijkstra.traverse(start = node.point, end = null,
                nodeGenerator = {
                    generatePoints(it, grid, node.keys)
                },
                distance = { _, _ -> 1L }
        )
    }

    private var cacheHit = 0
    private var cacheMiss = 0
    private val pathingCache = mutableMapOf<Node, PathingResult<Point>>()
    fun getPathingForNode(node: Node, grid: Grid): PathingResult<Point> {
        val cached = pathingCache[node]
        return if (cached == null) {
            cacheMiss++
            val result = pointPathing(node, grid)
            pathingCache[node] = result
            result
        } else {
            cacheHit++
            cached
        }
    }

    private fun generatePoints(point: Point, grid: Grid, keys: Set<String>): Sequence<Point> {
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


    fun generateNodes(node: Node, grid: Grid): Sequence<Node> {
        val pathingResult = getPathingForNode(node, grid)
        return grid.keys.keys.minus(node.keys)
                .asSequence()
                .mapNotNull { grid.keys[it] }
                .filter { it != node.point }
                .filter { pathingResult.steps[it] != null }
                .mapNotNull { keyPoint ->
                    pathingResult.findPath(keyPoint)?.let {
                        keyPoint to it
                    }
                }
                .map { (point, path) ->
                    point to path.mapNotNull { grid.tiles[it] }
                            .filterIsInstance(Tile.Key::class.java)
                            .map { it.id }
                            .toSet()
                }
                .map { (keyPoint, visitedKeys) ->
                    Node(keyPoint, node.keys + visitedKeys)
                }
    }

    private fun fullDijkstra(state: State): Long {
        pathingCache.clear()
        cacheMiss = 0
        cacheHit = 0
        val grid = state.grid
        val start = Node(state.position, setOf())
        val constraint: Constraint<Node> = CustomConstraint { node, steps ->
            node.keys.size < grid.keys.size
        }
        val (steps, previous, lastNode) = Dijkstra.traverse(
                start = start,
                end = null,
                nodeGenerator = {
                    val generateNodes = generateNodes(it, grid)
                    generateNodes
                },
                distance = { x: Node, y: Node ->
                    getPathingForNode(x, grid).steps[y.point] ?: error("Could not find distance from $x to $y")
                },
                queue = JavaPriorityQueue(),
                constraints = listOf(constraint)
        )
        val (node, count) = steps.filter { it.key.keys.size == grid.keys.size }.minBy { it.value }?.toPair()!!
        println("Cache rate ${cacheHit.toDouble() / (cacheHit + cacheMiss)}")
        return count
    }


    fun solve1(lines: Sequence<String>): Int {
        val state = buildState(lines)
        return fullDijkstra(state).toInt()
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