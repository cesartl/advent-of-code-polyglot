package com.ctl.aoc.kotlin.y2020

object Day1 {
    fun solve1(input: Sequence<Int>): Int {
        val (x, y) = find2WithSum(input, 2020)
        return x * y
    }

    private fun find2WithSum(input: Sequence<Int>, sum: Int): Pair<Int, Int> {
        val set = mutableSetOf<Int>()
        input.forEach { n ->
            val target = sum - n
            if (set.contains(target)) {
                return n to target
            }
            set.add(n)
        }
        throw IllegalArgumentException("Not found")
    }

    fun solve2(input: Sequence<Int>): Int {
        val list = find3WithSum(input, 2020)
        println(list)
        return list.fold(1, { acc, i -> acc * i })
    }

    private fun find3WithSum(input: Sequence<Int>, sum: Int): List<Int> {
        val sorted = input.sorted().toList()
        sorted.forEach { x ->
            var l = 0
            var r = sorted.size - 1
            var tarted = sum - x
            while (l < r) {
//                println("l: $l r: $r")
                when {
                    sorted[l] + sorted[r] == tarted -> {
                        return listOf(x, sorted[l], sorted[r])
                    }
                    sorted[l] + sorted[r] < tarted -> {
                        l++
                    }
                    else -> {
                        r--
                    }
                }
            }
        }
        throw IllegalArgumentException("Not found")
    }
}