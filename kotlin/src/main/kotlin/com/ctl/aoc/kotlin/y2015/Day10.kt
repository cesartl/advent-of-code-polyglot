package com.ctl.aoc.kotlin.y2015

object Day10 {

    fun nextCountAndSay(l: String): String {
        val first = l.first()
        val r = (l.drop(1)).fold(Triple(first, StringBuilder(), 1)) { (prev, acc, count), c ->
            when (c) {
                prev -> Triple(c, acc, count + 1)
                else -> {
                    acc.append("$count$prev")
                    Triple(c, acc, 1)
                }
            }
        }
        r.second.append("${r.third}${r.first}")
        return r.second.toString()
    }

    fun solve1(start: String): Int {
        return generateSequence(start) { nextCountAndSay(it) }.drop(40).first().length
    }

    fun solve2(start: String): Int {
        return generateSequence(start) { nextCountAndSay(it) }.drop(50).first().length
    }
}