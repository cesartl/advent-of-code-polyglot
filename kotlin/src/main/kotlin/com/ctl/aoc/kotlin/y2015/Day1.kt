package com.ctl.aoc.kotlin.y2015

object Day1 {
    fun solve1(s: String): Int {
        return s.fold(0) { acc, c ->
            when (c) {
                '(' -> acc + 1
                ')' -> acc - 1
                else -> throw IllegalArgumentException(c.toString())
            }
        }
    }

    fun solve2(s: String): Int {
        return s.foldIndexed(0) { i, acc, c ->
            val newAcc = when (c) {
                '(' -> acc + 1
                ')' -> acc - 1
                else -> throw IllegalArgumentException(c.toString())
            }
            if (newAcc == -1) {
                return i + 1
            }
            newAcc
        }
    }
}