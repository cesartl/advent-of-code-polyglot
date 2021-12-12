package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position

object Day11 {

    data class Grid(val energies: Map<Position, Int>) {

        fun runSteps(): Sequence<Pair<Grid, Int>> = generateSequence(this to 0) { (g, counter) ->
            val (newGrid, flashes) = g.nextStep()
            newGrid to (counter + flashes)
        }

        fun allFlashed(): Boolean = energies.asSequence().all { it.value == 0 }

        fun nextStep(): Pair<Grid, Int> {
            val newEnergies = energies.toMutableMap()
            //increase all energies
            newEnergies.forEach { (position, _) ->
                newEnergies[position] = newEnergies[position]!! + 1
            }
            val flashed = mutableSetOf<Position>()
            var newFlash: Boolean
            do {
                newFlash = false
                newEnergies.forEach { (position, e) ->
                    if (e > 9 && !flashed.contains(position)) {
                        newFlash = true
                        flashed.add(position)
                        //increasing all neighbours
                        position.neighbours().forEach { n ->
                            newEnergies[n]?.let { ne ->
                                newEnergies[n] = ne + 1
                            }
                        }
                    }
                }
            } while (newFlash)
            flashed.forEach { flash ->
                newEnergies[flash] = 0
            }
            return Grid(newEnergies) to flashed.size
        }

        fun print(): String {
            return energies.asSequence()
                    .groupBy { it.key.y }
                    .toList()
                    .sortedBy { it.first }
                    .joinToString(separator = "\n") { row -> row.second.sortedBy { it.key.x }.joinToString(separator = "") { it.value.toString() } }
        }

        companion object {
            fun parse(lines: Sequence<String>): Grid {
                val energies = lines.withIndex().flatMap { (y, line) ->
                    line.splitToSequence("").filter { it != "" }.mapIndexed { x, e -> Position(x, y) to e.toInt() }
                }.toMap()
                return Grid(energies)
            }
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val grid = Grid.parse(input)
        return grid.runSteps()
                .drop(100)
                .first()
                .second
    }

    fun solve2(input: Sequence<String>): Int {
        val grid = Grid.parse(input)
        return grid.runSteps()
                .withIndex()
                .first { it.value.first.allFlashed() }
                .index
    }
}