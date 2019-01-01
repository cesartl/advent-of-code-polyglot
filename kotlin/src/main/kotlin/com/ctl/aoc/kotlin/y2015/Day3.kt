package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.*

object Day3 {

    fun parse(char: Char): Orientation = when (char) {
        '^' -> N
        '>' -> E
        '<' -> W
        'v' -> S
        else -> throw IllegalArgumentException(char.toString())
    }

    fun solve1(moves: String): Int {
        return visit(moves.map { parse(it) }).size
    }

    private fun visit(moves: List<Orientation>): Set<Position> {
        var start = Position(0, 0)
        return moves.fold(start to setOf(start)) { (p, visited), direction ->
            val next = direction.move(p)
            next to visited + next
        }.second
    }

    fun solve2(moves: String): Int {
        val orientations = moves.map { parse(it) }

        val santa = visit(orientations.filterIndexed { index, _ -> index % 2 == 0 })
        val robot = visit(orientations.filterIndexed { index, _ -> index % 2 == 1 })
        return (santa + robot).size
    }
}