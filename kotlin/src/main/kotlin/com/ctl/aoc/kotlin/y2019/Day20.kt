package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.PathingResult
import com.ctl.aoc.util.JavaPriorityQueue
import kotlin.math.abs

object Day20 {

    fun solve1(lines: Sequence<String>): Long {
        val maze = parseMaze(lines)

        val result = Dijkstra.traverse(start = maze.start, end = maze.end,
                nodeGenerator = { maze.neighboursWraps(it) },
                distance = { _, _ -> 1L })

        return result.steps[maze.end] ?: -1
    }

    sealed class Tile {
        object Empty : Tile()
        object Wall : Tile()
    }

    data class Node(val point: Point, val level: Int = 0)

    val maxDepth = 1000

    fun solve2(lines: Sequence<String>): Long {
        val maze = parseMaze(lines)
        maze.print()
        val end = Node(maze.end)
        println(end)
        val result = Dijkstra.traverse(start = Node(maze.start), end = end,
                nodeGenerator = { maze.neighboursRecursive(it) },
                distance = { _, _ -> 1L },
                queue = JavaPriorityQueue()
        )

        return result.steps[end] ?: -1
    }

    fun foo(maze: Maze, n: Node, pather: Pather): Sequence<Node> = sequence {
        val (point, level) = n
        val pathingResult = pather.pathingResult(point)
        yieldAll(maze.pointsOfInterest.filter { it != point && pathingResult.steps[it] != null }
                .map { Node(it, level) })
        if (level <= maxDepth) {
            maze.goDeep[point]?.let { out ->
                yield(Node(point = out, level = level + 1))
            }
        }
        if (level > 0) {
            maze.comeBack[point]?.let { out ->
                val newNode = Node(point = out, level = level - 1)
                yield(newNode)
            }
        }
    }

    fun distance(x: Node, y: Node, pather: Pather, maze: Maze): Long {
        return if (x.level == y.level) {
            pather.pathingResult(x.point).steps[y.point] ?: error("No path from $x to $y")
        } else {
            1L
        }
    }

    fun solve2Bis(lines: Sequence<String>): Long {
        val maze = parseMaze(lines)
        maze.print()
        val end = Node(maze.end)
        val pather = Pather(maze)
        val result = Dijkstra.traverse(start = Node(maze.start), end = end,
                nodeGenerator = { n ->
                    foo(maze, n, pather)
                },
                distance = { x, y -> distance(x, y, pather, maze) },
                queue = JavaPriorityQueue())
        return result.steps[end] ?: -1
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


    data class Maze(val tiles: Map<Point, Tile>, val start: Point, val end: Point,
                    val wraps: Map<Point, Point>, val goDeep: Map<Point, Point>, val comeBack: Map<Point, Point>) {

        val pointsOfInterest = goDeep.keys + comeBack.keys + setOf(start, end)

        fun neighboursWraps(point: Point): Sequence<Point> {
            return sequence {
                yieldAll(point.adjacents().filter { tiles[it] == Tile.Empty })
//                wraps[point]?.let { yield(it) }
                goDeep[point]?.let { yield(it) }
                comeBack[point]?.let { yield(it) }
            }
        }

        fun neighbours(point: Point): Sequence<Point> {
            return sequence {
                yieldAll(point.adjacents().filter { tiles[it] == Tile.Empty })
            }
        }

        fun neighboursRecursive(node: Node): Sequence<Node> {
            val (point, level) = node
            return sequence {
                if (level <= maxDepth) {
                    goDeep[point]?.let { out ->
                        yield(Node(point = out, level = level + 1))
                    }
                }
                if (level > 0) {
                    comeBack[point]?.let { out ->
                        val newNode = Node(point = out, level = level - 1)
                        yield(newNode)
                    }
                }
                yieldAll(point.adjacents().filter { tiles[it] == Tile.Empty }.map { node.copy(point = it) })
            }
        }

        fun print() {
            val maxX = tiles.keys.maxByOrNull { it.x }?.x!! + 2
            val maxY = tiles.keys.maxByOrNull { it.y }?.y!! + 2
            (0..maxY).forEach { y ->
                (0..maxX).forEach { x ->
                    val p = Point(x, y)
                    when {
                        goDeep.contains(p) -> {
                            print('D')
                        }
                        comeBack.contains(p) -> {
                            print('B')
                        }
                        tiles[p] == Tile.Empty -> {
                            print('.')
                        }
                        tiles[p] == Tile.Wall -> {
                            print('#')
                        }
                        else -> {
                            print(' ')
                        }
                    }
                }
                println()
            }
        }
    }

    fun parseMaze(lines: Sequence<String>): Maze {
        val tiles = mutableMapOf<Point, Tile>()
        val labelsCandidates = mutableMapOf<Point, Char>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                when {
                    char == '.' -> {
                        tiles[(Point(x, y))] = Tile.Empty
                    }
                    char == '#' -> {
                        tiles[(Point(x, y))] = Tile.Wall
                    }
                    char.isLetter() -> {
                        labelsCandidates[Point(x, y)] = char
                    }
                }
            }
        }

        val midPoint = Point(lines.first().length / 2, lines.count() / 2)

        val processedCandidates = mutableSetOf<Point>()
        val labelsMap = mutableMapOf<List<Char>, MutableList<Point>>()
        labelsCandidates.forEach { (point, char) ->
            if (!processedCandidates.contains(point)) {
                val (otherCandidatePoint, otherChar) = point.adjacents()
                        .mapNotNull { p -> labelsCandidates[p]?.let { p to it } }
                        .firstOrNull() ?: error("Could not find other label for $char @ $point")

                val surroundingTiles = sequenceOf(point, otherCandidatePoint)
                        .flatMap { it.adjacents() }
                        .filter { tiles.contains(it) }
                        .toList()
                assert(surroundingTiles.size == 1)
                val tile = surroundingTiles.first()

                val label = if (isInner(point, midPoint, tiles)) listOf(char, otherChar) else listOf(otherChar, char)

                labelsMap.computeIfAbsent(label) { mutableListOf() }.add(tile)
                processedCandidates.add(otherCandidatePoint)
                processedCandidates.add(point)
            }
        }

        val start = labelsMap[listOf('A', 'A')]?.first()!!
        val end = labelsMap[listOf('Z', 'Z')]?.first()!!


        val goDeep = mutableMapOf<Point, Point>()
        val comeBack = mutableMapOf<Point, Point>()
        val wraps = mutableMapOf<Point, Point>()
        assert(labelsMap.all { it.value.size <= 2 })
        labelsMap.values.filter { it.size == 2 }.forEach {
            if (isInner(it[0], midPoint, tiles)) {
                goDeep[it[0]] = it[1]
                comeBack[it[1]] = it[0]
            } else {
                goDeep[it[1]] = it[0]
                comeBack[it[0]] = it[1]
            }
            wraps[it[0]] = it[1]
            wraps[it[1]] = it[0]
        }
        return Maze(tiles, start, end, wraps, goDeep, comeBack)
    }

    private fun isInner(p: Point, midPoint: Point, tiles: Map<Point, Tile>): Boolean {
        val empties = p.adjacents().filter { tiles[it] == null }.toList()
//        assert(empties.size <= 1){ println("empties: ${empties.size}, $p")}
        if(empties.size > 1){
            return false
        }
        val empty = empties.first()
        return empty.distance(midPoint) < p.distance(midPoint)
    }

    data class Pather(val maze: Maze) {
        private val pathingCache = mutableMapOf<Point, PathingResult<Point>>()

        fun pathingResult(point: Point): PathingResult<Point> {
            return pathingCache.computeIfAbsent(point) {
                Dijkstra.traverse(start = point, end = null,
                        nodeGenerator = { maze.neighbours(it) },
                        distance = { _, _ -> 1L },
                        queue = JavaPriorityQueue())
            }
        }
    }
}