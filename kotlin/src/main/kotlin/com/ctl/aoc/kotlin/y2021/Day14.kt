package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.frequency

object Day14 {

    @JvmInline
    value class Polymer(val poly: String)

    data class Rule(val pattern: String, val output: String) {
        val regex = pattern.toRegex()
        fun findAllMatches(poly: String): Sequence<Int> = sequence {
            yieldAll(findOneIndex(0, poly))
        }

        fun findOneIndex(start: Int, poly: String): Sequence<Int> = sequence {
//            println("start $start: ${poly.drop(start)}")
            val i = poly.indexOf(string = pattern, startIndex = start)
//            println("i $i")
            if (i >= 0) {
                yield(i)
                yieldAll(findOneIndex(i + 1, poly))
            }
        }

    }

    fun String.insertAt(i: Int, s: String): String {
        return this.slice(0 until i) + s + this.drop(i)
    }

    fun Polymer.applyRules(rules: List<Rule>): Polymer {
        val matches = rules.flatMap { rule ->
            rule.findAllMatches(poly).map { rule to it }
        }.sortedBy { it.second }
        val newPoly = matches.foldIndexed(poly) { index, acc, (rule, ruleIdx) ->
            acc.insertAt(index + ruleIdx + 1, rule.output)
        }
        return Polymer(newPoly)
    }

    fun solve1(input: Sequence<String>): Int {
        val (polymer, rules) = parse(input)
        val result = generateSequence(polymer) { it.applyRules(rules) }
                .drop(10)
                .first()
        val frequency = result.poly.toList().frequency()
        return frequency.maxByOrNull { it.value }!!.value - frequency.minByOrNull { it.value }!!.value
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }

    fun parse(lines: Sequence<String>): Pair<Polymer, List<Rule>> {
        val polymer = Polymer(lines.first())
        val rules = lines.drop(2)
                .map { line ->
                    val parts = line.split(" -> ")
                    Rule(parts[0], parts[1])
                }
                .toList()
        return polymer to rules
    }
}