package com.ctl.aoc.kotlin.y2020

object Day18 {
    fun solve1(input: Sequence<String>): Long {
        return input.map { evaluateExpression(it) }.sum()
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }

    sealed class Operator {
        object Plus : Operator()
        object Mult : Operator()

        fun compute(x: Long, y: Long): Long {
            return when (this) {
                Plus -> x + y
                Mult -> x * y
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
//        println("Doing $expression")
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