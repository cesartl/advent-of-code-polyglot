package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.y2016.Day23.Instruction.*
import com.ctl.aoc.kotlin.y2016.Day23.Ref.Register

object Day23 {

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
    }

    fun parseInstruction(s: String): Instruction {
        val split = s.split(" ")
        return when (split[0]) {
            "cpy" -> Copy(Ref.parse(split[1]), Register(split[2].first()))
            "inc" -> Inc(Register(split[1].first()))
            "dec" -> Dec(Register(split[1].first()))
            "jnz" -> Jump(Ref.parse(split[1]), Ref.parse(split[2]))
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

        private fun next(n: Int = 1): State {
            val p = this.position + n
            return if (p >= instructions.size) terminate()
            else this.copy(position = p)
        }

        fun execute(): State {
            val instr = instructions[position]
            return when (instr) {
                is Copy -> updateValue(instr.y, getValue(instr.x)).next()
                is Inc -> updateValue(instr.x, getValue(instr.x) + 1).next()
                is Dec -> updateValue(instr.x, getValue(instr.x) - 1).next()
                is Jump -> if (getValue(instr.x) != 0L) next(getValue(instr.y).toInt()) else next()
            }
        }
    }

    tailrec fun run(state: State): State = if (state.terminated) state else run(state.execute())

    fun parse(lines: Sequence<String>): State {
        val instructions = lines.map { parseInstruction(it) }.toList()
        return State(mapOf(), 0, false, instructions)
    }

    fun day12Solve1(lines: Sequence<String>): Long {
        val state = parse(lines)
        val end = run(state)
        return end.getValue(Register('a'))
    }

    fun day12Solve2(lines: Sequence<String>): Long {
        val state = parse(lines).updateValue(Register('c'), 1)
        val end = run(state)
        return end.getValue(Register('a'))
    }
}