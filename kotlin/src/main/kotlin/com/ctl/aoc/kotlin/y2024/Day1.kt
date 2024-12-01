package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.frequency
import kotlin.math.abs

object Day1 {
    fun solve1(input: Sequence<String>): Int {
        val (left, right) = parseLists(input)
        return left.asSequence().sorted().zip(right.asSequence().sorted())
            .map { (l, r) -> abs(l - r) }
            .sum()
    }

    fun solve2(input: Sequence<String>): Int {
        val (left, right) = parseLists(input)
        val rightFrequency = right.frequency()
        return left.asSequence()
            .map { it * (rightFrequency[it] ?: 0) }
            .sum()
    }

    private fun parseLists(input: Sequence<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        input.forEach { line ->
            //regex for whitespace
            val split = line.split("\\s+".toRegex())
            left.add(split[0].toInt())
            right.add(split[1].toInt())
        }
        return Pair(left, right)
    }
}
