package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Grid
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.parseGrid

object Day4 {

    private val word = "XMAS"

    fun solve1(input: Sequence<String>): Int {
        val grid = parseGrid(input)
        return grid.map
            .asSequence()
            .filter { it.value == word[0] }
            .map { countWord(grid, it.key) }
            .sum()
    }

    data class Node(val position: Position, val index: Int, val visited: Set<Position>) {
        fun next(p: Position): Node {
            return copy(position = p, index = index + 1, visited = visited + p)
        }
    }

    private fun countWord(grid: Grid<Char>, start: Position): Int {
        return Position(0, 0).neighbours()
            .count { vector ->
                val last = generateSequence(start) { it + vector }
                    .withIndex()
                    .takeWhile { grid.inScope(it.value) }
                    .takeWhile { it.index < word.length }
                    .takeWhile { grid.map[it.value] == word[it.index] }
                    .last()
                last.index == word.length - 1 && grid.map[last.value] == word[last.index]
            }
    }

    fun solve2(input: Sequence<String>): Int {
        val grid = parseGrid(input)
        return grid.map
            .asSequence()
            .filter { it.value == 'A' }
            .count { isX(grid, it.key) }
    }

    const val MAS = "MAS"

    private fun isX(grid: Grid<Char>, start: Position): Boolean {
        val diag1 = readWord(grid, start + Position(-1, -1), Position(1, 1))
        val diag2 = readWord(grid, start + Position(1, -1), Position(-1, 1))
        return (diag1 == MAS || diag1 == MAS.reversed()) && (diag2 == MAS || diag2 == MAS.reversed())
    }

    private fun readWord(grid: Grid<Char>, start: Position, vector: Position): String {
        return generateSequence(start) { it + vector }
            .takeWhile { grid.inScope(it) }
            .take(3)
            .mapNotNull { grid.map[it] }
            .joinToString(separator = "")
    }
}
