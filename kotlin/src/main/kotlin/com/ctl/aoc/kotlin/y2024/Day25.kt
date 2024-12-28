package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.parseGrid

object Day25 {
    fun solve1(input: String): Int {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        input.splitToSequence("\n\n")
            .map { it.trim() }
            .forEach {
                if (it.startsWith("#")) {
                    locks.add(parseLock(it))
                } else {
                    keys.add(parseKey(it))
                }
            }
        var count = 0
        locks.forEach { lock ->
            keys.forEach { key ->
                if (lock.zip(key)
                        .asSequence()
                        .map { (a, b) -> a + b }
                        .all { it < 6 }
                ) {
                    count++
                }
            }
        }
        return count
    }

    private fun parseLock(string: String): List<Int> {
        val grid = parseGrid(string.lineSequence()) {
            if (it == '#') it else null
        }
        return grid.xRange.map { x ->
            grid.map.entries
                .asSequence()
                .filter { it.key.x == x }
                .maxOf { it.key.y }
        }
    }

    private fun parseKey(string: String): List<Int> {
        val grid = parseGrid(string.lineSequence()) {
            if (it == '#') it else null
        }
        return grid.xRange.map { x ->
            grid.map.entries
                .asSequence()
                .filter { it.key.x == x }
                .minOf { it.key.y }
        }.map { 6 - it }
    }
}
