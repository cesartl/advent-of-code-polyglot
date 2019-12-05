package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException
import java.util.*

object Day5 {

    data class IntCodeState(val index: Int = 0, val intCode: IntArray, val terminated: Boolean = false, val input: LinkedList<Int> = LinkedList(), val output: LinkedList<Int> = LinkedList()) {
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

    sealed class Parameter {
        data class PositionParameter(val index: Int) : Parameter()
        data class ImmediateParameter(val value: Int) : Parameter()
    }

    fun IntCodeState.getValue(parameter: Parameter): Int {
        return when (parameter) {
            is Parameter.PositionParameter -> this.intCode[parameter.index]
            is Parameter.ImmediateParameter -> parameter.value
        }
    }

    sealed class OppCode {
        abstract fun nextState(state: IntCodeState): IntCodeState

        sealed class BinaryOppCode(open val param1: Parameter, open val param2: Parameter, open val output: Parameter.PositionParameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val intCode = state.intCode
                val result = compute(state.getValue(param1), state.getValue(param2))
                intCode[output.index] = result
                return state.copy(index = state.index + 4, intCode = intCode)
            }

            abstract fun compute(arg1: Int, arg2: Int): Int

            data class Op1(override val param1: Parameter, override val param2: Parameter, override val output: Parameter.PositionParameter) : BinaryOppCode(param1, param2, output) {
                override fun compute(arg1: Int, arg2: Int): Int = arg1 + arg2
            }

            data class Op2(override val param1: Parameter, override val param2: Parameter, override val output: Parameter.PositionParameter) : BinaryOppCode(param1, param2, output) {
                override fun compute(arg1: Int, arg2: Int): Int = arg1 * arg2
            }

            data class Op7(override val param1: Parameter, override val param2: Parameter, override val output: Parameter.PositionParameter) : BinaryOppCode(param1, param2, output) {
                override fun compute(arg1: Int, arg2: Int): Int = if(arg1 < arg2) 1 else 0
            }

            data class Op8(override val param1: Parameter, override val param2: Parameter, override val output: Parameter.PositionParameter) : BinaryOppCode(param1, param2, output) {
                override fun compute(arg1: Int, arg2: Int): Int = if(arg1 == arg2) 1 else 0
            }
        }

        object Op99 : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState = state.copy(terminated = true)
        }

        data class Opp3(val output: Parameter.PositionParameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val input = state.input.pollLast()
                val intCode = state.intCode
                intCode[output.index] = input
                return state.copy(index = state.index + 2)
            }
        }

        data class Opp4(val param: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val value = state.getValue(param)
                state.output.push(value)
                return state.copy(index = state.index + 2)
            }
        }

        data class Opp5(val param1: Parameter, val param2: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val test = state.getValue(param1)
                return if (test != 0) {
                    state.copy(index = state.getValue(param2))
                } else {
                    state.copy(index = state.index + 3)
                }
            }

        }

        data class Opp6(val param1: Parameter, val param2: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val test = state.getValue(param1)
                return if (test == 0) {
                    state.copy(index = state.getValue(param2))
                } else {
                    state.copy(index = state.index + 3)
                }
            }

        }

    }

    fun buildParameter(paramValue: Int, paramIndex: Int, nPositionMode: List<Boolean>): Parameter {
        return if (nPositionMode[paramIndex]) {
            Parameter.PositionParameter(paramValue)
        } else {
            Parameter.ImmediateParameter(paramValue)
        }
    }

    fun IntCodeState.currentOpCode(): OppCode {
        val instruction = this.intCode[this.index].toString()
        val code: Int
        val inPositionMode: List<Boolean>
        if (instruction.length < 2) {
            code = instruction.toInt()
            inPositionMode = listOf(true, true, true, true)
        } else {
            code = instruction.substring((instruction.length - 2 until instruction.length)).toInt()
            inPositionMode = instruction.substring(0 until instruction.length - 2).padStart(4, '0').reversed().map { it == '0' }
        }

        return when (code) {
            1 -> OppCode.BinaryOppCode.Op1(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode), Parameter.PositionParameter(this.intCode[index + 3]))
            2 -> OppCode.BinaryOppCode.Op2(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode), Parameter.PositionParameter(this.intCode[index + 3]))
            3 -> OppCode.Opp3(Parameter.PositionParameter(this.intCode[index + 1]))
            4 -> OppCode.Opp4(buildParameter(this.intCode[index + 1], 0, inPositionMode))
            5 -> OppCode.Opp5(buildParameter(this.intCode[index +1], 0, inPositionMode), buildParameter(this.intCode[index +2], 1, inPositionMode))
            6 -> OppCode.Opp6(buildParameter(this.intCode[index +1], 0, inPositionMode), buildParameter(this.intCode[index +2], 1, inPositionMode))
            7 -> OppCode.BinaryOppCode.Op7(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode), Parameter.PositionParameter(this.intCode[index + 3]))
            8 -> OppCode.BinaryOppCode.Op8(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode), Parameter.PositionParameter(this.intCode[index + 3]))
            99 -> OppCode.Op99
            else -> throw IllegalArgumentException("Invalid oppCode $code")
        }
    }

    fun IntCodeState.nextState(): IntCodeState {
        val oppCode = this.currentOpCode()
        val next = oppCode.nextState(this)
        return next
    }

    tailrec fun IntCodeState.exectute(): IntCodeState {
        return if (this.terminated) this else this.nextState().exectute()
    }

    fun solve1(puzzleInput: IntArray): Int {
        val input = LinkedList<Int>()
        input.push(1)
        val state = Day5.IntCodeState(intCode = puzzleInput, input = input)
        val final = state.exectute()
        return final.output.first
    }

    fun solve2(puzzleInput: IntArray): Int {
        val input = LinkedList<Int>()
        input.push(5)
        val state = Day5.IntCodeState(intCode = puzzleInput, input = input)
        val final = state.exectute()
        return final.output.first
    }
}