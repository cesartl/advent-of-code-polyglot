package com.ctl.aoc.kotlin.y2018

object Day1p2 {
    fun solve(input: Sequence<Int>): Int {
        var list = input.toList()
        var i = 0
        var seen = mutableSetOf<Int>()
        var current = 0
        var next: Int
        while (!seen.contains(current)) {
            seen.add(current)
            next = list[i % list.size]
            current += next
            i++
        }

        return current
    }
}