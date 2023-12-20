package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

/**
 * The pipes are arranged in a two-dimensional grid of tiles:
 *
 *     | is a vertical pipe connecting north and south.
 *     - is a horizontal pipe connecting east and west.
 *     L is a 90-degree bend connecting north and east.
 *     J is a 90-degree bend connecting north and west.
 *     7 is a 90-degree bend connecting south and west.
 *     F is a 90-degree bend connecting south and east.
 *     . is ground; there is no pipe in this tile.
 */
private fun Char.connect(): Pair<Orientation, Orientation>? = when (this) {
    '|' -> N to S
    '-' -> E to W
    'L' -> N to E
    'J' -> N to W
    '7' -> S to W
    'F' -> S to E
    else -> null
}

data class PipeMap(
    val graph: Graph<Position>,
    val start: Position,
    val yRange: IntRange,
    val xRange: IntRange,
    val charMap: Map<Position, Char>
)

private fun buildPipePap(input: Sequence<String>): PipeMap {
    var start = Position(0, 0)
    val graph = Graph<Position>()
    val connections = mutableSetOf<Pair<Position, Position>>()
    val charMap: MutableMap<Position, Char> = mutableMapOf()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val p = Position(x, y)
            charMap[p] = c
            if (c == 'S') {
                start = p
            } else {
                c.connect()?.let { (a, b) ->
                    connections.add(p to a.move(p))
                    connections.add(p to b.move(p))
                }
            }
        }
    }
    connections.forEach { (left, right) ->
        if (connections.contains(right to left)) {
            graph.addEdge(left, right)
        }
        if (left == start || right == start) {
            graph.addEdge(right, left)
        }
    }
    val yRange = 0..<input.count()
    val xRange = 0..<input.first().length
    charMap[start] = '-'
    return PipeMap(graph, start, yRange, xRange, charMap)
}

object Day10 {
    fun solve1(input: Sequence<String>): Long {
        val (graph, start) = buildPipePap(input)
        val r = graph.dijkstra(start = start, end = null)
        return r.steps.maxOf { it.value }
    }

    fun solve2(input: Sequence<String>): Int {
        val (graph, start, yRange, xRange, charMap) = buildPipePap(input)
        val (left, right) = graph.outgoingNodes(start).toList()
        graph.removeEdge(start, left)
        graph.removeEdge(left, start)
        val loop = graph.dfs(start).toSet()
        return tracing(yRange, xRange, loop, charMap)
    }

    private fun tracing(
        yRange: IntRange,
        xRange: IntRange,
        loopIndex: Set<Position>,
        charMap: Map<Position, Char>
    ): Int {
        val insidePoints = mutableSetOf<Position>()
        yRange.forEach { y ->
            var inside = false
            var x = 0
            while (x in xRange) {
                val p = Position(x, y)
                if (loopIndex.contains(p)) {
                    val c = charMap[p]!!
                    if (c.isPointingUp()) {
                        inside = !inside
                    }
                } else {
                    if (inside) {
                        insidePoints.add(p)
                    }
                }
                x++
            }
        }
        return insidePoints.size
    }
}

private fun Char.isPointingUp() = when (this) {
    '|' -> true
    'L' -> true
    'J' -> true
    else -> false
}
