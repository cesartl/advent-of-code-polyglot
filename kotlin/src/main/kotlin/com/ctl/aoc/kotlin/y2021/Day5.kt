package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position

object Day5 {

    data class Line(val start: Position, val end: Position) {


        fun points(): Sequence<Position> {
            val vector = (end - start).normalise()
            return generateSequence(start) { p -> p + vector }
                    .takeWhile { (it - vector) != end }
        }

        val isNotDiagonal: Boolean by lazy {
            start.x == end.x || start.y == end.y
        }

        companion object {
            private val regex = """([\d]+),([\d]+) -> ([\d]+),([\d]+)""".toRegex()
            fun parse(s: String): Line {
                val match = regex.matchEntire(s) ?: error("$s not a Line")
                val values = match.groupValues
                val start = Position(values[1].toInt(), values[2].toInt())
                val end = Position(values[3].toInt(), values[4].toInt())
                return Line(start, end)
            }
        }
    }

    fun solve1(input: Sequence<String>): Int {
        return input.map { Line.parse(it) }
                .filter { it.isNotDiagonal }
                .flatMap { it.points() }
                .groupBy { it }
                .filter { it.value.size > 1 }
                .count()
    }

    fun solve2(input: Sequence<String>): Int {
        return input.map { Line.parse(it) }
                .flatMap { it.points() }
                .groupBy { it }
                .filter { it.value.size > 1 }
                .count()
    }
}