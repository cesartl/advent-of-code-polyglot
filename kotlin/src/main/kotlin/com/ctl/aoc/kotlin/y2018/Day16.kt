package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.y2018.Day16.Opcode.*
import com.ctl.aoc.kotlin.y2018.Day16.Value.Immediate
import com.ctl.aoc.kotlin.y2018.Day16.Value.Register
import java.util.regex.Pattern

object Day16 {

    data class State(val registers: Map<Int, Int>) {
        fun getValue(value: Value): Int = value.getValue(this)
    }

    sealed class Value {
        data class Immediate(val value: Int) : Value()
        data class Register(val register: Int) : Value()
        object Nil : Value()

        fun getValue(state: State): Int {
            return when (this) {
                is Immediate -> this.value
                is Register -> state.registers[this.register] ?: 0
                is Nil -> -1
            }
        }
    }

    sealed class Opcode {

        open fun aValue(a: Int): Value = Register(a)

        abstract fun bValue(b: Int): Value

        abstract fun cValue(aV: Int, bV: Int): Int

        fun exec(a: Int, b: Int, c: Int): (State) -> State = { state ->
            val aV = aValue(a)
            val bV = bValue(b)
            val cV = cValue(state.getValue(aV), state.getValue(bV))
            state.copy(registers = state.registers + (c to cV))
        }

        // addition

        abstract class Add : Opcode() {
            override fun cValue(aV: Int, bV: Int): Int = aV + bV
        }

        object addr : Add() {
            override fun bValue(b: Int): Value = Register(b)
        }

        object addi : Add() {
            override fun bValue(b: Int): Value = Immediate(b)
        }

        // multiplication
        abstract class Mul : Opcode() {
            override fun cValue(aV: Int, bV: Int): Int = aV * bV
        }

        object mulr : Mul() {
            override fun bValue(b: Int): Value = Register(b)
        }

        object muli : Mul() {
            override fun bValue(b: Int): Value = Immediate(b)
        }

        // And
        abstract class Ban : Opcode() {
            override fun cValue(aV: Int, bV: Int): Int = aV and bV
        }

        object banr : Ban() {
            override fun bValue(b: Int): Value = Register(b)
        }

        object bani : Ban() {
            override fun bValue(b: Int): Value = Immediate(b)
        }

        // or
        abstract class Bor : Opcode() {
            override fun cValue(aV: Int, bV: Int): Int = aV or bV
        }

        object borr : Bor() {
            override fun bValue(b: Int): Value = Register(b)
        }

        object bori : Bor() {
            override fun bValue(b: Int): Value = Immediate(b)
        }

        // assignment
        abstract class Set : Opcode() {
            override fun bValue(b: Int): Value = Value.Nil
            override fun cValue(aV: Int, bV: Int): Int = aV
        }

        object setr : Set() {
            override fun aValue(a: Int): Value = Register(a)
        }

        object seti : Set() {
            override fun aValue(a: Int): Value = Immediate(a)
        }

        // gt
        abstract class Gt : Opcode() {
            override fun cValue(aV: Int, bV: Int): Int = if (aV > bV) 1 else 0
        }

        object gtir : Gt() {
            override fun aValue(a: Int): Value = Immediate(a)
            override fun bValue(b: Int): Value = Register(b)
        }

        object gtri : Gt() {
            override fun aValue(a: Int): Value = Register(a)
            override fun bValue(b: Int): Value = Immediate(b)
        }

        object gtrr : Gt() {
            override fun aValue(a: Int): Value = Register(a)
            override fun bValue(b: Int): Value = Register(b)
        }

        // eq
        abstract class Eq : Opcode() {
            override fun cValue(aV: Int, bV: Int): Int = if (aV == bV) 1 else 0
        }

        object eqir : Eq() {
            override fun aValue(a: Int): Value = Immediate(a)
            override fun bValue(b: Int): Value = Register(b)
        }

        object eqri : Eq() {
            override fun aValue(a: Int): Value = Register(a)
            override fun bValue(b: Int): Value = Immediate(b)
        }

        object eqrr : Eq() {
            override fun aValue(a: Int): Value = Register(a)
            override fun bValue(b: Int): Value = Register(b)
        }
    }

    val opcodes = listOf(addr, addi, mulr, muli, banr, bani, borr, bori, gtir, gtri, gtrr, eqir, eqri, eqrr, setr, seti)

    data class TestSpec(val opCode: Int, val a: Int, val b: Int, val c: Int, val before: State, val after: State)

    val beforePattern = Pattern.compile("Before: \\[(.+)\\]")
    val afterPattern = Pattern.compile("After: \\[(.+)\\]")

    fun parseState(s: String): State {
        val registers = s.split(",").map { it.trim() }.mapIndexed { index, s -> index to s.toInt() }.toMap()
        return State(registers)
    }

    fun parseSpec(lines: List<String>): TestSpec {
        val before = parseState(lines[0].substring(9).dropLast(1))
        val after = parseState(lines[2].substring(9).dropLast(1))
        val op = lines[1].split(" ").map { it.toInt() }
        return TestSpec(opCode = op[0], a = op[1], b = op[2], c = op[3], before = before, after = after)
    }

    fun matchSpec(opcode: Opcode, spec: TestSpec): Boolean {
        val exec = opcode.exec(spec.a, spec.b, spec.c)
        return exec(spec.before) == spec.after
    }

    fun matchForSpec(spec: TestSpec): List<Opcode> = opcodes.filter { matchSpec(it, spec) }

    fun allSpecs(lines: List<String>): List<TestSpec> {
        val l = mutableListOf<TestSpec>()
        var i = 0
        while (i < lines.size) {
            l.add(parseSpec(lines.subList(i, i + 3)))
            i += 4
        }
        return l.toList()
    }

    fun solve1(input: List<String>): Int {
        val specs = allSpecs(input)
        return specs.map { matchForSpec(it).size }.count { it >= 3 }
    }

}