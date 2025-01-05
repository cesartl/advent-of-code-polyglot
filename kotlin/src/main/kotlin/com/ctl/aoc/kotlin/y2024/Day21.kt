package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Grid
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.parseGrid

object Day21 {

    private val numericKeypadGrid: Grid<Char> = """789
        |456
        |123
        | 0A
    """.trimMargin().lineSequence().run {
        parseGrid(this) {
            when (it) {
                ' ' -> null
                else -> it
            }
        }
    }

    private val numericKeyPad: Map<Char, Position> = numericKeypadGrid
        .map
        .entries
        .associate { it.value to it.key }

    private val directionalKeypadGrid: Grid<Char> = """ ^A
        |<v>
    """.trimMargin().lineSequence().run {
        parseGrid(this) {
            when (it) {
                ' ' -> null
                else -> it
            }
        }
    }

    private val numericHole = Position(0, 3)

    private val directionalKeyPad: Map<Char, Position> = directionalKeypadGrid
        .map
        .entries
        .associate { it.value to it.key }

    private val directionHole = Position(0, 0)


    fun solve1(input: Sequence<String>): Long {
        return input.sumOf { code ->
            println("Code $code")
            val best = findBest(code, 2)
            println("Best $best")
            println("cache size ${cache.size}")
            code.dropLast(1).toInt() * best
        }
    }

    fun solve2(input: Sequence<String>): Long {
        return input.sumOf { code ->
            println("Code $code")
            val best = findBest(code, 25)
            println("Best $best")
            code.dropLast(1).toInt() * best
        }.run {
            println("cache size ${cache.size}")
            this
        }
    }

    private val instructionCache: MutableMap<Pair<Char, Char>, String> = mutableMapOf()

    private fun computeInstructions(from: Char, to: Char, pad: Map<Char, Position>, hole: Position): String {
        return instructionCache.getOrPut(from to to) {
            val fromPosition = pad[from] ?: error("No position for $from")
            val toPosition = pad[to] ?: error("No position for $to")
            val diff = toPosition - fromPosition
            val (dx, dy) = diff
            val h = when {
                dx > 0 -> ">".repeat(dx)
                else -> "<".repeat(-dx)
            }
            val v = when {
                dy > 0 -> "v".repeat(dy)
                else -> "^".repeat(-dy)
            }
            val instructions = if (toPosition.y == hole.y && fromPosition.x == hole.x) {
                h + v
            } else if (toPosition.x == hole.x && fromPosition.y == hole.y) {
                v + h
            } else {
                if (diff.x < 0) {
                    h + v
                } else {
                    v + h
                }
            }
            "${instructions}A"
        }
    }

    private fun findBest(code: String, n: Int): Long {
        return "A$code"
            .asSequence()
            .zipWithNext()
            .sumOf { (from, to) ->
                val path = computeInstructions(from, to, numericKeyPad, numericHole)
                "A$path".asSequence()
                    .zipWithNext()
                    .sumOf { (from, to) ->
                        findBestDirectional(from, to, n)
                    }
            }

    }

    private val cache: MutableMap<Pair<Pair<Char, Char>, Int>, Long> = mutableMapOf()

    private fun findBestDirectional(from: Char, to: Char, n: Int): Long {
        return cache.getOrPut((from to to) to n) {
            val nextInstructions = computeInstructions(from, to, directionalKeyPad, directionHole)
            if (n == 1) {
                nextInstructions.length.toLong()
            } else {
                "A$nextInstructions".asSequence()
                    .zipWithNext()
                    .sumOf { (from, to) ->
                        findBestDirectional(from, to, n - 1)
                    }
            }
        }
    }
}
