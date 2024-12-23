package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.parseGrid

object Day21 {

    private val numericKeypadGrid = """789
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

    private val directionalKeypadGrid = """ ^A
        |<v>
    """.trimMargin().lineSequence().run {
        parseGrid(this) {
            when (it) {
                ' ' -> null
                else -> it
            }
        }
    }

    private val directionalKeyPad: Map<Char, Position> = directionalKeypadGrid
        .map
        .entries
        .associate { it.value to it.key }

    fun solve1(input: Sequence<String>): Int {
        println(buildFullSequence("233A"))
        return input.sumOf { code ->
            buildFullSequence(code)
        }
    }

    private fun buildFullSequence(code: String): Int {
        val numeric = buildNumericalPadSequence(code)
        val directional1 = buildDirectionalKeyPad(numeric)
        val directional2 = buildDirectionalKeyPad(directional1)
        val codeValue = code.dropLast(1).toInt()
        println("$codeValue -> ${directional2.length}")
        return codeValue * directional2.length
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }

    private fun buildNumericalPadSequence(code: String): String {
        return "A$code".asSequence()
            .zipWithNext()
            .map { (a, b) ->
                val from = numericKeyPad[a]!!
                val to = numericKeyPad[b]!!
                val diff = to - from
                val vFirst = from.y == 3 && to.x == 0
                if(vFirst){
                    println("VFirst $a -> $b")
                }
                buildInstructions(diff, vFirst)
            }
            .joinToString(separator = "")
    }

    private fun buildDirectionalKeyPad(instructions: String): String {
        return "A$instructions".asSequence()
            .zipWithNext()
            .mapIndexed { i, (a, b) ->
                val from = directionalKeyPad[a]!!
                val to = directionalKeyPad[b]!!
                val diff = to - from
                val vFirst = from.y == 0 && b == '<'
                if(vFirst){
                    println("VFirst $a -> $b")
                }
                buildInstructions(diff, true)
            }
            .joinToString(separator = "")
    }

    private fun buildInstructions(diff: Position, vFirst: Boolean = false): String {
        val (dx, dy) = diff
        val h = when {
            dx > 0 -> ">".repeat(dx)
            else -> "<".repeat(-dx)
        }
        val v = when {
            dy > 0 -> "v".repeat(dy)
            else -> "^".repeat(-dy)
        }
        return if (vFirst) "$v${h}A" else "$h${v}A"
    }
}
