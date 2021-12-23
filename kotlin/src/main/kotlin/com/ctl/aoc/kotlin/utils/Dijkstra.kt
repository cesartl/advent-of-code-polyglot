package com.ctl.aoc.kotlin.utils

import com.ctl.aoc.util.JavaPriorityQueue
import com.ctl.aoc.util.JavaPriorityQueueInt
import com.ctl.aoc.util.MinPriorityQueue
import com.ctl.aoc.util.MinPriorityQueueInt

typealias NodeGenerator<T> = (T) -> Sequence<T>
typealias Distance<T> = (T, T) -> Long
typealias DistanceInt<T> = (T, T) -> Int

data class PathingResult<T>(val steps: Map<T, Long>, val previous: Map<T, T>, val lastNode: T? = null)
data class PathingResultInt<T>(val steps: Map<T, Int>, val previous: Map<T, T>, val lastNode: T? = null)

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
fun <T> PathingResultInt<T>.findPath(end: T): List<T> = findPath(end, this.previous)

object Dijkstra {

    fun <T> traverse(
        start: T,
        end: T?,
        nodeGenerator: NodeGenerator<T>,
        distance: Distance<T>,
        constraints: List<Constraint<T>> = listOf(),
        queue: MinPriorityQueue<T> = JavaPriorityQueue(),
        heuristic: (T) -> Long = { 0 },
        debug: Boolean = false
    ): PathingResult<T> {
        val steps = mutableMapOf<T, Long>()
        val prevs = mutableMapOf<T, T>()

        val visited = mutableSetOf<T>()

        steps[start] = 0
        queue.insert(start, 0)

        var current: T? = null

        while (!queue.isEmpty && (end == null || current != end) && constraintsMet(current, steps, constraints)) {
            if (debug) {
                if (steps.size % 1000 == 0) {
                    println("steps: ${steps.size}")
                }
            }
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

    fun <T> traverseInt(
        start: T,
        end: T?,
        nodeGenerator: NodeGenerator<T>,
        distance: DistanceInt<T>,
        queue: MinPriorityQueueInt<T> = JavaPriorityQueueInt(),
        heuristic: (T) -> Int = { 0 }
    ): PathingResultInt<T> {
        val steps = mutableMapOf<T, Int>()
        val prevs = mutableMapOf<T, T>()

        val visited = mutableSetOf<T>()

        steps[start] = 0
        queue.insert(start, 0)

        var current: T? = null

        while (!queue.isEmpty && (end == null || current != end)) {
            current = queue.extractMinimum()!!
            nodeGenerator(current).filter { !visited.contains(it) }.forEach { n ->
                if (!queue.contains(n)) {
                    queue.insert(n, Int.MAX_VALUE)
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
        return PathingResultInt(steps, prevs, current)
    }

    private fun <T> isConstraintMet(current: T, steps: Map<T, Long>, constraint: Constraint<T>): Boolean =
        when (constraint) {
            is StepConstraint<*> -> (steps[current] ?: 0) < constraint.maxSteps
            is CustomConstraint -> constraint.f(current, steps)
        }

    private fun <T> constraintsMet(current: T?, steps: Map<T, Long>, constraints: List<Constraint<T>>): Boolean =
        current?.let { node ->
            constraints.all { constraint -> isConstraintMet(node, steps, constraint) }
        } ?: true
}

fun <T> Graph<T>.dijkstra(
    start: T,
    end: T?,
    distance: Distance<T> = { _, _ -> 1L },
    constraints: List<Constraint<T>> = listOf()
): PathingResult<T> = Dijkstra.traverse(
    start,
    end, {
        this.outgoingNodes(it).asSequence()
    },
    distance,
    constraints
)