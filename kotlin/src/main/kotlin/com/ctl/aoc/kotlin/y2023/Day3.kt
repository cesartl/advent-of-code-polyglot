package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.Position

data class EngineNumber(
    val start: Position,
    val id: Int
)

data class EngineMap(
    val numbers: Map<Position, EngineNumber>,
    val symbols: Map<Position, String>
)


private val numberRegex = """(\d+)""".toRegex()
private val symbolRegex = """([^\\.\d])""".toRegex()
private fun buildEngineMap(lines: Sequence<String>): EngineMap {
    val numbers: MutableMap<Position, EngineNumber> = mutableMapOf()
    val symbols: MutableMap<Position, String> = mutableMapOf()

    lines.forEachIndexed { y, line ->
        numberRegex.findAll(line).forEach { match ->
            val number = match.value.toInt()
            match.groups.filterNotNull().first().range.forEach { x ->
                numbers[Position(x, y)] = EngineNumber(Position(match.range.first, y), number)
            }
        }
        symbolRegex.findAll(line).forEach { match ->
            val x = match.range.first
            symbols[Position(x, y)] = match.groupValues[0]
        }
    }

    return EngineMap(numbers, symbols)
}

object Day3 {
    fun solve1(input: Sequence<String>): Int {
        val engineMap = buildEngineMap(input)
        val engineParts = mutableSetOf<EngineNumber>()
        engineMap.symbols.forEach { (p, symbol) ->
            p.neighbours()
                .mapNotNull { engineMap.numbers[it] }
                .forEach { engineParts.add(it) }
        }
        return engineParts.sumOf { it.id }
    }

    fun solve2(input: Sequence<String>): Int {
        val engineMap = buildEngineMap(input)
        return engineMap
            .symbols
            .asSequence()
            .filter { (_, symbol) -> symbol == "*" }
            .mapNotNull { (position, _) ->
                val numbers = position
                    .neighbours()
                    .mapNotNull { engineMap.numbers[it] }
                    .toSet()
                if (numbers.size == 2) {
                    numbers
                } else {
                    null
                }
            }.sumOf { numbers ->
                numbers
                    .asSequence()
                    .map { number -> number.id }
                    .fold(1) { acc, i -> acc * i }
                    .toInt()
            }
    }
}
