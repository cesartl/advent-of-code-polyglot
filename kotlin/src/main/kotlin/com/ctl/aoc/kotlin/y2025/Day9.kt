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

        val all = generateEdges(redTiles + redTiles.first())
            .toSet()

        val maxX = redTiles.maxOf { it.x }
        val maxY = redTiles.maxOf { it.y }

        val red = redTiles.toSet()

        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val p = Position(x, y)
                if (red.contains(p)) {
                    print('#')
                } else if (all.contains(p)) {
                    print('X')
                } else {
                    print('.')
                }
            }
            println()
        }

        val xRange = 0..(maxX+1)

        return redTiles.pairs()
            .filter { (a, b) ->
               squareEdges(a, b).all { isContained(it, all, xRange) }
            }
            .maxOf { (a, b) -> a.area(b) }
    }

    private fun squareEdges(a: Position, b: Position): Sequence<Position>{
        val diff = b - a
        val corners = listOf(a, a + Position(diff.x, 0),b, a + Position(0, diff.y), a)
        return generateEdges(corners)
    }

    private fun isContained(p: Position, all: Set<Position>, xRange: IntRange): Boolean {
        if(all.contains(p)){
            return true
        }
        val count = generateSequence(p) { it + Position(1, 0) }
            .takeWhile { xRange.contains(it.x) }
            .count { all.contains(it) }
        return count % 2 == 1
    }

    private fun generateEdges(corners: List<Position>): Sequence<Position>{
        return corners
            .asSequence()
            .zipWithNext()
            .flatMap { (start, end) ->
                val direction = end - start
                generateSequence(start) { it + direction.normalise() }.takeWhile { it != end }
            }
    }
}
