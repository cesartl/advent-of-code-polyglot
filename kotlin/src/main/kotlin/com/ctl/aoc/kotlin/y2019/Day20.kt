package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.Dijkstra
import kotlin.math.abs

object Day20 {

    fun solve1(lines: Sequence<String>): Long {
        val maze = parseMaze(lines)

        val result = Dijkstra.traverse(start = maze.start, end = maze.end,
                nodeGenerator = { maze.neighboursWraps(it) },
                distance = { _, _ -> 1L })

        return result.steps[maze.end] ?: -1
    }

    data class Node(val point: Point, val level: Int = 0)

    val maxDepth = 20

    fun solve2(lines: Sequence<String>): Long {
        val maze = parseMaze(lines)
        maze.print()
        val end = Node(maze.end)
        val result = Dijkstra.traverse(start = Node(maze.start), end = end,
                nodeGenerator = { maze.neighboursRecursive(it) },
                distance = { _, _ -> 1L }
                , heuristic = { (point, level) -> level * 100L }
        )

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


    data class Maze(val tiles: Set<Point>, val start: Point, val end: Point,
                    val wraps: Map<Point, Point>, val goDeep: Map<Point, Point>, val comeBack: Map<Point, Point>) {
        fun neighboursWraps(point: Point): Sequence<Point> {
            return sequence {
                yieldAll(point.adjacents().filter { tiles.contains(it) })
                wraps[point]?.let { yield(it) }
            }
        }

        fun neighboursRecursive(node: Node): Sequence<Node> {
            val (point, level) = node
            return sequence {
                yieldAll(point.adjacents().filter { tiles.contains(it) }.map { node.copy(point = it) })
                if (level <= maxDepth) {
                    goDeep[point]?.let { yield(node.copy(point = it, level = level + 1)) }
                } else if (level > 0) {
                    comeBack[point]?.let { yield(node.copy(level = level - 1, point = it)) }
                }
            }
        }

        fun print() {
            val maxX = tiles.maxBy { it.x }?.x!! + 2
            val maxY = tiles.maxBy { it.y }?.y!! + 2
            (0..maxY).forEach { y ->
                (0..maxX).forEach { x ->
                    val p = Point(x, y)
                    if (goDeep.contains(p)) {
                        print('D')
                    } else if (comeBack.contains(p)) {
                        print('B')
                    } else if (tiles.contains(p)) {
                        print('.')
                    } else {
                        print(' ')
                    }
                }
                println()
            }
        }
    }

    fun parseMaze(lines: Sequence<String>): Maze {
        val tiles = mutableSetOf<Point>()
        val labelsCandidates = mutableMapOf<Point, Char>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == '.') {
                    tiles.add(Point(x, y))
                } else if (char.isLetter()) {
                    labelsCandidates[Point(x, y)] = char
                }
            }
        }


        val processedCandidates = mutableSetOf<Point>()
        val labelsMap = mutableMapOf<Set<Char>, MutableList<Point>>()
        labelsCandidates.forEach { (point, char) ->
            if (!processedCandidates.contains(point)) {
                val (otherCandidatePoint, otherChar) = point.adjacents()
                        .mapNotNull { p -> labelsCandidates[p]?.let { p to it } }
                        .firstOrNull() ?: error("Could not find other label for $char @ $point")

                val tiles = sequenceOf(point, otherCandidatePoint)
                        .flatMap { it.adjacents() }
                        .filter { tiles.contains(it) }
                        .toList()
                assert(tiles.size == 1)
                val tile = tiles.first()
                labelsMap.computeIfAbsent(setOf(char, otherChar)) { mutableListOf() }.add(tile)
                processedCandidates.add(otherCandidatePoint)
                processedCandidates.add(point)
            }
        }

        val start = labelsMap[setOf('A')]?.first()!!
        val end = labelsMap[setOf('Z')]?.first()!!
        val midPoint = Point(lines.first().length / 2, lines.count() / 2)

        val goDeep = mutableMapOf<Point, Point>()
        val comeBack = mutableMapOf<Point, Point>()
        val wraps = mutableMapOf<Point, Point>()
        labelsMap.values.filter { it.size == 2 }.forEach {
            if (midPoint.distance(it[0]) < midPoint.distance(it[1])) {
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

    private fun isInner(p: Point, midPoint: Point): Boolean{
        TODO()
    }
}