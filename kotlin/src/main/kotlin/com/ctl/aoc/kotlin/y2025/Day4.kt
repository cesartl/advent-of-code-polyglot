package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Grid
import com.ctl.aoc.kotlin.utils.parseGrid

object Day4 {

    data class PaperGrid(
        val grid: Grid<Boolean>
    ) {
        fun remove(): Pair<PaperGrid, Int> {
            val toRemove = grid.map.entries.filter {
                it.key.neighbours()
                    .filter { p -> grid.inScope(p) }
                    .filter { p -> grid.map[p] == true }
                    .count() < 4
            }.toList()
            val newMap = grid.map.toMutableMap()
            toRemove.forEach {
                newMap.remove(it.key)
            }
            return PaperGrid(Grid(newMap, this.grid.xRange, this.grid.yRange)) to toRemove.size
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val grid = parseGrid(input) { if (it == '@') true else null }
        return grid.map.entries.count {
            it.key.neighbours()
                .filter { p -> grid.inScope(p) }
                .filter { p -> grid.map[p] == true }
                .count() < 4
        }
    }

    fun solve2(input: Sequence<String>): Int {
        val grid = parseGrid(input) { if (it == '@') true else null }
        val paperGrid = PaperGrid(grid)
        return generateSequence(paperGrid.remove()) { (paperGrid, _) ->
            paperGrid.remove()
        }.takeWhile { (_, count) -> count > 0 }
            .map { (_, count) -> count }
            .sum()
    }
}
