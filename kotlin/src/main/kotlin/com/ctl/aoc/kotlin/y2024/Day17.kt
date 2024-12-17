package com.ctl.aoc.kotlin.y2024

object Day17 {

    class ChronospatialComputer(
        val program: IntArray,
        val output: (Int) -> Unit = {},
    ) {
        val registers: MutableMap<Char, Int> = mutableMapOf()
        var pointer = 0
        var terminated = false

        fun execute(debug: Boolean = false) {
            if(debug){
                println(program.joinToString(separator = ","))
            }
            while (!terminated) {
                val opCode = currentOpCode()
                if (debug) {
                    println("Pointer: $pointer, OpCode: $opCode(${program[pointer]}), Operand: ${program[pointer + 1]}  Registers: $registers")
                }
                val operand = program[pointer + 1]
                opCode.run {
                    evaluate(operand)
                    advance()
                }
                if (pointer >= program.size) {
                    terminated = true
                }
            }
        }

        fun registerValue(register: Char): Int = registers[register] ?: 0
        fun setRegister(register: Char, value: Int) {
            registers[register] = value
        }

        fun combo(operand: Int): Int {
            return when (operand) {
                0, 1, 2, 3 -> operand
                4 -> registerValue('A')
                5 -> registerValue('B')
                6 -> registerValue('C')
                else -> throw IllegalArgumentException("Unknown combo $operand")
            }
        }

        private fun currentOpCode(): OpCode {
            return when (program[pointer]) {
                0 -> OpCode.Adv
                1 -> OpCode.Bxl
                2 -> OpCode.Bst
                3 -> OpCode.Jnz
                4 -> OpCode.Bxc
                5 -> OpCode.Out
                6 -> OpCode.Bdv
                7 -> OpCode.Cdv
                else -> throw IllegalArgumentException("Unknown opcode ${program[pointer]}")
            }
        }
    }


    sealed class OpCode {
        abstract fun ChronospatialComputer.evaluate(operand: Int)

        open fun ChronospatialComputer.advance() {
            pointer += 2
        }

        data object Adv : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val numerator = registerValue('A')
                val combo = 1 shl combo(operand)
                val div = numerator / combo
                setRegister('A', div)
            }
        }

        data object Bxl : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val b = registerValue('B')
                val xor = b xor operand
                setRegister('B', xor)
            }
        }

        data object Bst : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val combo = combo(operand)
                setRegister('B', combo % 8)
            }
        }

        data object Jnz : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val a = registerValue('A')
                if (a != 0) {
                    pointer = operand
                } else {
                    pointer += 2
                }
            }

            override fun ChronospatialComputer.advance() {
                // do nothing
            }
        }

        data object Bxc : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val b = registerValue('B')
                val c = registerValue('C')
                setRegister('B', b xor c)
            }
        }

        data object Out : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                output(combo(operand) % 8)
            }
        }

        data object Bdv : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val numerator = registerValue('A')
                val combo = 1 shl combo(operand)
                val div = numerator / combo
                setRegister('B', div)
            }
        }

        data object Cdv : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val numerator = registerValue('A')
                val combo = 1 shl combo(operand)
                val div = numerator / combo
                setRegister('C', div)
            }
        }

    }

    fun solve1(input: String): String {
        val (initialRegisters, program) = input.parseProgram()

        val outputs = mutableListOf<Int>()
        val computer = ChronospatialComputer(program = program) {
            outputs.add(it)
        }

        computer.registers.putAll(initialRegisters)
        computer.execute(debug = false)
        return outputs.joinToString(separator = ",")
    }

    fun solve2(input: String): Int {
        TODO()
    }
}

private val regex = """Register ([ABC]): (\d+)""".toRegex()

private fun String.parseProgram(): Pair<Map<Char, Int>, IntArray> {
    val (registers, programString) = this.trim().split("\n\n")
    val registerMap = registers.lines().map {
        val (register, value) = regex.matchEntire(it)!!.destructured
        register[0] to value.toInt()
    }.toMap()
    val i = programString.indexOf(':')
    val program = programString.substring(i + 1).trim().split(",").map { it.toInt() }.toIntArray()
    return registerMap to program
}
