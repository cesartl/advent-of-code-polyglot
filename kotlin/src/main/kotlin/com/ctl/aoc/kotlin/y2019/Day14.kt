package com.ctl.aoc.kotlin.y2019

import kotlin.math.ceil

object Day14 {

    data class ReactionElement(val qty: Long, val element: String) {
        fun scale(n: Long): ReactionElement = this.copy(qty = this.qty * n)
    }

    data class Reaction(val input: List<ReactionElement>, val output: ReactionElement) {
        companion object {
            val regex = """([\d]+) ([A-Z]+)""".toRegex()
            fun parse(s: String): Reaction {
                val elements = regex.findAll(s).map { ReactionElement(it.groupValues[1].toLong(), it.groupValues[2]) }.toList()
                return Reaction(input = elements.dropLast(1), output = elements.last())
            }
        }
    }

    fun solve1(lines: Sequence<String>): Long {
        val elements = lines.map { Reaction.parse(it) }
        val reactionsByOutput = mutableMapOf<String, Reaction>()
        elements.forEach {
            reactionsByOutput[it.output.element] = it
        }
        return findRequiredOre(reactionsByOutput)
    }

    fun solve2(lines: Sequence<String>): Long {
        val elements = lines.map { Reaction.parse(it) }
        val reactionsByOutput = mutableMapOf<String, Reaction>()
        elements.forEach {
            reactionsByOutput[it.output.element] = it
        }
        val targetOre = 1000000000000L
        var highFuel = targetOre
        var lowFuel = 0L
        var ore: Long
        while (lowFuel <= highFuel) {
            ore = findRequiredOre(reactionsByOutput, (highFuel + lowFuel) / 2)
            if (ore < targetOre) {
                lowFuel = (highFuel + lowFuel) / 2 + 1
            } else if (ore > targetOre) {
                highFuel = (highFuel + lowFuel) / 2 - 1
            }
        }
        return highFuel
    }

    private fun findRequiredOre(reactionsByOutput: MutableMap<String, Reaction>, requiredFuel: Long = 1): Long {
        val required = mutableMapOf<String, Long>()
        required["FUEL"] = requiredFuel
        val p: (Map.Entry<String, Long>) -> Boolean = { it.key != "ORE" && it.value > 0 }
        while (required.filter(p).isNotEmpty()) {
            val (requiredElement, requiredQty) = required.filter(p).entries.first()
            reactionsByOutput[requiredElement]?.let { reaction ->
                val n = ceil(requiredQty.toDouble() / reaction.output.qty.toDouble()).toLong()
                required[requiredElement] = (required[requiredElement] ?: 0) - n * reaction.output.qty
                reaction.input.forEach {
                    required[it.element] = (required[it.element] ?: 0) + n * it.qty
                }
            }
        }
        return required["ORE"] ?: 0
    }
}