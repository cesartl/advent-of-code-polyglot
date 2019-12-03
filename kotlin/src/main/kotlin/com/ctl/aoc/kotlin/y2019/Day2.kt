package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException

object Day2 {

    data class IntCodeState(val index: Int = 0, val intCode: IntArray, val terminated: Boolean = false) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as IntCodeState

            if (index != other.index) return false
            if (terminated != other.terminated) return false

            return true
        }

        override fun hashCode(): Int {
            var result = index
            result = 31 * result + terminated.hashCode()
            return result
        }
    }

    sealed class OppCode {
        abstract fun nextState(state: IntCodeState): IntCodeState

        sealed class BinaryOppCode(open val arg1Idx: Int, open val arg2Idx: Int, open val outputIdx: Int) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val intCode = state.intCode
                val result = compute(intCode[arg1Idx], intCode[arg2Idx])
                intCode[outputIdx] = result
                return state.copy(index = state.index + 4, intCode = intCode)
            }

            abstract fun compute(arg1: Int, arg2: Int): Int

            data class Op1(override val arg1Idx: Int, override val arg2Idx: Int, override val outputIdx: Int) : BinaryOppCode(arg1Idx, arg2Idx, outputIdx) {
                override fun compute(arg1: Int, arg2: Int): Int = arg1 + arg2
            }

            data class Op2(override val arg1Idx: Int, override val arg2Idx: Int, override val outputIdx: Int) : BinaryOppCode(arg1Idx, arg2Idx, outputIdx) {
                override fun compute(arg1: Int, arg2: Int): Int = arg1 * arg2
            }
        }

        object Op99 : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState = state.copy(terminated = true)
        }
    }

    fun IntCodeState.currentOpCode(): OppCode {
        return when (val code = this.intCode[this.index]) {
            1 -> OppCode.BinaryOppCode.Op1(this.intCode[index + 1], this.intCode[index + 2], this.intCode[index + 3])
            2 -> OppCode.BinaryOppCode.Op2(this.intCode[index + 1], this.intCode[index + 2], this.intCode[index + 3])
            99 -> OppCode.Op99
            else -> throw IllegalArgumentException("Invalid oppCode $code")
        }
    }

    fun IntCodeState.nextState(): IntCodeState {
        val oppCode = this.currentOpCode()
        return oppCode.nextState(this)
    }

    tailrec fun IntCodeState.exectute(): IntCodeState {
        return if (this.terminated) this else this.nextState().exectute()
    }

    fun solve1(program: IntArray): Int {
        program[1] = 12
        program[2] = 2
        executeProgram(program)
        return program[0]
    }

    fun solve2(program: IntArray): Int {
        var left = 0
        var right = 99
        val target = 19690720
        while (left <= right) {
            val mid = (left + right) / 2;
            val result = executeProgram(program.clone(), mid, 0)
            println("mid $mid, left $left, right $right, result $result")

            if (result >= target) {
                right = mid - 1
            } else if (result < target) {
                left = mid + 1
            } else {
                break
            }
        }
        val noun = left - 1
        val tmp = executeProgram(program.clone(), noun, 0)
        val diff = (target - tmp)

        val verb = diff
        println("diff $diff")

        println("noun $noun, verb $verb, result ${executeProgram(program.clone(), noun, verb)}")
        return 100 * noun + verb
    }

    fun executeProgram(program: IntArray, noun: Int, verb: Int): Int {
        program[1] = noun
        program[2] = verb
        executeProgram(program)
        return program[0]
    }

    fun executeProgram(program: IntArray): IntArray {
        return IntCodeState(intCode = program).exectute().intCode
    }
}