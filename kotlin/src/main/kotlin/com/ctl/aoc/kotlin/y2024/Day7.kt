package com.ctl.aoc.kotlin.y2024


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

    data class State(val result: Long, val operators: List<Operator>)

    private val operatorsCache: MutableMap<Int, List<List<Operator>>> = mutableMapOf()

    data class Equation(val result: Long, val numbers: List<Long>) {

        fun isPossible(): Boolean {
            val n = numbers.size - 1
            val operators = operatorsCache.computeIfAbsent(n) {
                generateOperators(
                    numbers.size - 1,
                    listOf(Add, Mul),
                    listOf(emptyList())
                )
            }
            return operators.any { isMatch(it) }
        }

        fun isPossible2(): Boolean {
            val n = numbers.size - 1
            val operators = operatorsCache.computeIfAbsent(n) {
                generateOperators(
                    numbers.size - 1,
                    listOf(Add, Mul, Concat),
                    listOf(emptyList())
                )
            }
            return operators.any { isMatch(it) }
        }

        private fun isMatch(operators: List<Operator>): Boolean {
            val first = numbers.first()
            val result = numbers.asSequence()
                .drop(1)
                .fold(State(first, operators)) { state, current ->
                    val result = state.operators.first().apply(state.result, current)
                    State(result, state.operators.drop(1))
                }
            return result.result == this.result
        }
    }

    fun solve1(input: Sequence<String>): Long {
        return input
            .map { parseEquation(it) }
            .filter { it.isPossible() }
            .map { it.result }
            .sum()
    }

    fun solve2(input: Sequence<String>): Long {
        return input
            .map { parseEquation(it) }
            .filter { it.isPossible2() }
            .map { it.result }
            .sum()
    }

    private fun parseEquation(line: String): Equation {
        val (result, line) = line.split(":")
        val x = line.trim().splitToSequence(" ").map { it.toLong() }.toList()
        return Equation(result.trim().toLong(), x)
    }

    private tailrec fun <T> generateOperators(
        n: Int,
        operators: List<T>,
        acc: List<List<T>>
    ): List<List<T>> {
        if (n == 0) {
            return acc
        }
        val newAcc = acc.flatMap { line ->
            operators.map { operator -> line + operator }
        }
        return generateOperators(n - 1, operators, newAcc)
    }
}

