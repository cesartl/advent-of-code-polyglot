package com.ctl.aoc.kotlin.y2018

object Day1p2 {
    fun solve(input: List<Int>): Int  {
        val seq = generateSequence(input) { input }
        var ints = seq.flatMap { it.asSequence() }

        var seen = mutableSetOf<Int>()
        var current = 0

        val it = ints.iterator()
        var next: Int
        while(!seen.contains(current)){
            seen.add(current)
            next = it.next()
            current += next
        }

        return current
    }
}