package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.lcm
import java.math.BigInteger

data class HauntedMap(
    val directions: String,
    val nodes: Map<String, Pair<String, String>>
) {
    fun directions(): Sequence<Char> = generateSequence(directions) { it }
        .flatMap { it.asSequence() }

    fun next(current: String, direction: Char): String {
        val node = nodes[current] ?: error("No nodes from $current")
        return when (direction) {
            'L' -> node.first
            'R' -> node.second
            else -> error("Unknown direction $direction")
        }
    }
}

private val regex = """(\w+) = \((\w+), (\w+)\)""".toRegex()
private fun parseHauntedMap(input: Sequence<String>): HauntedMap {
    val lines = input.filter { it.isNotEmpty() }.toList()
    val direction = lines[0]
    val nodes = mutableMapOf<String, Pair<String, String>>()
    lines.asSequence()
        .drop(1)
        .forEach { line ->
            val m = regex.matchEntire(line) ?: error(line)
            val values = m.groupValues
            nodes[values[1]] = values[2] to values[3]
        }
    return HauntedMap(
        directions = direction,
        nodes = nodes
    )
}

private fun HauntedMap.walk(start: String): Sequence<String> {
    return directions().runningFold(start) { current, direction -> next(current, direction) }
}

private fun HauntedMap.walk(starts: List<String>): Sequence<List<String>> {
    return directions().runningFold(starts) { current, direction -> current.map { next(it, direction) } }
}

fun HauntedMap.cycles(start: String): Sequence<Pair<Int, String>> {
    return walk(start)
        .withIndex()
        .filter { it.value.endsWith("A") || it.value.endsWith("Z") }
        .zipWithNext { a, b -> (b.index - a.index) to b.value }
}

fun HauntedMap.cycle(start: String): BigInteger {
    return cycles(start)
        .first()
        .first
        .toBigInteger()
}

object Day8 {
    fun solve1(input: Sequence<String>): Int {
        val map = parseHauntedMap(input)
        return map.walk("AAA")
            .withIndex()
            .first { it.value == "ZZZ" }
            .index
    }

    fun solve2(input: Sequence<String>): BigInteger {
        val map = parseHauntedMap(input)
        val starts = map.nodes.keys.filter { it.endsWith("A") }
        val cycles = starts
            .asSequence()
            .map { map.cycle(it) }
        return lcm(cycles)
    }
}
