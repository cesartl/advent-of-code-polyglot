package com.ctl.aoc.kotlin.y2015

object Day17 {


    fun nWays(n: Int, containers: List<Int>): Int {
        if (containers.isEmpty()) return if (n == 0) 1 else 0
        val container = containers.first()
        return nWays(n, containers.drop(1)) + nWays(n - container, containers.drop(1))

    }

    fun nWays2(n: Int, containers: List<Int>, containersUsed: Int = 0): List<Int> {
        if (containers.isEmpty()) return if (n == 0) listOf(containersUsed) else listOf()
        val container = containers.first()
        return nWays2(n, containers.drop(1), containersUsed) + nWays2(n - container, containers.drop(1), containersUsed + 1)
    }

    fun solve1(containers: List<Int>, n: Int): Int {
        val sortedContainers = containers.sorted()
        return nWays(n, sortedContainers)
    }

    fun solve2(containers: List<Int>, n: Int): Int {
        val sortedContainers = containers.sorted()
        val s = nWays2(n, sortedContainers)
        return s.groupBy { it }.minByOrNull { it.key }?.value?.size ?: 0
    }
}