package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.*

private typealias Elevation = Int

object Day12 {

    data class Grid(val map: Map<Position, Elevation>, val start: Position, val target: Position) {
        fun neighboursUp(p: Position): Sequence<Position> {
            val e = map[p]!!
            return p.adjacent()
                .filter { map.containsKey(it) }
                .filter { map[it]!! <= e + 1 }
        }

        fun neighboursDown(p: Position): Sequence<Position> {
            val e = map[p]!!
            return p.adjacent()
                .filter { map.containsKey(it) }
                .filter { map[it]!! >= e - 1 }
        }
    }

    private fun parseGrid(input: Sequence<String>): Grid {
        val map = mutableMapOf<Position, Elevation>()
        var start: Position? = null
        var target: Position? = null
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, e ->
                val p = Position(x, y)
                when (e) {
                    in ('a'..'z') -> {
                        map[p] = e - 'a'
                    }

                    'S' -> {
                        start = p
                        map[p] = 0
                    }

                    'E' -> {
                        target = p
                        map[p] = 'z' - 'a'
                    }
                }
            }
        }
        return Grid(map, start!!, target!!)
    }

    private fun Grid.traverseUp(): PathingResult<Position> {
        return Dijkstra.traverse(
            start = this.start,
            end = this.target,
            nodeGenerator = { this.neighboursUp(it) },
            distance = { _, _ -> 1L },
            constraints = listOf(StepConstraint(484))
        )
    }

    private fun Grid.traverseDown(): PathingResult<Position> {
        return Dijkstra.traverse(
            start = this.target,
            end = null,
            nodeGenerator = { this.neighboursDown(it) },
            distance = { _, _ -> 1L },
            constraints = listOf(CustomConstraint { p, _ -> this.map[p]!! != 0 })
        )
    }

    fun solve1(input: Sequence<String>): Long {
        val grid = parseGrid(input)
        return grid.traverseUp().steps[grid.target] ?: error("No path found")
    }

    fun solve2(input: Sequence<String>): Long {
        val grid = parseGrid(input)
        return grid.traverseDown().let {
            it.steps[it.lastNode!!]
        } ?: error("")
    }
}
