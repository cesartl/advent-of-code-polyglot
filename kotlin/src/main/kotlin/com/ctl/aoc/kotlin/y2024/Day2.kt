package com.ctl.aoc.kotlin.y2024

import kotlin.math.sign

data class Report(
    val levels: List<Int>
) {
    fun isSafe(): Boolean {
        val diffs = levels.zipWithNext { a, b -> a - b }
        val firstSign = diffs.first().sign
        return diffs.all { diffCheck(firstSign, it) }
    }

    fun isSafe2(): Boolean {
        return removalCandidates().any { it.isSafe() }
    }

    private fun diffCheck(firstSign: Int, diff: Int): Boolean{
        return diff.sign == firstSign && diff != 0 && diff in -3..3
    }

    private fun removalCandidates(): Sequence<Report>{
        if(isSafe()){
            return sequenceOf(this)
        }
        val report = this
        return sequence {
           report.levels.indices.forEach { index ->
                val newLevels = report.levels.toMutableList()
                newLevels.removeAt(index)
                yield(Report(newLevels))
           }
        }
    }

}

private fun parseInput(input: Sequence<String>): List<Report> {
    return input
        .map { line ->
            line.split(" ").map { it.toInt() }
        }.map { Report(it) }
        .toList()

}

object Day2 {
    fun solve1(input: Sequence<String>): Int {
        val reports = parseInput(input)
        return reports.count { it.isSafe() }
    }

    fun solve2(input: Sequence<String>): Int {
        val reports = parseInput(input)
        return reports.count { it.isSafe2() }
    }
}
