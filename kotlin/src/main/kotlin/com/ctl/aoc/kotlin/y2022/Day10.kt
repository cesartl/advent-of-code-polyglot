package com.ctl.aoc.kotlin.y2022

import kotlin.math.absoluteValue

object Day10 {

    sealed class Instruction {

        abstract val cycles: Int

        abstract fun offsetSequence(): Sequence<Int>

        data class Add(val n: Int) : Instruction() {
            override val cycles: Int
                get() = 2

            override fun offsetSequence(): Sequence<Int> = sequenceOf(0, n)
        }

        object Noop : Instruction() {
            override val cycles: Int
                get() = 1

            override fun offsetSequence(): Sequence<Int> = sequenceOf(0)
        }
    }

    private fun String.toInstruction(): Instruction {
        return if (this == "noop") {
            Instruction.Noop
        } else {
            val n = this.split(" ")[1].toInt()
            return Instruction.Add(n)
        }
    }

    data class CpuState(val x: Int = 1, val tick: Int = 0) {
        fun runInstruction(instruction: Instruction): CpuState {
            val x = when (instruction) {
                is Instruction.Add -> this.x + instruction.n
                Instruction.Noop -> this.x
            }
            return this.copy(x = x, tick = this.tick + instruction.cycles)
        }
    }

    fun CpuState.runInstructions(instructions: Sequence<Instruction>): Sequence<CpuState> {
        return instructions.runningFold(this) { state, instruction -> state.runInstruction(instruction) }
    }

    fun solve1(input: Sequence<String>): Int {
        val state = CpuState()
        val states = state.runInstructions(input.map { it.toInstruction() })
            .map { it.tick to it }
            .toMap()
        val strengths = generateSequence(20) { it + 40 }.take(6)
            .map { states.getXat(it - 1) * it }
            .toList()
        println(strengths)
        return strengths.sum()
    }

    private fun tickAfter(n: Int): Sequence<Int> = sequence {
        repeat(n - 1) {
            yield(0)
        }
        yield(1)
    }

    fun solve1Bis(input: Sequence<String>): Int {
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

    fun Map<Int, CpuState>.getXat(cycle: Int): Int {
        return (this[cycle] ?: this[cycle - 1]!!).x
    }

    fun solve2(input: Sequence<String>) {
        val state = CpuState()
        val states = state.runInstructions(input.map { it.toInstruction() })
            .map { it.tick to it }
            .toMap()
        generateSequence(0) { it + 1 }
            .take(40 * 6)
            .map { tick ->
                val position = tick % 40
                val x = states.getXat(tick)
                if (x in (position - 1..position + 1)) {
                    "#"
                } else " "
            }
            .chunked(40)
            .forEach { println(it.joinToString("")) }
    }

    private fun crt(width: Int = 40, height: Int = 6): Sequence<Int> =
        generateSequence(0) { (it + 1) % width }.take(width * height)

    fun solve2Bis(input: Sequence<String>, lit: String = "#", dark: String = "."): String {
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
}
