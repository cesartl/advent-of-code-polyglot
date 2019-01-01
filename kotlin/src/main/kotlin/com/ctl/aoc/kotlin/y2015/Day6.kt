package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Position

object Day6 {
    data class Grid(val lights: MutableSet<Position> = mutableSetOf()) {
        private fun lights(from: Position, to: Position): Sequence<Position> {
            return sequence {
                for (y in from.y..to.y) {
                    for (x in from.x..to.x) {
                        yield(Position(x, y))
                    }
                }
            }
        }

        fun turnOn(p: Position) {
            lights.add(p)
        }

        fun turnOff(p: Position) {
            lights.remove(p)
        }

        fun toggle(p: Position) {
            if (lights.contains(p)) turnOff(p) else turnOn(p)
        }

        fun turnOn(from: Position, to: Position) = lights(from, to).forEach { turnOn(it) }
        fun turnOff(from: Position, to: Position) = lights(from, to).forEach { turnOff(it) }
        fun toggle(from: Position, to: Position) = lights(from, to).forEach { toggle(it) }

        fun apply(line: String) {
            val split = line.split(" ")
            when {
                line.startsWith("turn on") -> {
                    val from = Position.parse(split[2])
                    val to = Position.parse(split[4])
                    turnOn(from, to)
                }
                line.startsWith("turn off") -> {
                    val from = Position.parse(split[2])
                    val to = Position.parse(split[4])
                    turnOff(from, to)
                }
                else -> {
                    val from = Position.parse(split[1])
                    val to = Position.parse(split[3])
                    toggle(from, to)
                }
            }
        }
    }

    data class Grid2(val lights: MutableMap<Position, Long> = mutableMapOf()) {
        private fun lights(from: Position, to: Position): Sequence<Position> {
            return sequence {
                for (y in from.y..to.y) {
                    for (x in from.x..to.x) {
                        yield(Position(x, y))
                    }
                }
            }
        }

        fun turnOn(p: Position) {
            lights.merge(p, 1) { t, u -> t + u }
        }

        fun turnOff(p: Position) {
            val v = lights[p]
            if(v != null){
                if(v <= 1) lights.remove(p) else lights.merge(p, -1) { t, u -> t + u }
            }
        }

        fun toggle(p: Position) {
            lights.merge(p, 2) { t, u -> t + u }
        }

        fun turnOn(from: Position, to: Position) = lights(from, to).forEach { turnOn(it) }
        fun turnOff(from: Position, to: Position) = lights(from, to).forEach { turnOff(it) }
        fun toggle(from: Position, to: Position) = lights(from, to).forEach { toggle(it) }

        fun apply(line: String) {
            val split = line.split(" ")
            when {
                line.startsWith("turn on") -> {
                    val from = Position.parse(split[2])
                    val to = Position.parse(split[4])
                    turnOn(from, to)
                }
                line.startsWith("turn off") -> {
                    val from = Position.parse(split[2])
                    val to = Position.parse(split[4])
                    turnOff(from, to)
                }
                else -> {
                    val from = Position.parse(split[1])
                    val to = Position.parse(split[3])
                    toggle(from, to)
                }
            }
        }
    }


    fun solve1(lines: Sequence<String>): Int {
        val grid = Grid()
        lines.forEach { grid.apply(it) }
        return grid.lights.size
    }

    fun solve2(lines: Sequence<String>): Long {
        val grid = Grid2()
        lines.forEach { grid.apply(it) }
        return grid.lights.values.sum()
    }

}