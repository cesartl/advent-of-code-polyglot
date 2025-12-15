package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Grid
import com.ctl.aoc.kotlin.utils.parseGrid

object Day12 {
    fun solve1(input: String): Int {
        val split = input.split("\n\n").map { it.trim() }
        val shapes: Map<Int, Shape> = split.asSequence().take(6).map { parseShape(it) }.associateBy { it.id }
        return split.drop(6).first().lineSequence()
            .map { parseRegion(it) }
            .count { region ->
                val total = region.shapeCount.asSequence()
                    .sumOf { (shapeId, count) ->
                        val shape = shapes[shapeId] ?: error("Shape $shapeId not found")
                        shape.numberOfElements * count
                    }
                region.size >= total
            }

    }

    data class Shape(
        val id: Int,
        val grid: Grid<Char>
    ){
        val numberOfElements: Int = grid.map.size
    }

    private fun parseShape(str: String): Shape {
        val lines = str.lineSequence()
        val id = lines.first().first().digitToInt()
        val grid = parseGrid(lines.drop(1)) {
            it.takeIf { it == '#' }
        }
        return Shape(id, grid)
    }

    data class Region(
        val width: Int,
        val length: Int,
        val shapeCount: Map<Int, Int>
    ){
        val size = width * length
    }

    private val regionRegex = """(\d+)x(\d+):(.*)""".toRegex()

    private fun parseRegion(str: String): Region {
        val match = regionRegex.matchEntire(str) ?: error("Invalid input: $str")
        val width = match.groupValues[1].toInt()
        val length = match.groupValues[2].toInt()
        val shapeCount = match.groupValues[3].trim()
            .splitToSequence(' ')
            .withIndex()
            .associate { it.index to it.value.toInt() }
        return Region(width, length, shapeCount)
    }
}
