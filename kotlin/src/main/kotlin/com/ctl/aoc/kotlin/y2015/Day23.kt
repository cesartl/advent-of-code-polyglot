package com.ctl.aoc.kotlin.y2015

import java.lang.IllegalArgumentException

object Day23 {

    sealed class Instruction {
        data class Half(val register: String) : Instruction()
        data class Tripple(val register: String) : Instruction()
        data class Increment(val register: String) : Instruction()
        data class Jump(val offset: Int) : Instruction()
        data class JumpIfEven(val register: String, val offset: Int) : Instruction()
        data class JumpIfOne(val register: String, val offset: Int) : Instruction()

        companion object {
            private val regex = """([a-z]+) ([ab])?,? ?([+-][0-9]+)?""".toRegex()
            fun parse(s: String): Instruction {
                val match = regex.matchEntire(s)!!
                val register = match.groups[2]?.value
                val offset = match.groups[3]?.value?.toInt()
                return when (match.groups[1]?.value) {
                    "hlf" -> Half(register!!)
                    "tpl" -> Tripple(register!!)
                    "inc" -> Increment(register!!)
                    "jmp" -> Jump(offset!!)
                    "jie" -> JumpIfEven(register!!, offset!!)
                    "jio" -> JumpIfOne(register!!, offset!!)
                    else -> throw IllegalArgumentException("Unknown instruction $s")
                }
            }
        }
    }


    data class State(val instructions: List<Instruction>, val position: Int = 0, val registers: Map<String, Int> = mapOf(), val terminated: Boolean = false) {
        fun next(n: Int = 1): State {
            val newPosition = position + n
            return if (newPosition >= instructions.size) {
                this.copy(terminated = true)
            } else {
                this.copy(position = newPosition)
            }
        }

        fun execute(instruction: Instruction): State {
            return when (instruction) {
                is Instruction.Half -> this.copy(registers = this.registers + (instruction.register to (this.registers[instruction.register]
                        ?: 0) / 2)).next()
                is Instruction.Tripple -> this.copy(registers = this.registers + (instruction.register to (this.registers[instruction.register]
                        ?: 0) * 3)).next()
                is Instruction.Increment -> this.copy(registers = this.registers + (instruction.register to (this.registers[instruction.register]
                        ?: 0) + 1)).next()
                is Instruction.Jump -> next(instruction.offset)
                is Instruction.JumpIfEven -> (if ((registers[instruction.register]
                                ?: 0) % 2 == 0) instruction.offset else 1).let { this.next(it) }
                is Instruction.JumpIfOne -> (if ((registers[instruction.register]
                                ?: 0) == 1) instruction.offset else 1).let { this.next(it) }
            }

        }

        fun execute(): State {
            val instruction = instructions[position]
            return execute(instruction)
        }
        override fun toString(): String {
            return "State(line=${position+1}, registers=$registers)"
        }
    }

    tailrec fun run(state: State): State = if (state.terminated) state else {
        val next = state.execute()
//        println("state $next")
        run(next)
    }

    fun solve1(instructions: List<Instruction>): Int {
        val state = State(instructions)
        val final = run(state)
        return final.registers["b"] ?: 0
    }

    fun solve1Compiled(): Int{
        var a = 4591
        return solveCompiled(a)
    }

    fun solve2Compiled(): Int{
        var a = 113383
        return solveCompiled(a)
    }

    private fun solveCompiled(x: Int): Int{
        var a : Long = x.toLong()
        var b = 0
        while(a > 1){
            b++
            if(a % 2L == 0L){
                a /= 2L
            }else{
                a *= 3L
                println("a: $a")
                a++
            }
        }
        return b
    }

    fun solve2(instructions: List<Instruction>): Int {
        val state = State(instructions = instructions, registers = mapOf("a" to 1))
        val final = run(state)
        return final.registers["b"] ?: 0
    }
}