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

    data class State(val positions: List<Point>, val grid: Grid)

    data class Node(val point: Point, val keys: Set<String>)
    data class MultiNode(val r1: Point, val r2: Point, val r3: Point, val r4: Point, val keys: Set<String>)

    private fun pointPathing(node: Node, grid: Grid): PathingResult<Point> {
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
    private fun getPathingForNode(node: Node, grid: Grid): PathingResult<Point> {
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
                        is Tile.Door -> keys.contains(tile.id.uppercase())
                    }
                }
                .map { it.second }
    }


    private fun generateNodes(node: Node, grid: Grid): Sequence<Node> {
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

    private fun distance(x: Node, y: Node, grid: Grid): Long {
        return getPathingForNode(x, grid).steps[y.point] ?: error("Could not find distance from $x to $y")
    }

    private fun distance(x: MultiNode, y: MultiNode, grid: Grid): Long {
        val d1 = distance(Node(x.r1, x.keys), Node(y.r1, y.keys), grid)
        val d2 = distance(Node(x.r2, x.keys), Node(y.r2, y.keys), grid)
        val d3 = distance(Node(x.r3, x.keys), Node(y.r3, y.keys), grid)
        val d4 = distance(Node(x.r4, x.keys), Node(y.r4, y.keys), grid)
        return d1 + d2 + d3 + d4
    }

    private fun fullDijkstra(grid: Grid, start: Point): Long {
        pathingCache.clear()
        cacheMiss = 0
        cacheHit = 0
        val start = Node(start, setOf())
        val constraint: Constraint<Node> = CustomConstraint { node, steps ->
            node.keys.size < grid.keys.size
        }
        val (steps, previous, lastNode) = Dijkstra.traverse(
                start = start,
                end = null,
                nodeGenerator = { generateNodes(it, grid) },
                distance = { x: Node, y: Node -> distance(x, y, grid) },
                queue = JavaPriorityQueue(),
                constraints = listOf(constraint)
        )
        val (node, count) = steps.filter { it.key.keys.size == grid.keys.size }.minBy { it.value }?.toPair()!!
        println("Cache rate ${cacheHit.toDouble() / (cacheHit + cacheMiss)}")


        return count
    }



    private fun fullDijkstraMultiRobot(grid: Grid, points: List<Point>): Long {
        pathingCache.clear()
        cacheMiss = 0
        cacheHit = 0
        assert(points.size == 4)
        val start = MultiNode(points[0], points[1], points[2], points[3], setOf())
        val constraint: Constraint<MultiNode> = CustomConstraint { node, _ ->
            node.keys.size < grid.keys.size
        }
        val (steps, previous, lastNode) = Dijkstra.traverse(
                start = start,
                end = null,
                nodeGenerator = { (r1, r2, r3, r4, keys) ->
                    sequence {
                        yieldAll(generateNodes(Node(r1, keys), grid).map { (p, newKeys) -> MultiNode(p, r2, r3, r4, newKeys) })
                        yieldAll(generateNodes(Node(r2, keys), grid).map { (p, newKeys) -> MultiNode(r1, p, r3, r4, newKeys) })
                        yieldAll(generateNodes(Node(r3, keys), grid).map { (p, newKeys) -> MultiNode(r1, r2, p, r4, newKeys) })
                        yieldAll(generateNodes(Node(r4, keys), grid).map { (p, newKeys) -> MultiNode(r1, r2, r3, p, newKeys) })
                    }
                },
                distance = { x, y -> distance(x, y, grid) },
                queue = JavaPriorityQueue(),
                constraints = listOf(constraint)
        )
        val (node, count) = steps.filter { it.key.keys.size == grid.keys.size }.minBy { it.value }?.toPair()!!
        return count
    }


    fun solve1(lines: Sequence<String>): Int {
        val state = buildState(lines)
        return fullDijkstra(grid = state.grid, start = state.positions.first()).toInt()
    }

    fun solve2(lines: Sequence<String>): Int {
        val state = buildState(lines)
        return fullDijkstraMultiRobot(grid = state.grid, points = state.positions).toInt()
    }

    private fun buildState(lines: Sequence<String>): State {
        val tiles = mutableMapOf<Point, Tile>()
        val keys = mutableMapOf<String, Point>()
        val positions = mutableListOf<Point>()
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
                                positions.add(Point(x, y))
                                Tile.Empty
                            }
                            else -> {
                                throw IllegalArgumentException("unknown car $c")
                            }
                        }
                tiles[Point(x, y)] = tile
            }
        }
        return State(positions, Grid(tiles, keys))
    }
}
