package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Lists
import com.ctl.aoc.kotlin.utils.PathingResult
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.y2016.Day24.GridElement.Target
import com.ctl.aoc.kotlin.y2016.Day24.GridElement.Wall

object Day24 {
    sealed class GridElement {
        object Wall : GridElement()
        data class Target(val i: Int) : GridElement()
    }


    data class Grid(val elements: Map<Position, GridElement>, val targets: Map<Int, Position>) {

        fun neighbours(position: Position): Sequence<Position> {
            return position.adjacent().filter { p ->
                when (elements[p]) {
                    is Wall -> false
                    else -> true
                }
            }
        }

        companion object {
            fun parse(lines: Sequence<String>): Grid {
                val elements = mutableMapOf<Position, GridElement>()
                val targets = mutableMapOf<Int, Position>()
                lines.forEachIndexed { y, row ->
                    row.forEachIndexed { x, c ->
                        val p = Position(x, y)
                        if (c == '#') {
                            elements[p] = Wall
                        } else if (c.isDigit()) {
                            val i = c - '0'
                            elements[p] = Target(i)
                            targets[i] = p
                        }
                    }
                }
                return Grid(elements, targets)
            }
        }
    }

    private fun pathTarget(target: Position, grid: Grid): PathingResult<Position> {
        return Dijkstra.traverse(target, null, { grid.neighbours(it) }, { _, _ -> 1 })
    }

    data class PathedGrid(val grid: Grid) {
        val targetPathing: Map<Position, PathingResult<Position>>

        init {
            val tmp = mutableMapOf<Position, PathingResult<Position>>()
            grid.targets.forEach { i, target ->
                tmp[target] = pathTarget(target, grid)
                println("Finished pathing for $i")
            }
            targetPathing = tmp.toMap()
        }

        fun cost(route: List<Position>): Long {
            val (total, _) = route.fold(0L to grid.targets[0]!!) { (acc, previous), position ->
                (acc + (targetPathing[previous]?.steps?.get(position)!!)) to position
            }
            return total
        }
    }

    fun solve1(lines: Sequence<String>): Long {
        val grid = PathedGrid(Grid.parse(lines))
        val targets = grid.grid.targets.filter { it.key > 0 }.values.toList()
        return Lists.permutations(targets).map { grid.cost(it) }.min() ?: 0
    }

    fun solve2(lines: Sequence<String>): Long {
        val grid = PathedGrid(Grid.parse(lines))
        val targets = grid.grid.targets.filter { it.key > 0 }.values.toList()
        return Lists.permutations(targets).map { it + grid.grid.targets[0]!! }.map { grid.cost(it) }.min() ?: 0
    }
}