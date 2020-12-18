package com.ctl.aoc.kotlin.y2020

import java.util.*

object Day18 {
    fun solve1(input: Sequence<String>): Long {
        return input.map { evaluateExpression1(it) }.sum()
    }

    fun solve2(input: Sequence<String>): Long {
        return input.map { evaluateExpression2(it) }.sum()
    }

    fun evaluateExpression1(string: String): Long = evaluateExpression(string, Operator::priority1)
    fun evaluateExpression2(string: String): Long = evaluateExpression(string, Operator::priority2)

    sealed class Operator {
        object Plus : Operator()
        object Mult : Operator()
        object Blank : Operator()

        fun compute(x: Long, y: Long): Long {
            return when (this) {
                Plus -> x + y
                Mult -> x * y
                Blank -> x
            }
        }

        fun priority1(): Int {
            return when (this) {
                Plus -> 1
                Mult -> 1
                Blank -> 0
            }
        }

        fun priority2(): Int {
            return when (this) {
                Plus -> 2
                Mult -> 1
                Blank -> 0
            }
        }
    }


    data class ExpressionStacks(
            val numberStack: Deque<Long> = ArrayDeque(),
            val operatorStack: Deque<Operator> = ArrayDeque(),
            val priority: (Operator) -> Int) {
        fun collapse(newOperator: Operator) {
            while (operatorStack.isNotEmpty() && numberStack.size >= 2) {
                if (priority(newOperator) <= priority(operatorStack.peek())) {
                    val second = numberStack.pop()
                    val first = numberStack.pop()
                    val op = operatorStack.pop()
                    val n = op.compute(first, second)
                    numberStack.push(n)
                } else {
                    break
                }
            }
        }
    }

    private fun evaluateExpression(string: String, priority: (Operator) -> Int): Long {
        val expression = string.replace(" ", "")
        var idx = 0
        val stacks = ExpressionStacks(priority = priority)
        while (idx < expression.length) {
            val c = expression[idx]
            when {
                c == '+' -> {
                    idx++
                    val newOperator = Operator.Plus
                    stacks.collapse(newOperator)
                    stacks.operatorStack.push(newOperator)
                }
                c == '*' -> {
                    idx++
                    val newOperator = Operator.Mult
                    stacks.collapse(newOperator)
                    stacks.operatorStack.push(newOperator)
                }
                c.isDigit() -> {
                    idx++
                    val n = c.toString().toLong()
                    stacks.numberStack.push(n)
                }
                c == '(' -> {
                    val endIdx = findClosingBracket(expression, idx)
                    val bracketExpression = expression.substring((idx + 1) until endIdx)
                    val n = evaluateExpression(bracketExpression, priority)
                    idx = endIdx + 1
                    stacks.numberStack.push(n)
                }
                else -> {
                    throw Error("Char: $c at $idx")
                }
            }
        }
        stacks.collapse(Operator.Blank)
        return stacks.numberStack.pop()
    }

    fun findClosingBracket(s: String, start: Int): Int {
        var bracketCount = 1
        var idx = start + 1
        while (bracketCount > 0) {
            val c = s[idx]
            if (c == '(') {
                bracketCount++
            } else if (c == ')') {
                bracketCount--
            }
            idx++
        }
        return idx - 1
    }
}