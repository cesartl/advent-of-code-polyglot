package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.Graph


object Day7 {
    val containsRegex = """([\w ]+) bags contain (.*)""".toRegex()
    val quantityRegex = """([0-9]+) ([\w ]+) bags?""".toRegex()
    val noBagRegex = """([\w ]+) bags contain no other bags\.""".toRegex()

    data class Contains(val n: Int, val colour: String)
    sealed class BagRule {
        data class ContainsRule(val colour: String, val contains: List<Contains>) : BagRule()
        data class NoBagRule(val colour: String) : BagRule()
        companion object {
            fun parse(line: String): BagRule {
                val parsed = containsRegex.matchEntire(line)?.let { match ->
                    val colour = match.groupValues[1]
                    val contains = quantityRegex.findAll(match.groupValues[2]).map {
                        Contains(it.groupValues[1].toInt(), it.groupValues[2])
                    }.toList()
                    ContainsRule(colour, contains)
                } ?: (noBagRegex.matchEntire(line)?.let { NoBagRule(it.groupValues[1]) })
                return parsed ?: throw IllegalArgumentException("Could not parse $line")
            }
        }
    }

    data class Node(val colours: String, val n: Int)

    fun solve1(input: Sequence<String>): Int {
        val graph = Graph<String>()
        input.map { BagRule.parse(it) }.forEach { rule ->
            when (rule) {
                is BagRule.ContainsRule -> {
                    rule.contains.forEach { contained ->
                        graph.addDirectedEdge(contained.colour, rule.colour)
                    }
                }
                is BagRule.NoBagRule -> {
                    //nothing for now
                }
            }
        }
        val dfs = graph.dfs("shiny gold").toList()
        return dfs.size - 1
    }

    fun solve2(input: Sequence<String>): Int {
        val map = mutableMapOf<String, List<Contains>>()
        input.map { BagRule.parse(it) }.forEach { rule ->
            when (rule) {
                is BagRule.ContainsRule -> {
                    map[rule.colour] = rule.contains
                }
                is BagRule.NoBagRule -> {
                    map[rule.colour] = listOf()
                }
            }
        }
        return count("shiny gold", map) -1
    }

    fun count(start: String, map: Map<String, List<Contains>>): Int {
        val l = map[start]?: listOf()
        if(l.isEmpty()){
            return 1
        }
        return 1 + l.map { it.n * count(it.colour, map) }.sum()
    }
}