package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position


object Day25 {

    data class Grid(val east: Set<Position>, val south: Set<Position>, val bottomRight: Position) {

        fun Position.moveEast(): Position {
            val (x, y) = this
            return Position((x + 1) % bottomRight.x, y)
        }

        fun Position.moveSouth(): Position {
            val (x, y) = this
            return Position(x, (y + 1) % bottomRight.y)
        }

        companion object {
            fun parse(lines: Sequence<String>): Grid {
                val east = mutableSetOf<Position>()
                val south = mutableSetOf<Position>()
                lines.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        if (c == '>') {
                            east.add(Position(x, y))
                        } else if (c == 'v') {
                            south.add(Position(x, y))
                        }
                    }
                }
                val maxY = lines.count()
                val maxX = lines.first().length
                return Grid(east, south, Position(maxX, maxY))
            }
        }
    }

    fun Grid.next(): Grid {
        val newEast = east
            .map { it to it.moveEast() }
            .map { (p, pp) -> if (!east.contains(pp) && !south.contains(pp)) pp else p }
            .toSet()

        val newSouth = south
            .map { it to it.moveSouth() }
            .map { (p, pp) -> if (!newEast.contains(pp) && !south.contains(pp)) pp else p }
            .toSet()
        return this.copy(east = newEast, south = newSouth)
    }

    fun solve1(input: Sequence<String>): Int {
        val grid = Grid.parse(input)
        val seq = generateSequence(grid) { it.next() }
        return seq.zipWithNext().withIndex().first { it.value.first == it.value.second }.index + 1
    }


    fun Grid.print() {
        (0 until bottomRight.y).forEach { y ->
            (0 until bottomRight.x).forEach { x ->
                val p = Position(x, y)
                when {
                    east.contains(p) -> {
                        print('>')
                    }
                    south.contains(p) -> {
                        print('v')
                    }
                    else -> {
                        print('.')
                    }
                }
            }
            println()
        }
    }
}