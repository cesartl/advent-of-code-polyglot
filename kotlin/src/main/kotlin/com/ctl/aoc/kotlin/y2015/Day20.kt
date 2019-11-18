package com.ctl.aoc.kotlin.y2015

object Day20 {
    fun solve1(minPresents: Int): Int {
        val n = minPresents
        var houses = IntArray(n)
        (1 until n).forEach { i ->
            var j = i
            while (j < n) {
                houses[j] += 10 * i
                j += i
            }
        }

        println("hello")

        var i = 0
        while (houses[i] < minPresents) {
            i++
        }
        return i
    }

    fun solve2(minPresents: Int): Int {
        val n = minPresents
        var houses = IntArray(n)
        (1 until n).forEach { i ->
            var j = i
            var count = 0
            while (j < n && count < 50) {
                houses[j] += 11 * i
                j += i
                count++
            }
        }

        println("hello")

        var i = 0
        while (houses[i] < minPresents) {
            i++
        }
        return i
    }
}