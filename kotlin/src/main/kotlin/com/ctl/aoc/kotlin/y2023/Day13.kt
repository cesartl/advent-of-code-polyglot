package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.transpose

class MirrorPattern(
    val rows: Array<CharArray>,
    val cols: Array<CharArray>,
    val i: Int,
    val j: Int
) {


    constructor(rows: Array<CharArray>) : this(rows, rows.transpose(), 0, 0)
    constructor(rows: Array<CharArray>, i: Int, j: Int) : this(rows, rows.transpose(), i, j)


    fun findReflection(not: Int? = null): Int? {
        return rows.findReflection(
            not?.let { it / 100 - 1 }
        )?.let { (it + 1) * 100 }
            ?: cols.findReflection(
                not?.let { it - 1 }
            )?.let { it + 1 }
    }

    private fun allSmudges(): Sequence<MirrorPattern> = sequence {
        val nRows = rows.size
        val nCols = rows.first().size
        (0..<nRows).forEach { j ->
            (0..<nCols).forEach { i ->
                val newRows = rows.deepCopy()
                newRows[j][i] = newRows[j][i].swap()
                yield(MirrorPattern(newRows, i, j))
            }
        }
    }

    fun findSmudgedReflection(): Int {
        val originalReflection = findReflection()!!
        return allSmudges()
            .mapNotNull { it.findReflection(originalReflection)?.let { r -> r to it } }
            .firstOrNull()
            ?.first
            ?: error("")
    }

    override fun toString(): String {
        return "MirrorPattern(i=$i, j=$j)"
    }
}

private fun Char.swap(): Char {
    return when (this) {
        '.' -> '#'
        '#' -> '.'
        else -> error("invalid char: $this")
    }
}

private fun String.parseIslandPattern(): MirrorPattern {
    val rows = this
        .splitToSequence("\n")
        .map { it.toCharArray() }
        .toList()
        .toTypedArray()
    return MirrorPattern(rows)
}

private fun String.parsePatterns(): Sequence<MirrorPattern> {
    return this.splitToSequence("\n\n")
        .map { it.trim() }
        .map { it.parseIslandPattern() }
}

private fun Array<CharArray>.checkReflection(i: Int): Boolean {
    val down = (i downTo 0)
        .asSequence()
        .map { this[it] }
    val up = (i + 1..<this.size)
        .asSequence()
        .map { this[it] }
    return down.zip(up).all { (left, right) -> left.contentEquals(right) }
}

private fun Array<CharArray>.findReflection(not: Int? = null): Int? {
    return (0..<this.size - 1)
        .filter { not == null || it != not }
        .find { checkReflection(it) }
}

private fun Array<CharArray>.deepCopy(): Array<CharArray> {
    val cols = this.first().size
    val rows = this.size
    return Array(rows) { j ->
        CharArray(cols) { i ->
            this[j][i]
        }
    }
}

object Day13 {
    fun solve1(input: String): Int {
        val patterns = input
            .parsePatterns()
            .toList()
        return patterns.sumOf { p -> p.findReflection() ?: error("no reflection") }
    }

    fun solve2(input: String): Int {
        return input.parsePatterns()
            .map { it.findSmudgedReflection() }
            .sum()
    }
}
