package com.ctl.aoc.kotlin.y2020

import java.math.BigInteger

object Day14 {

    sealed class Instruction {
        data class Mem(val address: Int, val value: BigInteger) : Instruction()
        data class Mask(val mask: Map<Int, Boolean?>) : Instruction()

        companion object {
            val memRegex = """mem\[(\d+)\] = (\d+)""".toRegex()
            fun parse(s: String): Instruction {
                val memMatch = memRegex.matchEntire(s)
                if (memMatch != null) {
                    return Mem(memMatch.groupValues[1].toInt(), memMatch.groupValues[2].toBigInteger())
                } else {
                    val maskString = s.split(" ")[2]
                    val offset = maskString.length - 1
                    val mask = maskString.mapIndexed { index, c ->
                        when (c) {
                            '1' -> offset - index to true
                            '0' -> offset - index to false
                            else -> offset - index to null
                        }
                    }.toMap()
                    return Mask(mask)
                }
            }
        }
    }

    data class State(val memories: Map<Int, BigInteger> = mapOf(), val mask: Map<Int, Boolean?> = mapOf())

    fun State.apply(instruction: Instruction): State {
        return when (instruction) {
            is Instruction.Mask -> this.copy(mask = instruction.mask)
            is Instruction.Mem -> {
                val newValue = mask.entries.fold(instruction.value) { n, m ->
                    when (m.value) {
                        true -> {
                            n.setBit(m.key)
                        }
                        false -> {
                            n.clearBit(m.key)
                        }
                        else -> {
                            n
                        }
                    }
                }
                this.copy(memories = memories + (instruction.address to newValue))
            }
        }
    }

    fun solve1(input: Sequence<String>): BigInteger {
        val instructions = input.map { Instruction.parse(it) }.toList()
        val state = instructions.fold(State()) { state, instruction -> state.apply(instruction) }
        return state.memories.values.fold(BigInteger.ZERO) { acc, n -> acc + n }
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}