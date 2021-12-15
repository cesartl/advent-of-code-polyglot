package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.PathingResultInt
import com.ctl.aoc.kotlin.utils.Position


object Day15 {

    data class Cave(val risk: Map<Position, Int>) {

        val bottomRight: Position by lazy {
            Position(risk.keys.maxByOrNull { it.x }!!.x, risk.keys.maxByOrNull { it.y }!!.y)
        }

        fun print() {
            (0..bottomRight.y).forEach { y ->
                (0..bottomRight.x).forEach { x ->
                    print(risk[Position(x, y)])
                }
                println()
            }
        }

        companion object {
            fun parse(lines: Sequence<String>): Cave {
                val risk = lines.flatMapIndexed { y, line ->
                    line.splitToSequence("").filter { it != "" }.mapIndexed { x, r -> Position(x, y) to r.toInt() }
                }.toMap()
                return Cave(risk)
            }
        }
    }

    fun Cave.duplicate(n: Int): Cave {
        val allTiles = risk.toMutableMap()
        val (X, Y) = bottomRight
        (0 until n).forEach { row ->
            (0 until n).forEach { column ->
                risk.entries.forEach { (p, risk) ->
                    val newP = p + Position((X + 1) * column, (Y + 1) * row)
                    val newRisk = (risk + row + column - 1).mod(9) + 1
                    if (!allTiles.contains(newP)) {
                        allTiles[newP] = newRisk
                    }
                }
            }
        }
        return Cave(allTiles)
    }

    fun Cave.adjacent(p: Position): Sequence<Position> = p.adjacent().filter { risk.contains(it) }

    fun Cave.findExit(): PathingResultInt<Position> = Dijkstra.traverseInt(Position(0, 0), this.bottomRight, { this.adjacent(it) }, { _, e -> this.risk[e]!! }, heuristic = { 0 })


    fun solve1(input: Sequence<String>): Int {
        val cave = Cave.parse(input)
        val result = cave.findExit()
        return result.steps[cave.bottomRight]!!
    }

    fun solve2(input: Sequence<String>): Int {
        val cave = Cave.parse(input)
        val duplicate = cave.duplicate(5)
        val result = duplicate.findExit()
        println("total visited: ${result.steps.size}")
        return result.steps[duplicate.bottomRight]!!
    }
}