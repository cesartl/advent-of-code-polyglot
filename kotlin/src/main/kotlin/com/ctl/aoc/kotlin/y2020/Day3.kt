package com.ctl.aoc.kotlin.y2020

object Day3 {

    fun solve1(input: Sequence<String>): Long {
        val rows = parse(input)
        return countTrees(rows, 3, 1)
    }

    fun solve2(input: Sequence<String>): Long {
        val rows = parse(input)
        return listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
                .map { (right, down) -> countTrees(rows, right, down) }
                .fold(1L, { x, y -> x * y })
    }

    private fun countTrees(rows: List<List<Boolean>>, right: Int, down: Int): Long {
        var x = 0
        var count = 0L
        (rows.indices step down).forEach { y ->
            count += if (rows[y][(x % rows[y].size)]) 1 else 0
            x += right
        }
        return count
    }

    private fun parse(input: Sequence<String>): List<List<Boolean>> {
        return input.map { line -> line.map { it == '#' } }.toList()
    }
}