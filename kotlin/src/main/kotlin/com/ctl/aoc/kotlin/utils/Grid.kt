package com.ctl.aoc.kotlin.utils

data class Grid<T>(
    val map: Map<Position, T>,
    val xRange: IntRange,
    val yRange: IntRange
) {
    fun inScope(position: Position): Boolean {
        val (x, y) = position
        return x in xRange && y in yRange
    }

    val bottomRight = Position(xRange.last, yRange.last)
}

fun <T> parseGrid(input: Sequence<String>, f: (Char) -> T?): Grid<T> {
    val map = mutableMapOf<Position, T>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val p = Position(x, y)
            f(c)?.let {
                map[p] = it
            }
        }
    }
    val xRange = 0..<input.first().length
    val yRange = 0..<input.count()
    return Grid(map, xRange, yRange)
}

fun parseGrid(input: Sequence<String>): Grid<Char> {
    return parseGrid(input) { it }
}
