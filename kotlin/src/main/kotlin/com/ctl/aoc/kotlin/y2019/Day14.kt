package com.ctl.aoc.kotlin.y2019

import java.util.*
import kotlin.math.ceil

object Day14 {

    data class ReactionElement(val qty: Int, val element: String) {
        fun scale(n: Int): ReactionElement = this.copy(qty = this.qty * n)
    }

    data class Reaction(val input: List<ReactionElement>, val output: ReactionElement) {
        companion object {
            val regex = """([\d]+) ([A-Z]+)""".toRegex()
            fun parse(s: String): Reaction {
                val elements = regex.findAll(s).map { ReactionElement(it.groupValues[1].toInt(), it.groupValues[2]) }.toList()
                return Reaction(input = elements.dropLast(1), output = elements.last())
            }
        }
    }

    data class WithLeftover(val reactionElement: ReactionElement, val leftOver: ReactionElement)

    fun solve1(lines: Sequence<String>): Int {
        val elements = lines.map { Reaction.parse(it) }
        val reactionsByOutput = mutableMapOf<String, Reaction>()
        elements.forEach {
            reactionsByOutput[it.output.element] = it
        }
        val requirements: Deque<ReactionElement> = ArrayDeque()
        reactionsByOutput["FUEL"]?.let {
            requirements.addAll(it.input)
        }
        var oreRequired = 0
        val leftOvers = mutableMapOf<String, Int>()
        while (requirements.isNotEmpty()) {
            val required = requirements.pop()
            val fromLeftOver = ((leftOvers[required.element] ?: 0)).coerceAtMost(required.qty)
            leftOvers[required.element] = (leftOvers[required.element] ?: 0) - fromLeftOver
            val found = findRequirements(required.copy(qty = required.qty - fromLeftOver), reactionsByOutput)
            found.forEach { (r, leftover) ->
                if (r.element == "ORE") {
                    oreRequired += r.qty
                } else {
                    requirements.add(r)
                }
                leftOvers[leftover.element] = (leftOvers[leftover.element] ?: 0) + leftover.qty
            }
        }
        return oreRequired
    }

    private fun findRequirements(required: ReactionElement, reactionsByOutput: Map<String, Reaction>): List<WithLeftover> {
        if (required.qty == 0) {
            return listOf()
        }
        return reactionsByOutput[required.element]?.let { reaction ->
            val n = ceil(required.qty.toDouble() / reaction.output.qty.toDouble()).toInt()
            return reaction.input.map { WithLeftover(it.scale(n), ReactionElement(n * reaction.output.qty - required.qty, required.element)) }
        } ?: listOf()
    }
}