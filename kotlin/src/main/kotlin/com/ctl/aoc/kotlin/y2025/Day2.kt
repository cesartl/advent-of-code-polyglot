package com.ctl.aoc.kotlin.y2025

object Day2 {

    private val invalidRegex1 = Regex("^(\\d+)\\1$")
    private val invalidRegex2 = Regex("^(\\d+)\\1+$")

    fun solve1(input: String): Long {
        return findInvalidIds(invalidRegex1,input)
    }

    fun solve2(input: String): Long {
        return findInvalidIds(invalidRegex2,input)
    }

    private fun findInvalidIds(regex: Regex, input: String): Long {
        return input.splitToSequence(",")
            .flatMap {
                val (start, end) = it.split("-")
                val range = LongRange(start.toLong(), end.toLong())
                findInvalidIds(regex, range)
            }
            .sum()
    }

    private fun findInvalidIds(regex: Regex, range: LongRange): Sequence<Long>{
        return range.asSequence()
            .filter {
                regex.matches(it.toString())
            }
    }
}
