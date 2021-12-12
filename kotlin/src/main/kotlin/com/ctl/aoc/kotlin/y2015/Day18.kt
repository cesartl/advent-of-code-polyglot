package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Position

object Day18 {

    fun adjacents(position: Position): Sequence<Position> {
        val (x, y) = position
        return sequence {
            yield(Position(x - 1, y - 1))
            yield(Position(x - 1, y))
            yield(Position(x - 1, y + 1))
            yield(Position(x, y - 1))
            yield(Position(x, y + 1))
            yield(Position(x + 1, y - 1))
            yield(Position(x + 1, y))
            yield(Position(x + 1, y + 1))
        }
    }

    data class Grid(val lights: Set<Position>, val max: Position) {
        fun next(): Grid {
            val newLights = mutableSetOf<Position>()
            val (maxX, maxY) = max
            for (y in 0..maxY) {
                for (x in 0..maxX) {
                    val p = Position(x, y)
                    val on = adjacents(p).filter { (x1, y1) -> x1 in 0..max.x && y1 in 0..max.y }.count { lights.contains(it) }
                    if (lights.contains(p)) {
                        if (on in 2..3) {
                            newLights.add(p)
                        }
                    } else {
                        if (on == 3) {
                            newLights.add(p)
                        }
                    }
                }
            }
            return copy(lights = newLights)
        }

        fun turnOnCorners(): Grid {
            val newLights = lights.toMutableSet()
            val (maxX, maxY) = max
            newLights.add(Position(0, 0))
            newLights.add(Position(maxX, 0))
            newLights.add(Position(0, maxY))
            newLights.add(max)
            return copy(lights = newLights)
        }

        fun print(): String {
            val builder = StringBuilder()
            val (maxX, maxY) = max
            for (y in 0..maxY) {
                for (x in 0..maxX) {
                    if (lights.contains(Position(x, y))) {
                        builder.append("#")
                    } else {
                        builder.append(".")
                    }
                }
                builder.append("\n")
            }
            builder.append("\n")
            return builder.toString()
        }

        companion object {
            fun parse(lines: List<String>): Grid {
                val lights = mutableSetOf<Position>()
                for (y in lines.indices) {
                    for (x in lines[y].indices) {
                        if (lines[y][x] == '#') {
                            lights.add(Position(x, y))
                        }
                    }
                }
                val maxX = lights.maxByOrNull { it.x }?.x!!
                val maxY = lights.maxByOrNull { it.y }?.y!!
                return Grid(lights, Position(maxX, maxY))
            }
        }
    }

    fun solve1(lines: Sequence<String>, n: Int): Int {
        var grid = Grid.parse(lines.toList())
//        println(grid.print())
        for (i in 1..n) {
            grid = grid.next()
//            println(grid.print())
        }
        return grid.lights.count()
    }

    fun solve2(lines: Sequence<String>, n: Int): Int {
        var grid = Grid.parse(lines.toList()).turnOnCorners()
        for (i in 1..n) {
            grid = grid.next().turnOnCorners()
        }
        return grid.lights.count()
    }
}