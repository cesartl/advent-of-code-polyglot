package com.ctl.aoc.kotlin.utils

import com.ctl.aoc.util.FibonacciHeap
import com.ctl.aoc.util.MinPriorityQueue

typealias NodeGenerator<T> = (T) -> Sequence<T>
typealias Distance<T> = (T, T) -> Long

data class PathingResult<T>(val steps: Map<T, Long>, val previous: Map<T, T>, val lastNode: T? = null)

sealed class Constraint<T>

data class StepConstraint<T>(val maxSteps: Long) : Constraint<T>()
data class CustomConstraint<T>(val f: (T, steps: Map<T, Long>) -> Boolean) : Constraint<T>()

fun <T> findPath(end: T, prev: Map<T, T>): List<T> {
    return sequence<T> {
        var current: T? = end
        while (current != null) {
            yield(current)
            current = prev[current]
        }
    }.toList().reversed()
}

fun <T> PathingResult<T>.findPath(end: T): List<T> = findPath(end, this.previous)

object Dijkstra {

    fun <T> traverse(start: T, end: T?, nodeGenerator: NodeGenerator<T>, distance: Distance<T>, constraints: List<Constraint<T>> = listOf(), queue: MinPriorityQueue<T> = FibonacciHeap(), heuristic: (T) -> Long = { 0 }): PathingResult<T> {
        var count = 0
        val steps = mutableMapOf<T, Long>()
        val prevs = mutableMapOf<T, T>()

        val visited = mutableSetOf<T>()

        steps[start] = 0
        queue.insert(start, 0)

        var current: T? = null

        while (!queue.isEmpty && (end == null || current != end) && constraintsMet(current, steps, constraints)) {
            count + 1
            current = queue.extractMinimum()!!
            nodeGenerator(current).filter { !visited.contains(it) }.forEach { n ->
                if (!queue.contains(n)) {
                    queue.insert(n, Long.MAX_VALUE)
                }
                val stepsToCurrent = steps[current] ?: 0
                val alt = stepsToCurrent + distance(current, n) // new distance to reach n
                if (steps[n]?.let { alt < it } != false) {
                    // the new distance is better
                    steps[n] = alt
                    prevs[n] = current
                    queue.decreasePriority(n, alt + heuristic(n))
                }
            }
            visited.add(current)
        }
        return PathingResult(steps, prevs, current)
    }

    private fun <T> isConstraintMet(current: T, steps: Map<T, Long>, constraint: Constraint<T>): Boolean = when (constraint) {
        is StepConstraint<*> -> (steps[current] ?: 0) < constraint.maxSteps
        is CustomConstraint -> constraint.f(current, steps)
    }

    private fun <T> constraintsMet(current: T?, steps: Map<T, Long>, constraints: List<Constraint<T>>): Boolean = current?.let { node ->
        constraints.all { constraint -> isConstraintMet(node, steps, constraint) }
    } ?: true
}

fun <T> Graph<T>.dijkstra(start: T, end: T?, distance: Distance<T> = { _, _ -> 1L }, constraints: List<Constraint<T>> = listOf()): PathingResult<T> = Dijkstra.traverse(
        start,
        end, {
    this.outgoingNodes(it).asSequence()
},
        distance,
        constraints
)