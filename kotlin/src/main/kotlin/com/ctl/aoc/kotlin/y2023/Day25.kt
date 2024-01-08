package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Graph
import com.ctl.aoc.kotlin.utils.Queue
import com.ctl.aoc.kotlin.utils.findPath

object Day25 {
    fun solve1(input: Sequence<String>): Int {
        val graph = Graph<String>()
        input.forEach { l ->
            val (from, tos) = l.split(":").map { it.trim() }
            tos.split(" ").map { it.trim() }.forEach { to ->
                graph.addEdge(from, to)
            }
        }


        val threshold = 11
        val f1 = graph.traversal(startNode = graph.adjacencyMap.keys.first(), storage = Queue())
            .flatMap { getAllEdgesInPaths(it, threshold, graph) }
            .groupingBy { it }
            .eachCount()

        val edges = f1.entries
            .asSequence()
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key.toList() }
            .toList()
        edges.forEach { (left, right) ->
            graph.removeEdge(left, right)
            graph.removeEdge(right, left)
        }

        return edges
            .flatten()
            .map { graph.traversal(it, storage = Queue()).count() }
            .distinct()
            .fold(1) { acc, i -> i * acc }
    }

    private fun getAllEdgesInPaths(start: String, threshold: Int, graph: Graph<String>): Sequence<Set<String>> {
        val r = Dijkstra.traverse(
            start = start,
            end = null,
            nodeGenerator = {
                graph.outgoingNodes(it).asSequence()
            },
            distance = { _, _ -> 1 }
        )
        return r.steps.entries
            .asSequence()
            .filter { it.value > threshold }
            .flatMap { r.findPath(it.key).zipWithNext().map { (a, b) -> setOf(a, b) } }
    }
}
