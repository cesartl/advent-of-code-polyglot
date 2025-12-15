package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Graph

object Day11 {
    fun solve1(input: Sequence<String>): Int {
        val g = parseGraph(input)
        val cache = mutableMapOf<Pair<String, String>, Int>()
        return countPaths("you", "out", g, cache)
    }

    fun solve2(input: Sequence<String>): Long {
        val g = parseGraph(input)
        val cache = mutableMapOf<CacheKey, Long>()
        return countPaths2("svr", "out", g, cache)
    }

    private fun parseGraph(input: Sequence<String>): Graph<String> {
        val g = Graph<String>()
        input.forEach { line ->
            val (from, tos) = line.split(":").map { it.trim() }
            tos.splitToSequence(" ").map { it.trim() }.forEach { to ->
                g.addDirectedEdge(from, to)
            }
        }
        return g
    }

    private fun countPaths(
        start: String,
        to: String,
        graph: Graph<String>,
        cache: MutableMap<Pair<String, String>, Int>
    ): Int {
        cache[start to to]?.let { return it }
        val result = if (start == to) {
            1
        } else {
            graph.outgoingNodes(start).sumOf { countPaths(it, to, graph, cache) }
        }
        cache[start to to] = result
        return result
    }

    data class CacheKey(
        val from: String,
        val to: String,
        val visitedFft: Boolean,
        val visitedDac: Boolean
        )

    private fun countPaths2(
        start: String,
        to: String,
        graph: Graph<String>,
        cache: MutableMap<CacheKey, Long>,
        visitedFft: Boolean = false,
        visitedDac: Boolean = false
    ): Long {
        val key = CacheKey(start, to, visitedFft, visitedDac)
        cache[key]?.let { return it }
        val result = if (start == to) {
            if (visitedFft && visitedDac) {
                1
            } else {
                0
            }
        } else {
            graph.outgoingNodes(start).sumOf { countPaths2(
                it,
                to,
                graph,
                cache,
                visitedFft = visitedFft || it == "fft",
                visitedDac = visitedDac || it == "dac"
            ) }
        }
        cache[key] = result
        return result
    }


}
