package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Lists

object Day13 {
    data class SeatingRule(val p1: String, val p2: String, val happiness: Int) {
        companion object {
            val regex = """([\w]+) would (gain|lose) ([\d]+) happiness units by sitting next to ([\w]+)\.""".toRegex()
            fun parse(s: String): SeatingRule {
                val m = regex.matchEntire(s)
                if (m != null) {
                    val sign = if (m.groupValues[2] == "gain") 1 else -1
                    return SeatingRule(m.groupValues[1], m.groupValues[4], m.groupValues[3].toInt() * sign)
                }
                throw IllegalArgumentException(s)
            }
        }
    }

    fun happiness(guests: List<String>, happinessMap: Map<Pair<String, String>, Int>): Int {
        return guests.foldIndexed(0) { index, acc, s ->
            val neighbour = guests[(index + 1) % guests.size]
            acc + (happinessMap[s to neighbour] ?: 0) + (happinessMap[neighbour to s] ?: 0)
        }
    }

    data class SeatingPlan(val guests: List<String>, val happinessMap: Map<Pair<String, String>, Int>) {
        val happiness = Day13.happiness(guests, happinessMap)
    }

    fun happinessMap(rules: List<SeatingRule>): Map<Pair<String, String>, Int> {
        val map = mutableMapOf<Pair<String, String>, Int>()
        rules.forEach { (p1, p2, h) -> map[p1 to p2] = h }
        return map
    }

    fun solve1(lines: Sequence<String>): Int {
        val rules = lines.map { SeatingRule.parse(it) }.toList()

        val happinessMap = happinessMap(rules)

        val guests = happinessMap.keys.flatMap { listOf(it.first, it.second) }.toSet().toList()

        return Lists.permutations(guests).map { SeatingPlan(it, happinessMap) }.maxByOrNull { it.happiness }?.happiness ?: 0
    }

    fun solve2(lines: Sequence<String>): Int {
        val rules = lines.map { SeatingRule.parse(it) }.toList()

        val happinessMap = happinessMap(rules)

        val guests = happinessMap.keys.flatMap { listOf(it.first, it.second) }.toSet().toList() + "me"

        return Lists.permutations(guests).map { SeatingPlan(it, happinessMap) }.maxByOrNull { it.happiness }?.happiness ?: 0
    }
}