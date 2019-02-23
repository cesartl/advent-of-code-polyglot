package com.ctl.aoc.kotlin.y2015

object Day20 {
    fun presentSequence(): Sequence<Int> = sequence {
        val nats = mutableListOf<Int>()
        var current = 0
        while(true){
            current++
            nats.add(current)
            yield(nats.filter { current % it == 0 }.sum())
        }
    }

    fun solve1(n: Int): Int {
        return presentSequence().withIndex().dropWhile { it.value < n }.first().index + 1
    }
}