package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*

data class DigInstruction(
    val orientation: Orientation,
    val amount: Int,
    val rbg: String
) {
    fun parseRGB(): DigInstruction {
        val distance = rbg.substring(0..<5).toInt(16)
        val orientation = when (rbg[5]) {
            '0' -> "R"
            '1' -> "D"
            '2' -> "L"
            '3' -> "U"
            else -> error("")
        }.parseOrientation()
        return DigInstruction(orientation, distance, rbg)
    }
}

private val regex = """([UDRL]) (\d+) \(#(\w+)\)""".toRegex()

private fun String.parseDigInstruction(): DigInstruction {
    val match = regex.matchEntire(this) ?: error(this)
    val o = match.groupValues[1].parseOrientation()
    val amount = match.groupValues[2].toInt()
    val rgb = match.groupValues[3]
    return DigInstruction(o, amount, rgb)
}

private fun String.parseOrientation(): Orientation = when (this) {
    "U" -> N
    "R" -> E
    "D" -> S
    "L" -> W
    else -> error("Invalid orientation $this")
}

object Day18 {
    fun solve1(input: Sequence<String>): Long {
        val instructions = input
            .map { it.parseDigInstruction() }
        return countInside(instructions)
    }

    fun solve2(input: Sequence<String>): Long {
        val instructions = input
            .map { it.parseDigInstruction() }
            .map { it.parseRGB() }
        return countInside(instructions)
    }

    private fun countInside(instructions: Sequence<DigInstruction>): Long {
        val corners = instructions
            .runningFold(Position(0, 0)) { acc, (o, amount) ->
                o.move(acc, amount)
            }.toList()
        val area: Long = area(corners)
        val b = instructions.fold(0L) { acc, digInstruction -> acc + digInstruction.amount }
        return area + b / 2 + 1
    }

    fun area(corners: List<Position>): Long {
        val a = corners.zipWithNext().sumOf { (a, b) ->
            (a.y + b.y) * (a.x - b.x).toLong()
        }
        return a / 2
    }
}
