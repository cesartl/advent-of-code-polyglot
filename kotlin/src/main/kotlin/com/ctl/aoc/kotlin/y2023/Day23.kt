package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*
import kotlin.collections.set

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

private fun Grid<Terrain>.nextNoSlope(p: Position): Sequence<Position> {
    return p.adjacent()
        .filter { inScope(it) }
        .filterNot { map[it] == Terrain.Forest }
}

private fun hike(grid: Grid<Terrain>, next: (Position) -> Sequence<Position>): Sequence<HikePath> {
    val start = Position(grid.xRange.first + 1, 0)
    val end = Position(grid.xRange.last - 1, grid.yRange.last)
    return sequence {
        val queue = ArrayDeque<HikePath>()
        queue.addFirst(HikePath(start, LinkedHashSet(setOf(start))))
        var current: HikePath
        while (queue.isNotEmpty()) {
            current = queue.removeFirst()
            if (current.last == end) {
                yield(current)
            } else {
                next(current.last)
                    .filterNot { current.path.contains(it) }
                    .map { current.walkTo(it) }
                    .forEach { queue.addLast(it) }
            }
        }
    }
}

data class IntersectionState(
    val position: Position,
    val lastIntersection: Position,
    val distanceCount: Int
) {
    fun resetIntersection(): IntersectionState {
        return copy(lastIntersection = position, distanceCount = 0)
    }

    fun moveTo(p: Position): IntersectionState {
        return this.copy(position = p, distanceCount = distanceCount + 1)
    }
}

private fun isIntersection(p: Position, grid: Grid<Terrain>): Boolean {
    return p.adjacent()
        .filter { grid.inScope(it) }
        .filterNot { grid.map[it] == Terrain.Forest }
        .count() >= 3
}

private fun findIntersections(grid: Grid<Terrain>): Map<Position, Map<Position, Int>> {
    val start = Position(grid.xRange.first + 1, 0)
    val end = Position(grid.xRange.last - 1, grid.yRange.last)
    val distances: MutableMap<Position, MutableMap<Position, Int>> = mutableMapOf()
    val queue = ArrayDeque<IntersectionState>()
    queue.addFirst(IntersectionState(start, start, 0))
    var current: IntersectionState
    val visited = mutableSetOf<Pair<Position, Position>>()
    while (queue.isNotEmpty()) {
        current = queue.removeFirst()
        visited.add(current.lastIntersection to current.position)
        if (isIntersection(current.position, grid) || current.position == end) {
            distances.computeIfAbsent(current.lastIntersection) { mutableMapOf() }[current.position] =
                current.distanceCount
            current = current.resetIntersection()
        }
        grid.nextNoSlope(current.position)
            .map { current.moveTo(it) }
            .filterNot { visited.contains(it.lastIntersection to it.position) }
            .forEach { queue.addLast(it) }
    }
    return distances
}

object Day23 {
    fun solve1(input: Sequence<String>): Int {
        val grid = parseGrid(input) { it.parseTerrain() }
        val longestPath = hike(grid) { grid.next(it) }.maxBy { it.path.size }
        return longestPath.path.size
    }

    fun solve2(input: Sequence<String>): Int {
        val grid = parseGrid(input) { it.parseTerrain() }
        val distances = findIntersections(grid)
        val paths = hike(grid) {
            (distances[it] ?: mapOf()).keys.asSequence()
        }.toList()
        val lengths = paths.map { hike ->
            hike.path.zipWithNext().sumOf { (from, to) -> distances[from]?.get(to) ?: error("") }
        }
        return lengths.max()
    }
}
