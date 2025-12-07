package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.transpose

object Day6 {
    fun solve1(input: Sequence<String>): Long {
        val strings = input.map { it.trim().split("\\s+".toRegex()) }.toList()
        val numbers: List<List<Long>> = strings.dropLast(1).map { line -> line.map { it.toLong() } }
        val ops: List<String> = strings.last()

        return ops.asSequence()
            .withIndex()
            .sumOf { (i, op) ->
                numbers.asSequence()
                    .map { it[i] }
                    .fold(initial(op), operation(op))
            }
    }

    fun solve2(input: Sequence<String>): Long {
        val ops: List<IndexedValue<Char>> = input.last().withIndex()
            .filterNot { it.value == ' ' }
            .toList()

        val strings = input.toList().dropLast(1)
        val l = strings.maxOf { it.length }
        val padded = strings.map { line -> line.padEnd(l, ' ') }
        val transpose = padded.map { it.toList() }.transpose()
        val numbers = transpose
            .map { it.joinToString("").trim() }

        return ops.asSequence().map { (i, op) ->
            generateSequence(i){it + 1}
                .takeWhile { it < numbers.size}
                .map { numbers[it] }
                .takeWhile { it != "" }
                .map { it.toLong() }
                .fold(initial(op.toString()), operation(op.toString()))
        }.sum()
    }

    fun initial(op: String): Long {
        return when (op) {
            "*" -> 1L
            "+" -> 0L
            else -> error("Unexpected op: $op")
        }
    }

    fun operation(op: String): (Long, Long) -> Long {
        return when (op) {
            "*" -> { a, b -> a * b }
            "+" -> { a, b -> a + b }
            else -> error("Unexpected op: $op")
        }
    }
}
