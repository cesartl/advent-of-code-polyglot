package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.y2023.LongPosition

object Day13 {

    data class ClawMachine(
        val a: LongPosition,
        val b: LongPosition,
        val prize: LongPosition
    )

    fun solve1(input: String): Long {
        val machines = parse(input)
        return machines.mapNotNull { winPrice(it) }.sum()
    }


    private fun winPrice(machine: ClawMachine): Long? {
        val (a, b, prize) = machine
        val (X, Y) = prize
        val bottom = b.y * a.x - b.x * a.y
        if (bottom == 0L) {
            return null
        }
        val top = Y * a.x - X * a.y
        if (top % bottom != 0L) {
            return null
        }
        val B = top / bottom


        val a1 = X - B * b.x
        if(a1 % a.x != 0L){
            return null
        }
        val A = a1 / a.x
        return 3 * A + B
    }

    fun solve2(input: String): Long {
        val machines = parse(input)
        val extra = 10000000000000L
        val results = machines
            .map { it.copy(prize = LongPosition(it.prize.x + extra, it.prize.y + extra, 0)) }
            .map { winPrice(it) }
            .toList()
        return results.asSequence().filterNotNull().sum()
    }

    private val regex = """X([+\-0-9]+), Y([+\-0-9]+)""".toRegex()
    private val regex2 = """X=([-0-9]+), Y=([+0-9]+)""".toRegex()

    private fun parsePosition(input: String): LongPosition {
        val (x, y) = (regex.matchEntire(input) ?: error("Not valid: $input")).destructured
        return LongPosition(x.toLong(), y.toLong(), 0L)
    }

    private fun parsePrize(input: String): LongPosition {
        val (x, y) = (regex2.matchEntire(input) ?: error("Not valid: $input")).destructured
        return LongPosition(x.toLong(), y.toLong(), 0L)
    }

    private fun parse(input: String): Sequence<ClawMachine> {
        return input.splitToSequence("\n\n").map { block ->
            val lines = block.trim().lines()
            val a = parsePosition(lines[0].substringAfter("Button A: "))
            val b = parsePosition(lines[1].substringAfter("Button B: "))
            val prize = parsePrize(lines[2].substringAfter("Prize: "))
            ClawMachine(a, b, prize)
        }
    }
}
