package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*

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
    val start: Position
)

private fun buildPipePap(input: Sequence<String>): PipeMap {
    var start = Position(0, 0)
    val graph = Graph<Position>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val p = Position(x, y)
            if (c == 'S') {
                start = p
            } else {
                c.connect()?.let { (a, b) ->
                    graph.addDirectedEdge(a.move(p), p)
                    graph.addDirectedEdge(b.move(p), p)
                }
            }
        }
    }
    return PipeMap(graph, start)
}

object Day10 {
    fun solve1(input: Sequence<String>): Long {
        val (graph, start) = buildPipePap(input)
        val r = graph.dijkstra(start = start, end = null)
        return r.steps.maxOf { it.value }
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
