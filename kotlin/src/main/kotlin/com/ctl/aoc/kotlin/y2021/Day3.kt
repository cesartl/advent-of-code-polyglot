package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.leastFrequent
import com.ctl.aoc.kotlin.utils.mostFrequent

object Day3 {
    fun solve1(input: Sequence<String>): Int {
        val numbers: Sequence<List<String>> = input.map { it.split("").filter { c -> c != "" } }
        val grouped: Map<Int, List<String>> = groupByPosition(numbers)


        val gammaBits = grouped.map { e -> e.value.mostFrequent()!!.toInt() }.joinToString("")
        val epsilonBits = gammaBits.map { if (it == '1') '0' else '1' }.joinToString("")
        println("gamma bits $gammaBits")
        println("epsilon bits $epsilonBits")

        val gamma = Integer.parseInt(gammaBits, 2)
        val epsilon = Integer.parseInt(epsilonBits, 2)
        return gamma * epsilon
    }

    private fun groupByPosition(numbers: Sequence<List<String>>) = numbers
            .flatMap {
                it.mapIndexed { index, c -> (index to c) }.asSequence()
            }
            .groupBy { it.first }
            .mapValues { e -> e.value.map { it.second } }

    fun solve2(input: Sequence<String>): Int {
        val numbers: Sequence<List<String>> = input.map { it.split("").filter { c -> c != "" } }
        val o2RateBits = findRate(numbers, 0) { it.mostFrequent() ?: "1" }.joinToString("")
        val co2RateBits = findRate(numbers, 0) { it.leastFrequent() ?: "0" }.joinToString("")
        println("o2 rate bits: $o2RateBits")
        println("co2 rate bits: $co2RateBits")
        val o2Rate = Integer.parseInt(o2RateBits, 2)
        val co2Rate = Integer.parseInt(co2RateBits, 2)
        return o2Rate * co2Rate
    }

    private tailrec fun findRate(numbers: Sequence<List<String>>, position: Int, select: (List<String>) -> String): List<String> {
        val grouped = groupByPosition(numbers)
        val criteria = select(grouped[position]!!)
        println("Criteria is $criteria at $position")
        val filtered = numbers.filter { it[position] == criteria }
        println("Filtered ${filtered.map { it.joinToString("") }.toList()}")
        if (filtered.count() == 1) {
            return filtered.first()
        }
        return findRate(filtered, position + 1, select)
    }
}