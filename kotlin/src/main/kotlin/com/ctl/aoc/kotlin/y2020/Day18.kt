package com.ctl.aoc.kotlin.y2020

import java.util.*

object Day18 {
    fun solve1(input: Sequence<String>): Long {
        return input.map { evaluateExpression(it) }.sum()
    }

    fun solve2(input: Sequence<String>): Long {
        return input.map { evaluateExpression2(it) }.sum()
    }

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

        fun priority(): Int {
            return when (this) {
                Plus -> 2
                Mult -> 1
                Blank -> 0
            }
        }
    }

    data class Evaluation(val acc: Long = 0L, val operator: Operator = Operator.Plus) {
        fun eval(n: Long): Evaluation {
            return this.copy(acc = operator.compute(acc, n))
        }
    }

    fun evaluateExpression(string: String): Long {
        val expression = string.replace(" ", "")
        var idx = 0
        var evaluation = Evaluation()
        while (idx < expression.length) {
            val c = expression[idx]
            evaluation = when {
                c == '+' -> {
                    idx++
                    evaluation.copy(operator = Operator.Plus)
                }
                c == '*' -> {
                    idx++
                    evaluation.copy(operator = Operator.Mult)
                }
                c.isDigit() -> {
                    idx++
                    val n = c.toString().toLong()
                    evaluation.eval(n)
                }
                c == '(' -> {
                    val endIdx = findClosingBracket(expression, idx)
                    val bracketExpression = expression.substring((idx + 1) until (endIdx - 1))
                    val n = evaluateExpression(bracketExpression)
                    idx = endIdx
                    evaluation.eval(n)
                }
                else -> {
                    throw Error("Char: $c at $idx")
                }
            }
        }
//        println("$expression is ${evaluation.acc}")
        return evaluation.acc
    }

    fun evaluateExpression2(string: String): Long {
        val expression = string.replace(" ", "")
//        println("Doing $expression")
        var idx = 0
        val numberStack: Deque<Long> = ArrayDeque()
        val operatorStack: Deque<Operator> = ArrayDeque()
        while (idx < expression.length) {
            val c = expression[idx]
            when {
                c == '+' -> {
                    idx++
                    val newOperator = Operator.Plus
                    collapse(operatorStack, numberStack, newOperator)
                    operatorStack.push(newOperator)
                }
                c == '*' -> {
                    idx++
                    val newOperator = Operator.Mult
                    collapse(operatorStack, numberStack, newOperator)
                    operatorStack.push(newOperator)
                }
                c.isDigit() -> {
                    idx++
                    val n = c.toString().toLong()
                    numberStack.push(n)
                }
                c == '(' -> {
                    val endIdx = findClosingBracket(expression, idx)
                    val bracketExpression = expression.substring((idx + 1) until (endIdx - 1))
                    val n = evaluateExpression2(bracketExpression)
                    idx = endIdx
                    numberStack.push(n)
                }
                else -> {
                    throw Error("Char: $c at $idx")
                }
            }
        }
        collapse(operatorStack, numberStack, Operator.Blank)
        val result = numberStack.pop()
//        println("$expression is $result")
        return result
    }

    private fun collapse(operatorStack: Deque<Operator>, numberStack: Deque<Long>, newOperator: Operator) {
        while (operatorStack.isNotEmpty() && numberStack.size >= 2) {
            if (newOperator.priority() <= operatorStack.peek().priority()) {
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
        return idx
    }
}