package com.ctl.aoc.kotlin.y2023

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

object Day8 {
    fun solve1(input: Sequence<String>): Int {
        val map = parseHauntedMap(input)
        return map.walk("AAA")
            .withIndex()
            .first { it.value == "ZZZ" }
            .index
    }

    fun solve2(input: Sequence<String>): Int {
        val map = parseHauntedMap(input)
        TODO()
    }
}
