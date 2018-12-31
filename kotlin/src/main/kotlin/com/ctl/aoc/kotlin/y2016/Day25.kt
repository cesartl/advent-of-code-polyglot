package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.y2016.Day25.Instruction.*
import com.ctl.aoc.kotlin.y2016.Day25.Ref.Register

object Day25 {

    sealed class Ref {
        data class Register(val name: Char) : Ref()
        data class Number(val n: Long) : Ref()
        companion object {
            fun parse(s: String): Ref {
                return s.toLongOrNull()?.let { Number(it) } ?: Register(s.first())
            }
        }
    }

    sealed class Instruction {
        data class Copy(val x: Ref, val y: Ref.Register) : Instruction()
        data class Inc(val x: Ref.Register) : Instruction()
        data class Dec(val x: Ref.Register) : Instruction()
        data class Jump(val x: Ref, val y: Ref) : Instruction()
        data class Out(val x: Register) : Instruction()
    }

    fun parseInstruction(s: String): Instruction {
        val split = s.split(" ")
        return when (split[0]) {
            "cpy" -> Copy(Ref.parse(split[1]), Register(split[2].first()))
            "inc" -> Inc(Register(split[1].first()))
            "dec" -> Dec(Register(split[1].first()))
            "jnz" -> Jump(Ref.parse(split[1]), Ref.parse(split[2]))
            "out" -> Out(Register(split[1].first()))
            else -> throw IllegalArgumentException(s)
        }
    }

    data class State(val registers: Map<Char, Long>, val position: Int, val terminated: Boolean, val instructions: List<Instruction>) {
        fun getValue(ref: Ref): Long {
            return when (ref) {
                is Register -> registers[ref.name] ?: 0
                is Ref.Number -> ref.n
            }
        }

        fun terminate() = this.copy(terminated = true)

        fun updateValue(r: Register, value: Long): State = this.copy(registers = this.registers + (r.name to value))

        fun next(n: Int = 1): State {
            val p = this.position + n
            return if (p >= instructions.size) terminate()
            else this.copy(position = p)
        }
    }

    fun run(state: State): Sequence<Long> {
        var current = state
        return sequence {
            while (!current.terminated) {
                val instr = current.instructions[current.position]
                current = when (instr) {
                    is Copy -> current.updateValue(instr.y, current.getValue(instr.x)).next()
                    is Inc -> current.updateValue(instr.x, current.getValue(instr.x) + 1).next()
                    is Dec -> current.updateValue(instr.x, current.getValue(instr.x) - 1).next()
                    is Jump -> if (current.getValue(instr.x) != 0L) current.next(current.getValue(instr.y).toInt()) else current.next()
                    is Out -> {
                        yield(current.getValue(instr.x))
                        current.next()
                    }
                }
            }
        }
    }

    fun parse(lines: Sequence<String>): State {
        val instructions = lines.map { parseInstruction(it) }.toList()
        return State(mapOf(), 0, false, instructions)
    }

    fun clockSignal(a: Long, instructions: List<Instruction>): Sequence<Long> {
        val state = State(mapOf('a' to a), 0, false, instructions)
        return run(state)
    }

    fun isClockSignal(signal: Sequence<Long>): Boolean {
        val n = 5
        val sample = signal.take(n).toList()
//        println(sample)
        return sample.size == n && sample.withIndex().all { (index, value) -> value == index % 2L }
    }

    fun solve(lines: Sequence<String>): Long {
        val instructions = lines.map { parseInstruction(it) }.toList()
        var a = 0L
        var signal: Sequence<Long>
        do {
            a++
            println("a $a")
            signal = clockSignal(a, instructions)
        }while(!isClockSignal(signal))
        return a
    }

}