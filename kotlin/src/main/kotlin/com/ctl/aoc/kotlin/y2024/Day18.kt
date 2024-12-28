package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Position

object Day18 {
    fun solve1(input: Sequence<String>, max: Int = 70, n: Int = 1024): Long {
        val bytes = input.map {
            Position.parse(it)
        }.take(n).toSet()

        return findPath(max, bytes)
    }

    fun solve2(input: Sequence<String>, max: Int = 70, n: Int = 1024): String {
        val allBytes = input.map {
            Position.parse(it)
        }.toList()

        val byteSet = mutableSetOf<Position>()
        byteSet.addAll(allBytes.take(n))
        var result: Long
        var i = n - 1
        do {
            i++
            byteSet.add(allBytes[i])
            result = findPath(max, byteSet)
        } while (result > 0)

        println("j: $i")
        val (x, y) = allBytes[i]
        return "$x,$y"
    }

    fun solve2BinarySearch(input: Sequence<String>, max: Int = 70, n: Int = 1024): String {
        val allBytes = input.map {
            Position.parse(it)
        }.toList()
        val i = binary(allBytes, max, n, allBytes.size - 1)
        println("i: $i")
        val (x, y) = allBytes[i]
        return "$x,$y"
    }

    private fun binary(allBytes: List<Position>, max: Int, low: Int, high: Int): Int {
        if (low > high) {
            return high
        }
        val mid = (low + high) / 2
        val bytes = allBytes.asSequence().take(mid).toSet()
        val result = findPath(max, bytes)
        return if (result == -1L) {
            binary(allBytes, max, low, mid - 1)
        } else {
            binary(allBytes, max, mid + 1, high)
        }
    }


    private fun findPath(max: Int, bytes: Set<Position>): Long {
        val xRange = 0..max
        val yRange = 0..max

        val end = Position(max, max)
        val result = Dijkstra.traverse(
            start = Position(0, 0),
            end = end,
            nodeGenerator = {
                it.adjacent()
                    .filter { (x, y) -> x in xRange && y in yRange }
                    .filterNot { p -> bytes.contains(p) }
            },
            distance = { _, _ -> 1 },
        )

        return result.steps[end] ?: -1
    }


}
