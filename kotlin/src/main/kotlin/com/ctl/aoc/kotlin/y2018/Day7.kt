package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.BuildOrder
import java.lang.IllegalArgumentException
import java.util.regex.Pattern

object Day7 {

    val pattern = Pattern.compile("Step ([\\w]) must be finished before step ([\\w]) can begin\\.")

    fun parse(s: String): Pair<String, String> {
        val m = pattern.matcher(s)
        if (m.matches()) {
            return m.group(1) to m.group(2)
        }
        throw IllegalArgumentException(s)
    }

    fun solve1(lines: Sequence<String>): String {
        val steps = lines.map { parse(it) }.toList()

        val allNodes = steps.flatMap { listOf(it.first, it.second) }.toSet()

        val inOrder = BuildOrder.buildOrder(allNodes, steps)
        return inOrder.joinToString("")
    }

    fun solve2(lines: Sequence<String>, nWorkers : Int, time: (String) -> Int): Int {
        val steps = lines.map { parse(it) }.toList()

        val allNodes = steps.flatMap { listOf(it.first, it.second) }.toSet()

        val inOrder = BuildOrder.buildOrder(allNodes, steps, nWorkers, time)
        return (inOrder.maxBy { it.second }?.second ?: 0)
    }
}