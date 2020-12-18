package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.RPN
import com.ctl.aoc.kotlin.utils.RpnOperator
import com.ctl.aoc.kotlin.utils.RpnOperatorContext
import com.ctl.aoc.kotlin.utils.longContext

object Day18Bis {

    fun solve1(input: Sequence<String>): Long {
        return input.map { evaluateExpression1(it) }.sum()
    }

    fun evaluateExpression1(it: String) = RPN.parse(it, longContext, operatorContext1).value

    fun solve2(input: Sequence<String>): Long {
        return input.map { evaluateExpression2(it) }.sum()
    }

    fun evaluateExpression2(it: String) = RPN.parse(it, longContext, operatorContext2).value

    private val operatorContext1 = object : RpnOperatorContext {
        override fun RpnOperator<*>.priority(): Int = 1
    }

    private val operatorContext2 = object : RpnOperatorContext {
        override fun RpnOperator<*>.priority(): Int = when (this) {
            is RpnOperator.Plus -> 2
            is RpnOperator.Minus -> 2
            is RpnOperator.Mult -> 1
            is RpnOperator.Div -> 1
        }
    }
}