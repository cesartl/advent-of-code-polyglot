package com.ctl.aoc.kotlin.y2022

import kotlin.math.absoluteValue

object Day10 {

    sealed class Instruction {
        abstract fun offsetSequence(): Sequence<Int>

        data class Add(val n: Int) : Instruction() {
            override fun offsetSequence(): Sequence<Int> = sequenceOf(0, n)
        }

        object Noop : Instruction() {
            override fun offsetSequence(): Sequence<Int> = sequenceOf(0)
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val ticks = generateSequence(20) { 40 }
            .take(6)
            .flatMap { n -> tickAfter(n) }

        val xValues = input
            .map { it.toInstruction() }
            .flatMap { it.offsetSequence() }
            .runningFold(1) { acc, i -> acc + i }

        return xValues
            .withIndex()
            .zip(ticks)
            .map { (state, tick) ->
                (state.index + 1) * state.value * tick
            }
            .sum()
    }

    fun solve2(input: Sequence<String>, lit: String = "#", dark: String = "."): String {
        val xValues = input
            .map { it.toInstruction() }
            .flatMap { it.offsetSequence() }
            .runningFold(1) { acc, i -> acc + i }

        return xValues
            .zip(crt())
            .map { (x, sprite) ->
                if ((x - sprite).absoluteValue <= 1) {
                    lit
                } else dark
            }
            .chunked(40)
            .map { it.joinToString("") }
            .joinToString(separator = "\n")
    }

    private fun String.toInstruction(): Instruction {
        return if (this == "noop") {
            Instruction.Noop
        } else {
            val n = this.split(" ")[1].toInt()
            return Instruction.Add(n)
        }
    }

    private fun tickAfter(n: Int): Sequence<Int> = sequence {
        repeat(n - 1) {
            yield(0)
        }
        yield(1)
    }


    private fun crt(width: Int = 40, height: Int = 6): Sequence<Int> =
        generateSequence(0) { (it + 1) % width }.take(width * height)
}
