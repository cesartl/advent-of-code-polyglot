package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*

sealed interface Element {
    data object Boulder : Element
    data object Wall : Element
}

data class Platform(
    val grid: Map<Position, Element>,
    val xRange: IntRange,
    val yRange: IntRange
) {

    fun load(): Int {
        return grid.map { (p, element) ->
            when (element) {
                Element.Boulder -> 1 + yRange.last - p.y
                Element.Wall -> 0
            }
        }.sum()
    }

    fun print() {
        yRange.forEach { y ->
            xRange.forEach { x ->
                val p = Position(x, y)
                val e = when (grid[p]) {
                    Element.Boulder -> 'O'
                    Element.Wall -> '#'
                    null -> '.'
                }
                print(e)
            }
            println()
        }
    }
}

private fun Platform.tilt(vararg orientations: Orientation): Platform {
    return orientations.fold(this) { acc, orientation -> acc.tilt(orientation) }
}

private fun Platform.tilt(orientation: Orientation): Platform {
    val newGrid = grid.toMutableMap()
    var moved: Boolean
    do {
        moved = false
        yRange.forEach { y ->
            xRange.forEach { x ->
                val p = Position(x, y)
                if (newGrid[p] == Element.Boulder) {
                    val newP = p.move(orientation, newGrid, xRange, yRange)
                    if (newP != null) {
                        moved = true
                        newGrid.remove(p)
                        newGrid[newP] = Element.Boulder
                    }
                }
            }
        }
    } while (moved)
    return this.copy(grid = newGrid)
}

private fun Position.move(
    orientation: Orientation,
    grid: Map<Position, Element>,
    xRange: IntRange,
    yRange: IntRange
): Position? {
    return generateSequence(this) { orientation.move(it) }
        .drop(1)
        .takeWhile { !grid.containsKey(it) }
        .takeWhile { (x, y) -> x in xRange && y in yRange }
        .lastOrNull()
}

private fun parsePlatform(input: Sequence<String>): Platform {
    val grid = mutableMapOf<Position, Element>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val p = Position(x, y)
            val element = when (c) {
                'O' -> Element.Boulder
                '#' -> Element.Wall
                '.' -> null
                else -> error("Unknown $c")
            }
            element?.let { grid[p] = it }
        }
    }
    val yRange = 0..<input.count()
    val xRange = 0..<input.first().length
    return Platform(grid, xRange, yRange)
}


private fun Platform.generateCycles(): Sequence<Platform> {
    return generateSequence(this) { it.tilt(N, W, S, E) }
}

private fun Platform.findCycle(): Pair<Int, Int> {
    val visited = mutableMapOf<Platform, Int>()
    generateCycles()
        .withIndex()
        .forEach { (i, platform) ->
            val found = visited[platform]
            if (found != null) {
                return found to (i - found)
            }
            visited[platform] = i
        }
    error("")
}

object Day14 {
    fun solve1(input: Sequence<String>): Int {
        val platform = parsePlatform(input)
        val tilted = platform.tilt(N)
        return tilted.load()
    }

    fun solve2(input: Sequence<String>): Int {
        val platform = parsePlatform(input)
        val (offset, n) = platform.findCycle()
        println("offset: $offset, n: $n")
        val target: Int = 1000000000
        return platform.generateCycles()
            .drop(offset)
            .drop((target - offset) % n)
            .first()
            .load()
    }
}
