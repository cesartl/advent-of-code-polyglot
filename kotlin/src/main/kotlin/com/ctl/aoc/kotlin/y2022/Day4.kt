package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.includes
import com.ctl.aoc.kotlin.utils.rangeIntersect

object Day4 {

    private val regex = "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex()

    private fun String.parse(): Pair<IntRange, IntRange> {
        return regex.matchEntire(this)?.groupValues?.let {
            val a = it[1].toInt()..it[2].toInt()
            val b = it[3].toInt()..it[4].toInt()
            a to b
        } ?: error(this)
    }

    fun solve1(input: Sequence<String>): Int {
        println(input.count())
        return input
            .map { it.parse() }
            .filter { (a, b) -> a.includes(b) || b.includes(a) }
            .count()
    }

    fun solve2(input: Sequence<String>): Int {
        return input
            .map { it.parse() }
            .filter { (a, b) -> a.rangeIntersect(b) != null }
            .count()
    }
}
