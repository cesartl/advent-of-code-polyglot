package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.pairs

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


        val xs: Map<Int, Int> =
            redTiles.asSequence().map { it.x }.sorted().withIndex().associate { it.value to it.index }
        val ys = redTiles.asSequence().map { it.y }.sorted().withIndex().associate { it.value to it.index }
        val compress: (Position) -> Position = { (x, y) -> Position(xs[x]!!, ys[y]!!) }

        val reverseXs = xs.asSequence().associate { it.value to it.key }
        val reverseYs = ys.asSequence().associate { it.value to it.key }
        val deCompress: (Position) -> Position = { (x, y) -> Position(reverseXs[x]!!, reverseYs[y]!!) }

        val compressed = redTiles.map { compress(it) }

        val all = generateEdges(compressed + compressed.first()).toSet()

        val maxX = compressed.maxOf { it.x }
//        val maxY = compressed.maxOf { it.y }
//        (0..maxY).forEach { y ->
//            (0..maxX).forEach { x ->
//                val p = Position(x, y)
//                if (all.contains(p)) {
//                    print('X')
//                } else {
//                    print('.')
//                }
//            }
//            println()
//        }

        val xRange = 0..(maxX + 1)

        val cache = mutableMapOf<Position, Boolean>()

        val sorted = compressed
            .pairs()
            .sortedByDescending { (a, b) -> deCompress(a).area(deCompress(b)) }

        return sorted.first { (a, b) ->
            squarePerimeters(a, b).all { isContained(it, all, xRange, cache) }
        }.let { (a, b) -> deCompress(a) to deCompress(b) }
            .let { (a, b) -> a.area(b) }
    }

    fun solve2Bis(input: Sequence<String>): Long {
        val redTiles = input.map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Position(x, y)
        }.toList()

        val edges: List<Pair<Position, Position>> = (redTiles + redTiles.first()).zipWithNext()
        val all = generateEdges(redTiles + redTiles.first()).toSet()

        val maxX = redTiles.maxOf { it.x }
        val xRange = 0..(maxX + 1)
        redTiles.pairs()
            .filter { (a, b) ->
                val corners = squareCorners(a, b)
                val squareEdge = (corners + corners.first()).zipWithNext()
                squareEdge.none { edge -> intersect(edge, edges) }
            }

        TODO()
    }

    fun intersect(
        squareEdge: Pair<Position, Position>,
        edges: List<Pair<Position, Position>>
    ): Boolean {
        TODO("Not yet implemented")
    }

    private fun squarePerimeters(a: Position, b: Position): Sequence<Position> {
        val corners = squareCorners(b, a)
        return generateEdges(corners)
    }

    private fun squareCorners(
        b: Position,
        a: Position
    ): List<Position> {
        val diff = b - a
        val corners = listOf(a, a + Position(diff.x, 0), b, a + Position(0, diff.y), a)
        return corners
    }

    private fun isContained(
        p: Position,
        all: Set<Position>,
        xRange: IntRange,
        cache: MutableMap<Position, Boolean>
    ): Boolean {
        cache[p]?.let { return it }
        val result = if (all.contains(p)) {
            true
        } else {
            val count = generateSequence(p) { it + Position(1, 0) }
                .takeWhile { xRange.contains(it.x) }
                .count { all.contains(it) }
            count % 2 == 1
        }
        cache[p] = result
        return result
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
