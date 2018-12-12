package com.ctl.aoc.kotlin.y2018

import java.lang.IllegalArgumentException
import java.util.regex.Pattern

object Day12 {

    data class Rule(val offsets: List<Pair<Long, Boolean>>, val result: Boolean)

    data class State(val plants: MutableMap<Long, Boolean>) {
        fun plantAt(i: Long): Boolean = plants[i] ?: false
        fun ruleMatched(position: Long, rule: Rule): Boolean {
            return rule.offsets.all { this.plantAt(position + it.first) == it.second }
        }

        fun print(): String {
            val start = (plants.keys.min() ?: 0)
            val end = (plants.keys.max() ?: 0)
            return (start..end).map { if (plantAt(it)) '#' else '.' }.joinToString(separator = "")
        }

        fun count(): Long = plants.filter { it.value }.map { it.key }.sum()
    }

    fun parseState(string: String): State {
        return State(string.mapIndexed { index, c -> index.toLong() to (c == '#') }.toMap().toMutableMap())
    }


    val pattern = Pattern.compile("([\\.#]{5}) => ([\\.#])")
    fun parseRule(line: String): Rule {
        val m = pattern.matcher(line)
        if (m.matches()) {

            val offsets = m.group(1).mapIndexed { index, c -> (index - 2L) to (c == '#') }
            val result = m.group(2) == "#"
            return Rule(offsets, result)
        }
        throw IllegalArgumentException(line)
    }

    fun nextGeneration(state: State, rules: Sequence<Rule>): State {
        val map = mutableMapOf<Long, Boolean>()
        val start = (state.plants.keys.min() ?: 0) - 2
        val end = (state.plants.keys.max() ?: 0) + 2
        for (p in start..end) {
            val matched = rules.find { state.ruleMatched(p, it) }
            if (matched != null) {
                map[p] = matched.result
            } else {
//                map[p] = state.plantAt(p)
            }
        }
        return State(map)
    }

    fun solve1(initialState: String, rulesLines: Sequence<String>, generations: Long): Long {
        var state = parseState(initialState)
        var rules = rulesLines.map { parseRule(it) }
        for (i in 0 until generations) {
            println("count ${state.count()} generation: $i")
            println(state.print())
            println()
            state = nextGeneration(state, rules)
        }
        return state.count()
    }

    fun solve2(generations: Long): Long = 724 + (generations - 101) * 5
}