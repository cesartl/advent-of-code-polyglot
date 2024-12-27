package com.ctl.aoc.kotlin.y2024

import java.math.BigInteger

object Day17 {

    class ChronospatialComputer(
        val program: List<Int>,
        val output: (Int) -> Unit = {},
    ) {
        val registers: MutableMap<Char, BigInteger> = mutableMapOf()
        var pointer = 0
        var terminated = false

        fun execute(debug: Boolean = false) {


            if (debug) {
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

        fun registerValue(register: Char): BigInteger = registers[register] ?: BigInteger.ZERO
        fun setRegister(register: Char, value: BigInteger) {
            registers[register] = value
        }

        fun combo(operand: Int): BigInteger {
            return when (operand) {
                0, 1, 2, 3 -> operand.toBigInteger()
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
                val combo = BigInteger.TWO.pow(combo(operand).toInt())
                val div = numerator / combo
                setRegister('A', div)
            }
        }

        data object Bxl : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val b = registerValue('B')
                val xor = b xor operand.toBigInteger()
                setRegister('B', xor)
            }
        }

        data object Bst : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val combo = combo(operand)
                setRegister('B', combo % 8.toBigInteger())
            }
        }

        data object Jnz : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val a = registerValue('A')
                if (a != BigInteger.ZERO) {
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
                output((combo(operand) % 8.toBigInteger()).toInt())
            }
        }

        data object Bdv : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val numerator = registerValue('A')
                val combo = BigInteger.TWO.pow(combo(operand).toInt())
                val div = numerator / combo
                setRegister('B', div)
            }
        }

        data object Cdv : OpCode() {
            override fun ChronospatialComputer.evaluate(operand: Int) {
                val numerator = registerValue('A')
                val combo = BigInteger.TWO.pow(combo(operand).toInt())
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

    data class State(val octalIndex: Int, val octals: CharArray, val output: List<Int>)

    fun solve2(input: String): BigInteger {
        val (_, program) = input.parseProgram()
        println(program.joinToString())

        val all = findQuine(program).toList()
        println(all)
        val first = all.min()
        println(runProgram(program, first))
        return first
    }

    private fun findQuine(program: List<Int>): Sequence<BigInteger> = sequence{
        val n = program.size
        println(n)
        val start = BigInteger.valueOf(8).pow(n - 1)
        val octals = CharArray(n) { '0' }
        octals[0] = '1'

        val queue = ArrayDeque<State>()
        queue.add(State(0, octals.copyOf(), runProgram(program, start)))
        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            if (state.output == program) {
                yield(state.octals.joinToString("").toLong(8).toBigInteger())
            }
            if (state.octalIndex < n) {
                findOctal(program, state.octals, state.octalIndex)
                    .map { it.copy(octalIndex = it.octalIndex + 1) }
                    .forEach { queue.add(it) }
            }
        }
    }

    private fun findOctal(program: List<Int>, octals: CharArray, i: Int): Sequence<State> {
        val programIndex = program.size - 1 - i
        val target = program[programIndex]
//        println("target at $programIndex = $target")
        val start = if (i == 0) 1 else 0
        return generateSequence(start) { it + 1 }
            .takeWhile { it < 8 }
            .map { octal ->
                val copy = octals.copyOf()
                copy[i] = octal.toString(8).single()
                val a = copy.joinToString("").toBigInteger(8)
//                println("a = ${a.toString(8)}")
                val output = runProgram(program, a)
//                println("Output = $output")
//                println("Value at target: ${output[programIndex]}")
                State(octalIndex = i, octals = copy, output = output)
            }
            .filter { it.output[programIndex] == target }
    }

    private fun runProgram(program: List<Int>, a: BigInteger): List<Int> {
        val outputs = mutableListOf<Int>()
        val computer = ChronospatialComputer(program = program) {
            outputs.add(it)
        }
        computer.registers['A'] = a
        computer.execute()
        return outputs
    }

    private val regex = """Register ([ABC]): (\d+)""".toRegex()

    private fun String.parseProgram(): Pair<Map<Char, BigInteger>, List<Int>> {
        val (registers, programString) = this.trim().split("\n\n")
        val registerMap = registers.lines().associate {
            val (register, value) = regex.matchEntire(it)!!.destructured
            register[0] to value.toBigInteger()
        }
        val i = programString.indexOf(':')
        val program = programString.substring(i + 1).trim().split(",").map { it.toInt() }.toList()
        return registerMap to program
    }
}
