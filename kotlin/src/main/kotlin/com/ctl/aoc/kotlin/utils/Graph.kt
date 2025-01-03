package com.ctl.aoc.kotlin.utils

import java.util.*


interface Storage<T> {
    fun hasNext(): Boolean
    fun pop(): T
    fun push(t: T)
    fun peek(): T
}


class Stack<T> : Storage<T> {
    override fun peek(): T = deque.peekFirst()

    private val deque: Deque<T> = ArrayDeque()

    override fun hasNext(): Boolean = deque.isNotEmpty()

    override fun pop(): T = deque.pop()

    override fun push(t: T) = deque.push(t)
}

class Queue<T> : Storage<T> {

    override fun peek(): T = deque.peekLast()

    private val deque: Deque<T> = ArrayDeque()

    override fun hasNext(): Boolean = deque.isNotEmpty()

    override fun pop(): T = deque.removeLast()

    override fun push(t: T) = deque.push(t)

}

fun <T> traversal(
    startNode: T,
    storage: Storage<T>,
    index: (node: T) -> String = { it.toString() },
    nodeGenerator: NodeGenerator<T>
): Sequence<T> {
    storage.push(startNode)
    val visited = mutableSetOf<String>()
    return sequence {
        var current: T
        while (storage.hasNext()) {
            current = storage.pop()
            if (!visited.contains(index(current))) {
//                println("Doing $current")
                yield(current)
                visited.add(index(current))
                nodeGenerator(current).forEach { storage.push(it) }
            }
        }
    }
}

class Graph<T> {
    val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()
    val incomingMap: HashMap<T, HashSet<T>> = HashMap()

    fun copy(): Graph<T> {
        val copy = Graph<T>()
        adjacencyMap.forEach { (key, values) ->
            copy.adjacencyMap[key] = HashSet(values)
        }
        incomingMap.forEach { (key, values) ->
            copy.incomingMap[key] = HashSet(values)
        }
        return copy
    }


    fun outGoingSize(): Int = adjacencyMap.size

    fun addEdge(source: T, dest: T) {
        addDirectedEdge(source, dest)
        addDirectedEdge(dest, source)
    }

    fun addDirectedEdge(source: T, dest: T) {
        adjacencyMap
            .computeIfAbsent(source) { HashSet() }
            .add(dest)
        incomingMap.computeIfAbsent(dest) { HashSet() }
            .add(source)
    }

    fun removeEdge(source: T, dest: T) {
        adjacencyMap[source]?.let { it.remove(dest) }
        incomingMap[dest]?.let { it.remove(source) }
    }

    fun removeAllOutgoing(source: T) {
        val outgoings = adjacencyMap[source] ?: emptySet<T>()
        adjacencyMap[source] = HashSet()
        outgoings.forEach { incomingMap[it]!!.remove(source) }
    }

    fun outgoingNodes(source: T): Set<T> = adjacencyMap[source]?.let { it.toSet() } ?: emptySet()
    fun incomingNodes(dest: T): Set<T> = incomingMap[dest]?.let { it.toSet() } ?: emptySet()

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }.toString()

    fun describe(f: (T) -> String = { it.toString() }) {
        adjacencyMap.forEach { (from, tos) ->
            tos.forEach { to ->
                println("${f(from)} -> ${f(to)}")
            }
        }
    }

    fun traversal(startNode: T, storage: Storage<T>, index: (node: T) -> String = { it.toString() }): Sequence<T> {
        return traversal(startNode, storage, index) { adjacencyMap[it]?.asSequence() ?: emptySequence() }
    }

    private data class NodeWithDistance<T>(val node: T, val distance: Int)

    fun neighboursWithin(start: T, distance: Int): Sequence<T> = sequence {
        val visited = mutableSetOf<T>()
        val queue = ArrayDeque<NodeWithDistance<T>>()
        queue.add(NodeWithDistance(start, 0))
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (!visited.contains(current.node) && current.distance < distance) {
                yield(current.node)
                visited.add(current.node)
                outgoingNodes(current.node).forEach {
                    queue.add(NodeWithDistance(it, current.distance + 1))
                }
                incomingNodes(current.node).forEach {
                    queue.add(NodeWithDistance(it, current.distance + 1))
                }
            }
        }
    }

    fun bfs(startNode: T, index: (node: T) -> String = { it.toString() }) = traversal(startNode, Queue(), index)
    fun dfs(startNode: T, index: (node: T) -> String = { it.toString() }) = traversal(startNode, Stack(), index)

    fun findMaximalClique(): Set<T>? {
        val cliques = mutableListOf<Set<T>>()
        bronKerbosch2(mutableSetOf(), adjacencyMap.keys.toMutableSet(), mutableSetOf(), cliques)
        return cliques.maxBy { it.size }
    }

    private fun bronKerbosch2(
        r: MutableSet<T>,  // Current clique
        p: MutableSet<T>,  // Potential candidates
        x: MutableSet<T>,  // Already processed vertices
        cliques: MutableList<Set<T>>  // Resultant cliques
    ) {
        if (p.isEmpty() && x.isEmpty()) {
            cliques.add(r) // Found a maximal clique
            return
        }

        val pIter = p.toSet() // To prevent modifying the original set during iteration
        for (v in pIter) {
            val neighbors = adjacencyMap[v]?.toSet() ?: emptySet()
            bronKerbosch2(
                (r + v).toMutableSet(),
                p.intersect(neighbors).toMutableSet(),
                x.intersect(neighbors).toMutableSet(),
                cliques
            )
            // Move vertex v from P to X
            p -= v
            x += v
        }
    }
}



