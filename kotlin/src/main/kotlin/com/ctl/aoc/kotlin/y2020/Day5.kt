package com.ctl.aoc.kotlin.y2020

object Day5 {

    data class Seat(val row: Int, val col: Int) {

        val id: Int = row * 8 + col

        companion object {
            fun parse(s: String): Seat {
                val row = toInt(s.take(7), 'B')
                val col = toInt(s.drop(7), 'R')
                return Seat(row, col)
            }
        }
    }

    fun solve1(input: Sequence<String>): Int? {
        return input.map { Seat.parse(it).id }.max()
    }

    fun solve2(input: Sequence<String>): Int? {
        val seats = input.map { Seat.parse(it) }.sortedBy { it.id }.toList()
        val ids = seats.map { it.id }.toSet()
        val min = seats.minBy { it.id }!!
        val max = seats.maxBy { it.id }!!
        println("min $min ${min.id}")
        println("max $max ${max.id}")
        val start = Seat(min.row, 7).id + 1
        val end = Seat(max.row, 0).id
        (start until end).forEach {
            if (!ids.contains(it)) {
                return it
            }
        }
        return null
    }

    fun toInt(s: String, upperChar: Char): Int {
        return Integer.parseInt(s.map { if (it == upperChar) '1' else '0' }.joinToString(""), 2)
    }
}