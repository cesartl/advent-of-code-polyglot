package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.pairs
import kotlin.collections.zipWithNext

object Day9 {
    fun solve1(input: Sequence<String>): Long {
        return input.map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Position(x, y)
        }
            .toList()
            .pairs()
            .maxOf { (a, b) -> a.area(b) }
    }

    fun solve2(input: Sequence<String>): Long {
        val redTiles = input.map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Position(x, y)
        }.toList()


        val xs: Map<Int, Int> = redTiles.asSequence().map { it.x }.sorted().withIndex().associate { it.value to it.index }
        val ys = redTiles.asSequence().map { it.y }.sorted().withIndex().associate { it.value to it.index }


        val reverseXs = xs.asSequence().associate { it.value to it.key }
        val reverseYs = ys.asSequence().associate { it.value to it.key }

        val compressed = redTiles.map { (x, y) -> Position(xs[x]!!, ys[y]!!) }

        val all = generateEdges(compressed + compressed.first())
            .toSet()

        val maxX = compressed.maxOf { it.x }
        val maxY = compressed.maxOf { it.y }


        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val p = Position(x, y)
                 if (all.contains(p)) {
                    print('X')
                } else {
                    print('.')
                }
            }
            println()
        }

        val xRange = 0..(maxX + 1)

        val reverse: (Position) -> Position = { (x, y) -> Position(reverseXs[x]!!, reverseYs[y]!!) }

        return compressed.pairs()
            .filter { (a, b) ->
                squareEdges(a, b).all { isContained(it, all, xRange) }
            }
            .map { (a, b) -> reverse(a) to reverse(b) }
            .maxOf { (a, b) -> a.area(b) }
    }

    private fun squareEdges(a: Position, b: Position): Sequence<Position> {
        val diff = b - a
        val corners = listOf(a, a + Position(diff.x, 0), b, a + Position(0, diff.y), a)
        return generateEdges(corners)
    }

    private fun isContained(p: Position, all: Set<Position>, xRange: IntRange): Boolean {
        if (all.contains(p)) {
            return true
        }
        val count = generateSequence(p) { it + Position(1, 0) }
            .takeWhile { xRange.contains(it.x) }
            .count { all.contains(it) }
        return count % 2 == 1
    }

    private fun generateEdges(corners: List<Position>): Sequence<Position> {
        return corners
            .asSequence()
            .zipWithNext()
            .flatMap { (start, end) ->
                val direction = end - start
                generateSequence(start) { it + direction.normalise() }.takeWhile { it != end }
            }
    }
}
