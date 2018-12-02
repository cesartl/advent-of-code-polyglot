package com.ctl.aoc.kotlin.y2018

object Day2p1 {
    fun checksum(ids: Sequence<String>): Int {
        val counts = ids.map { duplicateCount(it) }.reduce { acc, pair -> acc.first + pair.first to acc.second + pair.second }
        return counts.first * counts.second
    }

    fun duplicateCount(id: String): Pair<Int, Int> {
        val byChar = id.groupBy { it }
        val count2 = if (byChar.values.any { it.size == 2 }) 1 else 0
        val count3 = if (byChar.values.any { it.size == 3 }) 1 else 0
        return count2 to count3
    }
}