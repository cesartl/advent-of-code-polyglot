package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Position

object Day18 {
    fun solve1(input: Sequence<String>, max: Int = 70, n : Int = 1024): Long {
        val bytes = input.map {
            Position.parse(it)
        }.take(n).toList()

        return findPath(max, bytes)
    }

    fun solve2(input: Sequence<String>, max: Int = 70, n : Int = 1024): String {
        val allBytes = input.map {
            Position.parse(it)
        }.toList()

        val i = generateSequence(n) { it + 1 }
            .map {
                val bytes = allBytes.take(it).toList()
                it to findPath(max, bytes)
            }
            .first { it.second < 0 }
            .first
        val (x, y) = allBytes[i -1]
        return "$x,$y"
    }

    private fun findPath(max: Int, bytes: List<Position>): Long {
        val xRange = 0..max
        val yRange = 0..max

        val bytesSet = bytes.toSet()

        val result = Dijkstra.traverse(
            start = Position(0, 0),
            end = Position(max, max),
            nodeGenerator = {
                it.adjacent()
                    .filter { (x, y) -> x in xRange && y in yRange }
                    .filterNot { p -> bytesSet.contains(p) }
            },
            distance = { _, _ -> 1 },
        )

        return result.steps[Position(max, max)] ?: -1
    }


}
