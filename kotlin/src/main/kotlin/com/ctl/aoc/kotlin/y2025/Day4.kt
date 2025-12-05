package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.parseGrid

object Day4 {

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
        val rolls = grid.map.keys.toMutableSet()
        var totalRemoved = 0
        val start = System.currentTimeMillis()
        do {
            val toRemove = rolls.filter { c -> c.neighbours().count { n -> rolls.contains(n) } < 4 }
            totalRemoved += toRemove.size
            rolls.removeAll(toRemove.toSet())
        } while (toRemove.isNotEmpty())
        val end = System.currentTimeMillis()
        println("Total removed ${end - start} ms")
        return totalRemoved
    }

    fun solve2Adjacency(input: Sequence<String>): Int {
        val grid = parseGrid(input) { if (it == '@') true else null }

        val adjacency: MutableMap<Position, MutableSet<Position>> = grid
            .map
            .keys
            .associateWithTo(mutableMapOf()) {
                it.neighbours().filterTo(mutableSetOf()) { p -> grid.map.containsKey(p) }
            }

        do {
            val first = adjacency.entries.firstOrNull { it.value.size < 4 }
            if (first == null) {
                break
            }
            remove(first.key, adjacency)
        } while (true)
        return grid.map.size - adjacency.size
    }

    private fun remove(
        key: Position,
        adjacency: MutableMap<Position, MutableSet<Position>>
    ) {
        adjacency.remove(key)?.forEach { k ->
            adjacency[k]?.let {
                it.remove(key)
                if (it.size < 4) {
                    remove(k, adjacency)
                }
            }
        }
    }
}
