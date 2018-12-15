package com.ctl.aoc.kotlin.utils

import com.ctl.aoc.util.FibonacciHeap
import com.ctl.aoc.util.MinPriorityQueue
import java.nio.file.Path

typealias NodeGenerator<T> = (T) -> Sequence<T>
typealias Distance<T> = (T, T) -> Long

data class PathingResult<T>(val steps: Map<T, Long>, val previous: Map<T, T>)

sealed class Constraint

data class StepConstraint(val maxSteps: Long) : Constraint()


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

    fun <T> traverse(start: T, end: T?, nodeGenerator: NodeGenerator<T>, distance: Distance<T>, constraints: List<Constraint>, queue: MinPriorityQueue<T> = FibonacciHeap()): PathingResult<T> {
        var count = 0
        val steps = mutableMapOf<T, Long>()
        val prevs = mutableMapOf<T, T>()

        val visited = mutableSetOf<T>()

        steps[start] = 0
        queue.insert(start, 0)

        var current: T? = null

        while (!queue.isEmpty && (end == null || current != end) && constraintsNotMet(current, steps, constraints)) {
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
                    queue.decreasePriority(n, alt)
                }
            }
            visited.add(current)
        }
        return PathingResult(steps, prevs)
    }

    private fun <T> isConstraintMet(current: T, steps: Map<T, Long>, constraint: Constraint): Boolean = when (constraint) {
        is StepConstraint -> (steps[current] ?: 0) < constraint.maxSteps
    }

    private fun <T> constraintsNotMet(current: T?, steps: Map<T, Long>, constraints: List<Constraint>): Boolean = current?.let { node ->
        constraints.all { constraint -> isConstraintMet(node, steps, constraint) }
    } ?: true
}

fun <T> Graph<T>.dijkstra(start: T, end: T?, distance: Distance<T> = { _, _ -> 1L }, constraints: List<Constraint> = listOf()): PathingResult<T> = Dijkstra.traverse(
        start,
        end, {
    this.outgoingNodes(it).asSequence()
},
        distance,
        constraints
)