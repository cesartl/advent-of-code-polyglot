package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.frequency

object Day14 {


    data class PolymerPlus(val pairs: Map<Pair<Char, Char>, Long>, val charCounts: Map<Char, Long>) {

        companion object {
            fun parse(s: String): PolymerPlus {
                val pairs = s.zipWithNext()
                        .groupBy { it }
                        .mapValues { it.value.size.toLong() }
                return PolymerPlus(pairs, s.toList().frequency().mapValues { it.value.toLong() })
            }
        }
    }

    fun PolymerPlus.applyRules(reactions: Map<Pair<Char, Char>, Char>): PolymerPlus {
        val (newPairs, newCount) = pairs.entries.fold(mapOf<Pair<Char, Char>, Long>() to charCounts) { (acc, countMap), entry ->
            val pair = entry.key
            val count = entry.value
            reactions[pair]?.let { product ->
                val p1 = pair.first to product
                val p2 = product to pair.second
                val newAcc = acc + (p1 to count + (acc[p1] ?: 0L)) + (p2 to count + (acc[p2] ?: 0L))
                val newCountMap = countMap + (product to (countMap[product] ?: 0L) + count)
                newAcc to newCountMap
            } ?: acc to countMap
        }
        return PolymerPlus(newPairs, newCount)
    }

    fun solve1(input: Sequence<String>): Long {
        return runSteps(input, 10)
    }

    fun solve2(input: Sequence<String>): Long {
        return runSteps(input, 40)
    }

    private fun runSteps(input: Sequence<String>, n: Int): Long {
        val (polymer2, ruleMap) = parse(input)
        val result = generateSequence(polymer2) { it.applyRules(ruleMap) }
                .drop(n)
                .first()
        val frequency = result.charCounts
        return frequency.maxByOrNull { it.value }!!.value - frequency.minByOrNull { it.value }!!.value
    }


    fun parse(lines: Sequence<String>): Pair<PolymerPlus, Map<Pair<Char, Char>, Char>> {
        val polymer = PolymerPlus.parse(lines.first())
        val rules = lines.drop(2)
                .map { line ->
                    val parts = line.split(" -> ")
                    parts[0] to parts[1]
                }
                .toList()
        return polymer to rules.associate { (it.first[0] to it.first[1]) to it.second[0] }
    }
}