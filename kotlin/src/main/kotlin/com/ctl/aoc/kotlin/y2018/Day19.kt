package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.y2018.Day16.Opcode
import com.ctl.aoc.kotlin.y2018.Day16.State
import com.ctl.aoc.kotlin.y2018.Day16.Value.Register
import java.util.regex.Pattern

typealias Instruction = (State) -> State

object Day19 {

    data class Args(val a: Int, val b: Int, val c: Int)

    data class SourceCode(val ip: Int, val opcodes: List<Pair<Opcode, Args>>) {
        fun compile(): Program {
            return Program(ip, opcodes.map { it.first.exec(it.second.a, it.second.b, it.second.c) }, this)
        }
    }

    data class Program(val ip: Int, val instructions: List<Instruction>, val sourceCode: SourceCode) {
        val instructionCount = mutableMapOf<Int, Long>()

        fun runOnce(state: State): State {
//            println("state: ${state.print(6)}")
            val ipValue = state.getValue(Register(ip))
            if (ipValue >= instructions.size) {
                return state
            }
            if (ipValue == 1) return opt2(state)
            instructionCount.merge(ipValue, 1L) { t, u -> t + u }
            val next = instructions[ipValue]
            val opCode = sourceCode.opcodes[ipValue]
            val nextState = next(state)
            return nextState.incrementRegister(ip)
        }

        fun isTerminated(state: State): Boolean {
            val ipValue = state.getValue(Register(ip))
            return ipValue >= instructions.size
        }

        fun opt1(state: State): State {
            println("opt1")
            var r5 = 1
            var r2 = state.getValue(Register(2))
            var r4 = state.getValue(Register(4))
            var r0 = state.getValue(Register(0))
            do {
                if (r4 * r5 == r2) {
                    r0 += 4
                }
                r5 += 1
            } while (r5 <= r2)

            val registers = state.registers + (0 to r0) + (1 to 1) + (5 to r5) + (ip to 12)
            println(registers)
            return state.copy(registers = registers)
        }

        fun opt2(state: State): State {
            println("opt2")
            var r4 = 1
            var r2 = state.getValue(Register(2))
            var r0 = state.getValue(Register(0))
            do {
                if(r2 % r4 == 0){
                    r0 += r4
                    r0 += r2 / r4
                    println("r0 $r0")
                }
                r4 += 1
            } while (r4 * r4 <= r2 )
            val registers = state.registers + (0 to r0) + (1 to 1) + (5 to 1) + (4 to r4) + (ip to 16)
            println(registers)
            return state.copy(registers = registers)
        }
    }

    fun parseOpCode(line: String): Pair<Opcode, Args> {
        val (code, a, b, c) = line.split(" ")
        val opCode = Day16.opCodesByName[code]!!
        val args = Args(a.toInt(), b.toInt(), c.toInt())
        return opCode to args
    }

    val ipPattern = Pattern.compile("#ip ([\\d])")
    fun parse(lines: List<String>): SourceCode {
        val ipM = ipPattern.matcher(lines[0])
        if (!ipM.matches()) throw IllegalArgumentException(lines[0])
        val ip = ipM.group(1).toInt()
        val opcodes = lines.drop(1).map { parseOpCode(it) }
        return SourceCode(ip, opcodes)
    }

    fun solve1(lines: List<String>): Int {
        val source = parse(lines)
        println("source")
        println(source)
        val prgm = source.compile()
        return run(State(mapOf()), prgm)
    }

    fun solve2(lines: List<String>): Int {
        val source = parse(lines)
        println("source")
        println(source)
        val prgm = source.compile()
        return run(State(mapOf(0 to 1)), prgm)
    }

    fun run(startState: State, prgm: Program): Int {
        var state = startState
        var n = 0L
        while (!prgm.isTerminated(state)) {
            if (n % 100L == 0L) {
                println("n: $n")
                println(state.print(6))
                println(prgm.instructionCount)
            }
            state = prgm.runOnce(state)
            n++
        }
        println("final")
        println(state.print(6))
        return state.getValue(Register(0))
    }
}