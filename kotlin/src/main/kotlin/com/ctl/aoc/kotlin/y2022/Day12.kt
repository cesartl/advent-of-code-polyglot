package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.PathingResult
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.StepConstraint

private typealias Elevation = Int

object Day12 {

    data class Grid(val map: Map<Position, Elevation>, val start: Position, val target: Position) {
        fun neighbours(p: Position): Sequence<Position> {
            val e = map[p]!!
            return p.adjacent()
                .filter { map.containsKey(it) }
                .filter { map[it]!! <= e + 1 }
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

    private fun Grid.traverse(start: Position = this.start): PathingResult<Position> {
        return Dijkstra.traverse(
            start = start,
            end = this.target,
            nodeGenerator = { this.neighbours(it) },
            distance = { _, _ -> 1L },
//            heuristic = {(this.map[this.target]!! - this.map[it]!!).toLong() },
            constraints = listOf(StepConstraint(484))
        )
    }

    fun solve1(input: Sequence<String>): Long {
        val grid = parseGrid(input)
        return grid.traverse().steps[grid.target] ?: error("No path found")
    }

    fun solve2(input: Sequence<String>): Long {
        val grid = parseGrid(input)
        val results = grid.map.filter { it.value == 0 }
            .asSequence()
            .map { grid.traverse(it.key) }
            .mapNotNull { it.steps[grid.target] }
        return results.min()
    }
}
