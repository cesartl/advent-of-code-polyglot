package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException

object Day9 {

    data class IntCodeState(val index: Int = 0, val intCode: LongArray, val terminated: Boolean = false, val input: () -> Long = { 0 }, val output: (Long) -> Unit = {}, val relativeBase: Long = 0L) {
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
        data class ImmediateParameter(val value: Long) : Parameter()
        data class RelativeParameter(val relativeIndex: Int) : Parameter()
    }

    fun IntCodeState.getValue(parameter: Parameter): Long {
        return when (parameter) {
            is Parameter.PositionParameter -> this.intCode[parameter.index]
            is Parameter.ImmediateParameter -> parameter.value
            is Parameter.RelativeParameter -> this.intCode[(parameter.relativeIndex + this.relativeBase).toInt()]
        }
    }

    sealed class OppCode {
        abstract fun nextState(state: IntCodeState): IntCodeState

        sealed class BinaryOppCode(open val param1: Parameter, open val param2: Parameter, open val output: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val intCode = state.intCode
                val result = compute(state.getValue(param1), state.getValue(param2))
                when (output) {
                    is Parameter.PositionParameter -> intCode[(output as Parameter.PositionParameter).index] = result
                    is Parameter.ImmediateParameter -> throw IllegalArgumentException("Cannot use immediate with Opp3")
                    is Parameter.RelativeParameter -> intCode[((output as Parameter.RelativeParameter).relativeIndex + state.relativeBase).toInt()] = result
                }
                return state.copy(index = state.index + 4, intCode = intCode)
            }

            abstract fun compute(arg1: Long, arg2: Long): Long

            data class Op1(override val param1: Parameter, override val param2: Parameter, override val output: Parameter) : BinaryOppCode(param1, param2, output) {
                override fun compute(arg1: Long, arg2: Long): Long = arg1 + arg2
            }

            data class Op2(override val param1: Parameter, override val param2: Parameter, override val output: Parameter) : BinaryOppCode(param1, param2, output) {
                override fun compute(arg1: Long, arg2: Long): Long = arg1 * arg2
            }

            data class Op7(override val param1: Parameter, override val param2: Parameter, override val output: Parameter) : BinaryOppCode(param1, param2, output) {
                override fun compute(arg1: Long, arg2: Long): Long = if (arg1 < arg2) 1 else 0
            }

            data class Op8(override val param1: Parameter, override val param2: Parameter, override val output: Parameter) : BinaryOppCode(param1, param2, output) {
                override fun compute(arg1: Long, arg2: Long): Long = if (arg1 == arg2) 1 else 0
            }
        }

        object Op99 : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState = state.copy(terminated = true)
        }

        data class Opp3(val output: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val input = state.input()
                val intCode = state.intCode
                when (output) {
                    is Parameter.PositionParameter -> intCode[output.index] = input
                    is Parameter.ImmediateParameter -> throw IllegalArgumentException("Cannot use immediate with Opp3")
                    is Parameter.RelativeParameter -> intCode[(output.relativeIndex + state.relativeBase).toInt()] = input
                }
                return state.copy(index = state.index + 2)
            }
        }

        data class Opp4(val param: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val value = state.getValue(param)
                state.output(value)
                return state.copy(index = state.index + 2)
            }
        }

        data class Opp5(val param1: Parameter, val param2: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val test = state.getValue(param1)
                return if (test != 0L) {
                    state.copy(index = state.getValue(param2).toInt())
                } else {
                    state.copy(index = state.index + 3)
                }
            }
        }

        data class Opp6(val param1: Parameter, val param2: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val test = state.getValue(param1)
                return if (test == 0L) {
                    state.copy(index = state.getValue(param2).toInt())
                } else {
                    state.copy(index = state.index + 3)
                }
            }

        }

        data class Opp9(val param: Parameter) : OppCode() {
            override fun nextState(state: IntCodeState): IntCodeState {
                val value = state.getValue(param)
                return state.copy(index = state.index + 2, relativeBase = (state.relativeBase + value).toLong())
            }
        }

    }

    fun buildParameter(paramValue: Long, paramIndex: Int, nPositionMode: String): Parameter {
        return when {
            nPositionMode[paramIndex] == '0' -> {
                Parameter.PositionParameter(paramValue.toInt())
            }
            nPositionMode[paramIndex] == '1' -> {
                Parameter.ImmediateParameter(paramValue)
            }
            nPositionMode[paramIndex] == '2' -> {
                Parameter.RelativeParameter(paramValue.toInt())
            }
            else -> {
                throw IllegalArgumentException("Mode ${nPositionMode[paramIndex]}")
            }
        }
    }

    fun IntCodeState.currentOpCode(): OppCode {
        val instruction = this.intCode[this.index].toString()
        val code: Int
        val inPositionMode: String
        if (instruction.length < 2) {
            code = instruction.toInt()
            inPositionMode = "0000"
        } else {
            code = instruction.substring((instruction.length - 2 until instruction.length)).toInt()
            inPositionMode = instruction.substring(0 until instruction.length - 2).padStart(4, '0').reversed()
        }

        return when (code) {
            1 -> OppCode.BinaryOppCode.Op1(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode), buildParameter(this.intCode[index + 3], 2, inPositionMode))
            2 -> OppCode.BinaryOppCode.Op2(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode), buildParameter(this.intCode[index + 3], 2, inPositionMode))
            3 -> OppCode.Opp3(buildParameter(this.intCode[index + 1], 0, inPositionMode))
            4 -> OppCode.Opp4(buildParameter(this.intCode[index + 1], 0, inPositionMode))
            5 -> OppCode.Opp5(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode))
            6 -> OppCode.Opp6(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode))
            7 -> OppCode.BinaryOppCode.Op7(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode), buildParameter(this.intCode[index + 3], 2, inPositionMode))
            8 -> OppCode.BinaryOppCode.Op8(buildParameter(this.intCode[index + 1], 0, inPositionMode), buildParameter(this.intCode[index + 2], 1, inPositionMode), buildParameter(this.intCode[index + 3], 2, inPositionMode))
            9 -> OppCode.Opp9(buildParameter(this.intCode[index + 1], 0, inPositionMode))
            99 -> OppCode.Op99
            else -> throw IllegalArgumentException("Invalid oppCode $code")
        }
    }

    fun IntCodeState.nextState(): IntCodeState {
        val oppCode = this.currentOpCode()
        val next = oppCode.nextState(this)
        return next
    }

    tailrec fun IntCodeState.execute(): IntCodeState {
        return if (this.terminated) this else this.nextState().execute()
    }

    fun solve1(): Long {
        TODO()
    }
}