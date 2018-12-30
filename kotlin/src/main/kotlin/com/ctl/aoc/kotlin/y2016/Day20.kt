package com.ctl.aoc.kotlin.y2016

import java.util.*

object Day20 {

    private fun parse(range: String): LongRange {
        val split = range.split("-").map { it.toLong() }
        return split[0]..split[1]
    }

    fun solve1(lines: Sequence<String>): Long {
        val ranges = lines.map { parse(it) }.sortedBy { it.first }.toList()

        val sortedMap: SortedMap<Long, LongRange> = TreeMap()
        ranges.forEach { sortedMap.put(it.first, it) }

        var max = 0L
        for (r in sortedMap.values) {
            if (r.start > max + 1) {
                return max + 1
            }
            if (r.endInclusive > max) {
                max = r.endInclusive
            }
        }

        return 0
    }

    fun solve2(lines: Sequence<String>): Long {
        val ranges = lines.map { parse(it) }.sortedBy { it.first }.toList()

        val sortedMap: SortedMap<Long, LongRange> = TreeMap()
        ranges.forEach { sortedMap.put(it.first, it) }

        var max = 0L
        var count = 0L
        for (r in sortedMap.values) {
            if (r.start > max + 1) {
                count += r.start - max - 1
            }
            if (r.endInclusive > max) {
                max = r.endInclusive
            }
        }

        return count
    }
}