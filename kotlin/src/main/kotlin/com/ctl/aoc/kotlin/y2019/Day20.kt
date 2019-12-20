package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.Dijkstra
import kotlin.math.abs

object Day20 {

    fun solve1(lines: Sequence<String>): Long {
        val maze = parseMaze(lines)

        val result = Dijkstra.traverse(start = maze.start, end = maze.end,
                nodeGenerator = { maze.neighbours(it) },
                distance = { _, _ -> 1L })

        return result.steps[maze.end] ?: -1
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
                    val wraps: Map<Point, Point>) {
        fun neighbours(point: Point): Sequence<Point> {
            return sequence {
                yieldAll(point.adjacents().filter { tiles.contains(it) })
                wraps[point]?.let { yield(it) }
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
                        .firstOrNull()?: error("Could not find other label for $char @ $point")

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

        val wraps = mutableMapOf<Point, Point>()
        labelsMap.values.filter { it.size == 2 }.forEach {
            wraps[it[0]] = it[1]
            wraps[it[1]] = it[0]
        }
        return Maze(tiles, start, end, wraps)
    }
}