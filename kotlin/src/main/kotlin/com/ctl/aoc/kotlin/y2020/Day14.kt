package com.ctl.aoc.kotlin.y2020

import java.math.BigInteger

object Day14 {

    sealed class Instruction {
        data class Mem(val address: BigInteger, val value: BigInteger) : Instruction()
        data class Mask(val mask: List<Boolean?>) : Instruction()

        companion object {
            val memRegex = """mem\[(\d+)\] = (\d+)""".toRegex()
            fun parse(s: String): Instruction {
                val memMatch = memRegex.matchEntire(s)
                if (memMatch != null) {
                    return Mem(memMatch.groupValues[1].toBigInteger(), memMatch.groupValues[2].toBigInteger())
                } else {
                    val maskString = s.split(" ")[2]
                    val mask = maskString.map { c ->
                        when (c) {
                            '1' -> true
                            '0' -> false
                            else -> null
                        }
                    }
                    return Mask(mask.reversed())
                }
            }
        }
    }

    fun generateFloatingMasks(mask: List<Boolean?>): Sequence<List<Boolean>> {
        val floatingIndices = mask.mapIndexed { i, b -> if (b == null) i else null }.filterNotNull()
        return possibilities(floatingIndices, mapOf())
                .map { changes ->
                    mask.foldIndexed(listOf<Boolean>()) { i, acc, current ->
                        val changed = if (current == null) {
                            changes[i] ?: throw Error("Odd")
                        } else {
                            current
                        }
                        acc + changed
                    }
                }
    }

    private fun possibilities(indices: List<Int>, current: Map<Int, Boolean>): Sequence<Map<Int, Boolean>> {
        if (indices.isEmpty()) {
            return sequenceOf(current)
        } else {
            val first = indices.first()
            return sequence {
                yieldAll(possibilities(indices.drop(1), current + (first to true)))
                yieldAll(possibilities(indices.drop(1), current + (first to false)))
            }
        }
    }

    data class State(val memories: Map<BigInteger, BigInteger> = mapOf(), val mask: List<Boolean?> = listOf())

    fun State.apply(instruction: Instruction): State {
        return when (instruction) {
            is Instruction.Mask -> this.copy(mask = instruction.mask)
            is Instruction.Mem -> {
                val newValue = mask.foldIndexed(instruction.value) { i, n, m ->
                    when (m) {
                        true -> {
                            n.setBit(i)
                        }
                        false -> {
                            n.clearBit(i)
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

    fun State.apply2(instruction: Instruction): State {
        return when (instruction) {
            is Instruction.Mask -> this.copy(mask = instruction.mask)
            is Instruction.Mem -> {
                val bits = mask.indices.map { i -> instruction.address.testBit(i) }
                val addressMask = applyMask(bits, mask)
                val generateFloatingMasks = generateFloatingMasks(addressMask)
                        .map { applyMask(instruction.address, it) }
                        .toList()
                val newMemories = generateFloatingMasks
                        .fold(mutableMapOf<BigInteger, BigInteger>()) { acc, address ->
                            acc[address] = instruction.value
                            acc
                        }
                this.copy(memories = memories + newMemories)
            }
        }
    }

    private fun applyMask(bits: List<Boolean>, mask: List<Boolean?>): List<Boolean?> {
        return bits.zip(mask).map { (b, m) ->
            when (m) {
                true -> {
                    true
                }
                false -> {
                    b
                }
                else -> {
                    null
                }
            }
        }
    }

    private fun applyMask(integer: BigInteger, mask: List<Boolean?>): BigInteger {
        return mask.foldIndexed(integer) { i, n, m ->
            when (m) {
                true -> {
                    n.setBit(i)
                }
                false -> {
                    n.clearBit(i)
                }
                else -> {
                    n
                }
            }
        }
    }

    fun solve1(input: Sequence<String>): BigInteger {
        val instructions = input.map { Instruction.parse(it) }.toList()
        val state = instructions.fold(State()) { state, instruction -> state.apply(instruction) }
        return state.memories.values.fold(BigInteger.ZERO) { acc, n -> acc + n }
    }

    fun solve2(input: Sequence<String>): BigInteger {
        val instructions = input.map { Instruction.parse(it) }.toList()
        val state = instructions.fold(State()) { state, instruction -> state.apply2(instruction) }
        return state.memories.values.fold(BigInteger.ZERO) { acc, n -> acc + n }
    }
}