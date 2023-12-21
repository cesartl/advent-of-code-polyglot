package com.ctl.aoc.kotlin.y2023

data class SpringRecord(
    val record: String,
    val groups: List<Int>
)

private fun String.parseRecord(): SpringRecord {
    val parts = this.split(" ")
    val record = parts[0]
    val groups = parts[1]
        .splitToSequence(",")
        .map { it.trim() }
        .map { it.toInt() }
        .toList()
    return SpringRecord(record, groups)
}

object Day12 {
    fun solve1(input: Sequence<String>): Long {
        return input
            .map { it.parseRecord() }
            .map { count(it.record, it.groups) }
            .sum()
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }

    private fun count(record: String, groups: List<Int>): Long {
        if (record.isEmpty()) {
            return if (groups.isEmpty()) {
                1L
            } else {
                0L
            }
        }
        val c = record.first()
        val next = record.drop(1)
        if (c == '.') {
            return count(next, groups)
        }
        if (c == '?') {
            return count(".$next", groups) + count("#$next", groups)
        }
        if (groups.isEmpty()) {
            return 0
        }
        val group = groups.first()
        if (record.length < group) {
            return 0
        }
        if (record.subSequence(0, group).any { it == '.' }) {
            return 0
        }
        val next2 = record.drop(group)
        val first = next2.firstOrNull()
        return when (first) {
            '#' -> {
                0
            }

            '?' -> {
                count(next2.drop(1), groups.drop(1))
            }

            else -> {
                return count(next2, groups.drop(1))
            }
        }
    }
}
