package com.ctl.aoc.kotlin.y2020

object Day9 {
    fun solve1(input: Sequence<String>, size: Int = 25): Long {
        val numbers = input.map { it.toLong() }.toList()
        val init = numbers.take(size)
        numbers.drop(size).fold(init) { previous, n ->
            if (isValid(previous, n)) {
                previous.drop(1) + listOf(n)
            } else {
                return n
            }
        }
        throw Error("Not found")
    }

    private fun isValid(previous: List<Long>, n: Long): Boolean {
        val set = mutableSetOf<Long>()
        previous.forEach { i ->
            val target = n - i
            if (set.contains(target)) {
                return true
            }
            set.add(i)
        }
        return false
    }

    fun solve2(input: Sequence<String>, size: Int = 25): Long {
        val target = solve1(input, size)
        val numbers = input.map { it.toLong() }.toList()

        numbers.indices.forEach { start ->
            val candidates = mutableListOf<Long>()
            var i = start + 1
            while (i < numbers.size && candidates.sum() <= target) {
                candidates.add(numbers[i])
                i++
            }
            candidates.removeAt(candidates.size - 1)
            if (candidates.sum() == target) {
                return (candidates.minOrNull() ?: 0L) + (candidates.maxOrNull() ?: 0L)
            }
        }
        throw Error("Not found")
    }
}