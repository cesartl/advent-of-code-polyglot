package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Graph
import com.ctl.aoc.kotlin.utils.dijkstra
import com.ctl.aoc.kotlin.utils.findPath
import java.util.*

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

    data class Node(val name: String, val parent: Node? = null)

    tailrec fun Node.pathToRoot(acc: MutableList<Node> = mutableListOf()): List<Node> {
        acc.add(this)
        if (this.parent == null) {
            return acc
        }
        return parent.pathToRoot(acc)
    }

    fun solve1Bis(lines: Sequence<String>): Int {
        val nodeMap = computeNodeMap(lines)
        val distances = mutableMapOf<String, Int>("COM" to 0)
        nodeMap.values.forEach { computeDistances(distances, it) }
        return distances.values.sum()
    }

    private fun computeNodeMap(lines: Sequence<String>): MutableMap<String, Node> {
        val queue: Deque<Pair<String, String>> = ArrayDeque()
        lines.map { it.split(")") }.map { it[0] to it[1] }.forEach { queue.add(it) }
        val nodeMap = mutableMapOf<String, Node>()
        nodeMap["COM"] = Node("COM")

        while (queue.isNotEmpty()) {
            val (to, from) = queue.removeFirst()
            val parent = nodeMap[to]
            if (parent != null) {
                nodeMap[from] = Node(from, parent)
            } else {
                queue.addLast((from to to))
            }
        }
        return nodeMap
    }

    fun solve2Bis(lines: Sequence<String>): Int {
        val nodeMap = computeNodeMap(lines)
        val path1 = nodeMap["YOU"]!!.pathToRoot().reversed()
        val path2 = nodeMap["SAN"]!!.pathToRoot().reversed()

        var i = 0
        while (path1[i] == path2[i]) {
            i++
        }
        return (path1.size -1) + (path2.size - 1) - 2 * (i - 1) - 2
    }

    private fun computeDistances(distances: MutableMap<String, Int>, node: Node) {
        if (node.parent != null) {
            if (!distances.containsKey(node.parent.name)) {
                computeDistances(distances, node.parent)
            }
            val parent = distances[node.parent.name]!!
            distances[node.name] = parent + 1
        }
    }

    fun solve2(lines: Sequence<String>): Int {
        val graph = Graph<String>()
        lines.map { it.split(")") }.map { it[0] to it[1] }.forEach { (from, to) ->
            graph.addEdge(from, to)
        }
        val (steps, previous) = graph.dijkstra("YOU", "SAN")

        return findPath("SAN", previous).size - 3
    }

}