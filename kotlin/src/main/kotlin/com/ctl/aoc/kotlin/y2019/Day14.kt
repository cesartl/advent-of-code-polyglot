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

    fun solve1(lines: Sequence<String>): Int {
        val elements = lines.map { Reaction.parse(it) }
        val reactionsByOutput = mutableMapOf<String, Reaction>()
        elements.forEach {
            reactionsByOutput[it.output.element] = it
        }
        val required = mutableMapOf<String, Int>()
        required["FUEL"] = 1
        val p: (Map.Entry<String, Int>) -> Boolean = { it.key != "ORE" && it.value > 0 }
        while (required.filter(p).isNotEmpty()) {
            val (requiredElement, requiredQty) = required.filter(p).entries.first()
            reactionsByOutput[requiredElement]?.let { reaction ->
                val n = ceil(requiredQty.toDouble() / reaction.output.qty.toDouble()).toInt()
                required[requiredElement] = (required[requiredElement] ?: 0) - n * reaction.output.qty
                reaction.input.forEach {
                    required[it.element] = (required[it.element] ?: 0) + n * it.qty
                }
            }
        }
        return required["ORE"] ?: 0
    }
}