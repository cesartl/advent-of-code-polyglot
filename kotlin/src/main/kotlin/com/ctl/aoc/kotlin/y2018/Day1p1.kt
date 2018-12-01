package com.ctl.aoc.kotlin.y2018

object Day1p1{
    fun solve(lines: List<String>) : Int = findFreq(lines.map { it.toInt() })

    fun findFreq(changes: List<Int>) = changes.sum();
}