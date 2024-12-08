package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.y2024.Day7.Operator
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

private val operatorsCache: MutableMap<Int, List<List<Operator>>> = mutableMapOf()

object Day7 {

    sealed class Operator {
        abstract fun apply(x: Long, y: Long): Long
    }

    data object Add : Operator() {
        override fun apply(x: Long, y: Long): Long = x + y
    }

    data object Mul : Operator() {
        override fun apply(x: Long, y: Long): Long = x * y
    }

    data object Concat : Operator() {
        override fun apply(x: Long, y: Long): Long {
            return (x.toString() + y.toString()).toLong()
        }
    }

    private val POWERS_OF_TEN = (0..20).map { 10.0.pow(it.toDouble()).toLong() }

    data object Concat2 : Operator() {
        override fun apply(x: Long, y: Long): Long {
            val exp = floor( log10(y.toDouble())).toInt() + 1
            return x * Day7.POWERS_OF_TEN[exp] + y
        }
    }

    data object Concat3 : Operator() {
        override fun apply(x: Long, y: Long): Long {
            return concatNumbers(x, y)
        }

    }

    fun concatNumbers(a: Long, b: Long): Long {
        var bCopy = b
        var multiplier = 1

        // Calculate 10^n, where n is the number of digits in b
        while (bCopy > 0) {
            multiplier *= 10
            bCopy /= 10
        }

        // Concatenate the numbers
        return a * multiplier + b
    }

    data class State(val result: Long, val operators: List<Operator>)

    data class Equation(val result: Long, val numbers: List<Long>) {

        fun isPossible(operatorsMap: Map<Int, List<List<Operator>>>): Boolean {
            val operators = operatorsMap[numbers.size - 1] ?: emptyList()
            return operators.any { isMatch(it) }
        }

        private fun isMatch(operators: List<Operator>): Boolean {
            val first = numbers.first()
            val result = numbers.asSequence()
                .drop(1)
                .foldIndexed(State(first, operators)) {i, state, current ->
                    val result = state.operators[i].apply(state.result, current)

                    if (result > this.result) {
                        return false
                    }

                    State(result, state.operators)
                }
            return result.result == this.result
        }
    }

    fun solve1(input: Sequence<String>): Long {
        val equations = input
            .map { parseEquation(it) }
            .toList()
        val maxN = equations.maxOfOrNull { it.numbers.size } ?: 0
        val operators = generateOperators(maxN-1, listOf(Add, Mul))
        return equations
            .asSequence()
            .filter { it.isPossible(operators) }
            .map { it.result }
            .sum()
    }

    fun solve2(input: Sequence<String>): Long {
        val equations = input
            .map { parseEquation(it) }
            .toList()
        val maxN = equations.maxOfOrNull { it.numbers.size } ?: 0
        val operators = generateOperators(maxN-1, listOf(Add, Mul, Concat3))
        return equations
            .asSequence()
            .filter { it.isPossible(operators) }
            .map { it.result }
            .sum()
    }

    private fun parseEquation(line: String): Equation {
        val (result, line) = line.split(":")
        val x = line.trim().splitToSequence(" ").map { it.toLong() }.toList()
        return Equation(result.trim().toLong(), x)
    }

    private fun generateOperators(n: Int, operators: List<Operator>): Map<Int, List<List<Operator>>>{
        val cache = mutableMapOf<Int, List<List<Operator>>>()
        (1 .. n).forEach { i ->
            val acc = cache[i - 1] ?: listOf(emptyList())
            val newAcc = acc.flatMap { line ->
                operators.map { operator -> line + operator }
            }
            cache[i] = newAcc
        }
        return cache
    }
}

