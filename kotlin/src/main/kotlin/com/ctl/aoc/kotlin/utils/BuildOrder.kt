package com.ctl.aoc.kotlin.utils

object BuildOrder {

    fun <T> buildOrder(nodes: List<T>, dependencies: List<Pair<T, T>>): List<T> {
        val g: Graph<T> = Graph()
        dependencies.forEach { g.addDirectedEdge(it.first, it.second) }

        var remaining = nodes
        val result = mutableListOf<T>()

        while (remaining.isNotEmpty()) {
            val (readyToBuild, other) = remaining.partition { g.incomingNodes(it).isEmpty() }
            result.addAll(readyToBuild)
            readyToBuild.forEach { g.removeAllOutgoing(it) }
            if (other.size >= remaining.size) {
                throw IllegalAccessException("cyclical")
            }
            remaining = other
        }
        return result.toList()
    }

}