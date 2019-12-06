package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Graph
import com.ctl.aoc.kotlin.utils.dijkstra
import com.ctl.aoc.kotlin.utils.findPath

object Day6 {

    fun solve1(lines: Sequence<String>): Long {
        val nodes: MutableMap<String, MutableList<String>> = mutableMapOf()
        lines.map { it.split(")") }.map { it[0] to it[1] }.forEach { (from, to) ->
            nodes.computeIfAbsent(from) { mutableListOf() }.add(to)
        }
        val (steps, previous) = Dijkstra.traverse("COM", null, {
            (nodes[it] ?: listOf<String>()).asSequence()
        }, { _, _ -> 1L })
        return steps.values.sum()
    }

    fun solve2(lines: Sequence<String>): Int {
        val graph = Graph<String>()
        lines.map { it.split(")") }.map { it[0] to it[1] }.forEach { (from, to) ->
            graph.addEdge(from, to)
        }
        val (steps, previous) = graph.dijkstra("YOU", "SAN")

        return findPath("SAN", previous).size -3
    }

}