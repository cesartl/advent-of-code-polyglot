package com.ctl.aoc.kotlin.y2023

data class SpringRecord(
    val record: String,
    val groups: List<Int>
) {
    val regex: Regex by lazy {
        groups.joinToString(
            separator = "\\.+",
            prefix = "\\.*",
            postfix = "\\.*",
        ) {
            "#{$it}"
        }.toRegex()
    }

    fun matches(record: String): Boolean = regex.matches(record)

    fun generateRecords(): Sequence<String> {
        return go(0, "")
            .filter { this.matches(it) }
    }

    private fun go(index: Int, current: String): Sequence<String> = sequence {
        if (index == record.length) {
            yield(current)
        } else {
            val c = record[index]
            if (c == '?') {
                yieldAll(go(index + 1, "$current."))
                yieldAll(go(index + 1, "$current#"))
            } else {
                yieldAll(go(index + 1, current + c))
            }
        }
    }
}

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
    fun solve1(input: Sequence<String>): Int {
        return input
            .map { it.parseRecord() }
            .map { it.generateRecords().count() }
            .sum()
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
