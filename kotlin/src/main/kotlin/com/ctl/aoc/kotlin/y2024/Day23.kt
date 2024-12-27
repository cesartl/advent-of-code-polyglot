package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Graph

object Day23 {
    fun solve1(input: Sequence<String>): Int {
        val graph = Graph<String>()
        input.forEach { line ->
            val (from, to) = line.split("-")
            graph.addEdge(from, to)
        }

        val groups: MutableSet<MutableSet<String>> = mutableSetOf()
        graph.adjacencyMap.keys.forEach { a ->
            graph.outgoingNodes(a).forEach { b ->
                graph.outgoingNodes(b).filter { it != a }.forEach { c ->
                    graph.outgoingNodes(c).forEach { d ->
                        if (d == a) {
                            groups.add(mutableSetOf(a, b, c))
                        }
                    }
                }
            }
        }
        return groups.count { group ->
            group.any { it.startsWith("t") }
        }
    }

    fun solve2(input: Sequence<String>): String {
        val graph = Graph<String>()
        input.forEach { line ->
            val (from, to) = line.split("-")
            graph.addEdge(from, to)
        }
        val clique = graph.findMaximalClique()!!
        return clique.asSequence()
            .sorted()
            .joinToString(separator = ",")
    }
}
