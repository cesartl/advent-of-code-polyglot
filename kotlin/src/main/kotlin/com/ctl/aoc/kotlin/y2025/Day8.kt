package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Position3d
import com.ctl.aoc.kotlin.utils.pairs

object Day8 {
    fun solve1(input: Sequence<String>, n: Int = 10): Int {
        val positions = parsePositions(input)
        val circuits: MutableMap<Position3d, Set<Position3d>> = positions.associateWithTo(mutableMapOf()) { setOf(it) }
        val sortedPairs = positions.pairs()
            .map { it to distance(it.first, it.second) }
            .sortedBy { it.second }

        sortedPairs.take(n).forEach { (p, _) ->
            val (p1, p2) = p
            val newCircuit = circuits[p1]!! + circuits[p2]!!
            newCircuit.forEach { p ->
                circuits[p] = newCircuit
            }
        }

        val allCircuits = circuits.values.distinct()
        return allCircuits
            .asSequence()
            .sortedByDescending { it.size }
            .take(3)
            .fold(1) { acc, i -> acc * i.size }
    }

    fun distance(first: Position3d, second: Position3d): Long {
        val (x1, y1, z1) = first
        val (x2, y2, z2) = second
        return (x1 - x2) * (x1 - x2).toLong() + (y1 - y2) * (y1 - y2).toLong() + (z1 - z2) * (z1 - z2).toLong()
    }

    fun solve2(input: Sequence<String>): Int {
        val positions = parsePositions(input)
        val circuits: MutableMap<Position3d, Set<Position3d>> = positions.associateWithTo(mutableMapOf()) { setOf(it) }
        val sortedPairs = positions.pairs()
            .map { it to distance(it.first, it.second) }
            .sortedBy { it.second }

        var toConnect: Pair<Position3d, Position3d>
        val iterator = sortedPairs.iterator()
        do {
            toConnect = iterator.next().first
            val (p1, p2) = toConnect
            val newCircuit = circuits[p1]!! + circuits[p2]!!
            newCircuit.forEach { p ->
                circuits[p] = newCircuit
            }
        } while (circuits.values.any { it.size != positions.size })
        val (a, b) = toConnect
        return a.x * b.x
    }

    private fun parsePositions(input: Sequence<String>): List<Position3d> = input.map {
        val (x, y, z) = it.split(",").map { t -> t.toInt() }
        Position3d(x, y, z)
    }.toList()
}
