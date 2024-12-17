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
data class MutliPathingResultInt<T>(val steps: Map<T, Int>, val previous: Map<T, Set<T>>, val lastNode: T? = null)

sealed class Constraint<T>
data class StepConstraint<T>(val maxSteps: Long) : Constraint<T>()
data class CustomConstraint<T>(val f: (T, steps: Map<T, Long>) -> Boolean) : Constraint<T>()

sealed class ConstraintInt<T>
data class StepConstraintInt<T>(val maxSteps: Int) : ConstraintInt<T>()
data class CustomConstraintInt<T>(val f: (T, steps: Map<T, Int>) -> Boolean) : ConstraintInt<T>()

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

fun <T> MutliPathingResultInt<T>.findPaths(end: T): Sequence<List<T>> {
    return findPaths(this, end)
}

private fun <T> MutliPathingResultInt<T>.findPaths(
    result: MutliPathingResultInt<T>,
    end: T
): Sequence<List<T>> {
    return sequence {
        val previous = result.previous[end]
        if (previous != null) {
            previous.asSequence()?.forEach {
                yieldAll(findPaths(it).map { p -> p + end })
            }
        } else {
            yield(listOf(end))
        }
    }
}

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
        constraints: List<ConstraintInt<T>> = listOf(),
        queue: MinPriorityQueueInt<T> = JavaPriorityQueueInt(),
        heuristic: (T) -> Int = { 0 }
    ): PathingResultInt<T> {
        return traverseIntPredicate(
            start,
            { c: T? -> end?.let { it == c } ?: false },
            nodeGenerator,
            distance,
            constraints,
            queue,
            heuristic
        )
    }

    fun <T> traverseIntPredicate(
        start: T,
        end: (T?) -> Boolean,
        nodeGenerator: NodeGenerator<T>,
        distance: DistanceInt<T>,
        constraints: List<ConstraintInt<T>> = listOf(),
        queue: MinPriorityQueueInt<T> = JavaPriorityQueueInt(),
        heuristic: (T) -> Int = { 0 }
    ): PathingResultInt<T> {
        val steps = mutableMapOf<T, Int>()
        val prevs = mutableMapOf<T, T>()

        val visited = mutableSetOf<T>()

        steps[start] = 0
        queue.insert(start, 0)

        var current: T? = null

        while (!queue.isEmpty && (!end(current)) && constraintsMetInt(current, steps, constraints)) {
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

    fun <T> traverseMultiIntPredicate(
        start: T,
        end: (T?) -> Boolean,
        nodeGenerator: NodeGenerator<T>,
        distance: DistanceInt<T>,
        constraints: List<ConstraintInt<T>> = listOf(),
        queue: MinPriorityQueueInt<T> = JavaPriorityQueueInt(),
        heuristic: (T) -> Int = { 0 }
    ): MutliPathingResultInt<T> {
        val steps = mutableMapOf<T, Int>()
        val prevs = mutableMapOf<T, Set<T>>()

        val visited = mutableSetOf<T>()

        steps[start] = 0
        queue.insert(start, 0)

        var current: T? = null

        while (!queue.isEmpty && (!end(current)) && constraintsMetInt(current, steps, constraints)) {
            current = queue.extractMinimum()!!
            nodeGenerator(current).filter { !visited.contains(it) }.forEach { n ->
                if (!queue.contains(n)) {
                    queue.insert(n, Int.MAX_VALUE)
                }
                val stepsToCurrent = steps[current] ?: 0
                val alt = stepsToCurrent + distance(current, n) // new distance to reach n
                val existing = steps[n] ?: Int.MAX_VALUE
                if (alt < existing) {
                    steps[n] = alt
                    prevs[n] = setOf(current)
                    queue.decreasePriority(n, alt + heuristic(n))
                } else if (alt == existing) {
                    prevs[n] = prevs[n]!! + current
                }
            }
            visited.add(current)
        }
        return MutliPathingResultInt(steps, prevs, current)
    }


    private fun <T> isConstraintMet(current: T, steps: Map<T, Long>, constraint: Constraint<T>): Boolean =
        when (constraint) {
            is StepConstraint<*> -> (steps[current] ?: 0) < constraint.maxSteps
            is CustomConstraint -> constraint.f(current, steps)
        }

    private fun <T> isConstraintMetInt(current: T, steps: Map<T, Int>, constraint: ConstraintInt<T>): Boolean =
        when (constraint) {
            is StepConstraintInt<*> -> (steps[current] ?: 0) < constraint.maxSteps
            is CustomConstraintInt -> constraint.f(current, steps)
        }

    private fun <T> constraintsMet(current: T?, steps: Map<T, Long>, constraints: List<Constraint<T>>): Boolean =
        current?.let { node ->
            constraints.all { constraint -> isConstraintMet(node, steps, constraint) }
        } ?: true

    private fun <T> constraintsMetInt(current: T?, steps: Map<T, Int>, constraints: List<ConstraintInt<T>>): Boolean =
        current?.let { node ->
            constraints.all { constraint -> isConstraintMetInt(node, steps, constraint) }
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
