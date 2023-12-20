package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.Position
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class SkyMap(
    val galaxies: List<Position>,
    val emptyRows: Set<Int>,
    val emptyColumns: Set<Int>
)

private fun parseSkyMap(input: Sequence<String>): SkyMap {
    val galaxies: MutableList<Position> = mutableListOf()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') {
                galaxies.add(Position(x, y))
            }
        }
    }
    val yRange = (0..<input.count())
    val xRange = 0..<input.first().length

    val emptyRows = yRange.asSequence().filter { y ->
        xRange.none { x -> galaxies.contains(Position(x, y)) }
    }.toSet()

    val emptyColumns = xRange.asSequence().filter { x ->
        yRange.none { y -> galaxies.contains(Position(x, y)) }
    }.toSet()
    return SkyMap(galaxies, emptyRows, emptyColumns)
}

private fun SkyMap.distance(a: Position, b: Position, expansionFactor: Int): Long {
    val (xa, ya) = a
    val (xb, yb) = b

    val xRange = min(xa, xb)..max(xa, xb)
    val yRange = min(ya, yb)..max(ya, yb)

    val expandedRows = yRange.count { emptyRows.contains(it) }
    val expandedColumns = xRange.count { emptyColumns.contains(it) }

    val deltaX = abs(xa - xb).toLong() + (expansionFactor - 1) * expandedColumns
    val deltaY = abs(ya - yb).toLong() + (expansionFactor - 1) * expandedRows

    return deltaX + deltaY
}

private fun SkyMap.allDistances(expansionFactor: Int): Long {
    val n = galaxies.size
    var sum = 0L
    (0..<n - 1).forEach { i ->
        val a = galaxies[i]
        (i + 1..<n).forEach { j ->
            val b = galaxies[j]
            sum += distance(a, b, expansionFactor)
        }
    }
    return sum
}

object Day11 {
    fun solve1(input: Sequence<String>): Long {
        val skyMap = parseSkyMap(input)
        return skyMap.allDistances(2)
    }

    fun solve2(input: Sequence<String>): Long {
        val skyMap = parseSkyMap(input)
        return skyMap.allDistances(1000000)
    }
}
