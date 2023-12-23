package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.transpose

data class MirrorPattern(
    val rows: List<String>
) {
    val cols: List<String> by lazy {
        rows
            .map { it.toList() }
            .transpose()
            .map { it.joinToString(separator = "") }
    }

    fun findReflection(): Int {
        return rows.findReflection()?.let { (it + 1) * 100 }
            ?: cols.findReflection()?.let { it + 1 }
            ?: error("No reflection")
    }
}

private fun String.parseIslandPattern(): MirrorPattern {
    val rows = this.split("\n")
    return MirrorPattern(rows)
}

private fun String.parsePatterns(): Sequence<MirrorPattern> {
    return this.splitToSequence("\n\n")
        .map { it.trim() }
        .map { it.parseIslandPattern() }
}

private fun List<String>.checkReflection(i: Int): Boolean {
    val down = (i downTo 0)
        .asSequence()
        .map { this[it] }
    val up = (i + 1..<this.size)
        .asSequence()
        .map { this[it] }
    return down.zip(up).all { (left, right) -> left == right }
}

private fun List<String>.findReflection(): Int? {
    return (0..<this.size - 1).find {
        checkReflection(it)
    }
}

object Day13 {
    fun solve1(input: String): Int {
        val patterns = input
            .parsePatterns()
            .toList()
        return patterns.sumOf { p -> p.findReflection() }
    }

    fun solve2(input: String): Int {
        TODO()
    }
}
