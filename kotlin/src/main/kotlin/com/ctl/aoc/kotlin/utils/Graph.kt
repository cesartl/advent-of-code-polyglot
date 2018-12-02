package com.ctl.aoc.kotlin.utils

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet


interface Storage<T> {
    fun hasNext(): Boolean
    fun pop(): T
    fun push(t: T)
}


class Stack<T> : Storage<T> {

    private val deque: Deque<T> = ArrayDeque()

    override fun hasNext(): Boolean = deque.isNotEmpty()

    override fun pop(): T = deque.pop()

    override fun push(t: T) = deque.push(t)

}

class Queue<T> : Storage<T> {

    private val deque: Deque<T> = ArrayDeque()

    override fun hasNext(): Boolean = deque.isNotEmpty()

    override fun pop(): T = deque.removeLast()

    override fun push(t: T) = deque.push(t)

}

class Graph<T> {
    private val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()
    private val incomingMap: HashMap<T, HashSet<T>> = HashMap()

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

    fun traversal(startNode: T, storage: Storage<T>, index: (node: T) -> String = { it.toString() }): Sequence<T> {
        storage.push(startNode)
        val visited = mutableSetOf<String>()
        return sequence {
            var current: T
            while (storage.hasNext()) {
                current = storage.pop()
                if (!visited.contains(index(current))) {
                    println("Doing $current")
                    yield(current)
                    visited.add(index(current))
                    adjacencyMap[current]?.forEach { storage.push(it) }
                }
            }
        }
    }

    fun bfs(startNode: T, index: (node: T) -> String = { it.toString() }) = traversal(startNode, Queue())
    fun dfs(startNode: T, index: (node: T) -> String = { it.toString() }) = traversal(startNode, Stack())
}

