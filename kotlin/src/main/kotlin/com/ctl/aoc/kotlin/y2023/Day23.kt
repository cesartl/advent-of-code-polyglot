package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*

sealed class Terrain {
    data object Forest : Terrain()
    data class Slope(val orientation: Orientation) : Terrain()
}

private fun Char.parseTerrain(): Terrain? = when (this) {
    '#' -> Terrain.Forest
    '^' -> Terrain.Slope(N)
    '>' -> Terrain.Slope(E)
    'v' -> Terrain.Slope(S)
    '<' -> Terrain.Slope(W)
    else -> null
}

data class HikePath(
    val last: Position,
    val path: LinkedHashSet<Position>
) {
    fun walkTo(position: Position): HikePath {
        val newPath = LinkedHashSet(path)
        newPath.add(position)
        return HikePath(
            position,
            newPath
        )
    }
}

private fun Grid<Terrain>.next(p: Position): Sequence<Position> {
    return when (val terrain = this.map[p]) {
        is Terrain.Slope -> sequenceOf(terrain.orientation.move(p))
        else -> p.adjacent()
            .filter { inScope(it) }
            .filterNot { map[it] == Terrain.Forest }
    }
}

private fun hike(grid: Grid<Terrain>): Sequence<HikePath> {
    val start = Position(grid.xRange.first + 1, 0)
    val end = Position(grid.xRange.last - 1, grid.yRange.last)
    return sequence {
        val queue = ArrayDeque<HikePath>()
        queue.addFirst(HikePath(start, LinkedHashSet()))
        var current: HikePath
        while (queue.isNotEmpty()) {
            current = queue.removeFirst()
            if (current.last == end) {
                yield(current)
            } else {
                grid.next(current.last)
                    .filterNot { current.path.contains(it) }
                    .map { current.walkTo(it) }
                    .forEach { queue.addLast(it) }
            }
        }
    }
}

object Day23 {
    fun solve1(input: Sequence<String>): Int {
        val grid = parseGrid(input) { it.parseTerrain() }
        val longestPath = hike(grid).maxBy { it.path.size }
        return longestPath.path.size
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
