package com.ctl.aoc.kotlin.y2018
object Day6p2 {

    data class CoordinateValue(val coordinate: String, val distance: Int)
    data class Point(val x: Int, val y: Int)

    data class Grid(val maxX: Int, val maxY: Int, val map: MutableMap<Point, CoordinateValue>, val originalCoordinates: List<Point>) {
        fun print() {
            println()
            for (x in 0..maxX) {
                for (y in 0..maxY) {
                    val value = map[Point(x, y)] ?: CoordinateValue(".", -1)
                    if (value.distance == 0) print(value.coordinate.uppercase()) else print(value.coordinate)
                }
                println()
            }
            println()
        }
    }

    private fun parsePoint(s: String): Point {
        val split = s.split(",").map { it.trim() }
        return Point(split[1].toInt(), split[0].toInt())
    }

    private fun Point.distance(other: Point): Int = Math.abs(this.x - other.x) + Math.abs(this.y - other.y)

    private fun buildGrid(points: Sequence<Point>): Grid {
        val maxX = points.maxBy { it.x }?.x ?: 0
        val maxY = points.maxBy { it.y }?.y ?: 0
        val map = points.mapIndexed { idx, p -> p to CoordinateValue("${'a' + idx}", 0) }.toMap()
        return Grid(maxX + 1, maxY + 1, map.toMutableMap(), map.keys.toList())
    }


    private fun countRegionWithinRange(grid: Grid, max: Int): Int{
        var sum: Int
        var count = 0
        for (x in 0..grid.maxX) {
            for (y in 0..grid.maxY) {
                sum = grid.originalCoordinates.map { it.distance(Point(x, y)) }.sum()
                if(sum < max){
                    count ++
                }
            }
        }
        return count
    }


    fun solve2(lines: Sequence<String>, max: Int): Int {
        val points = lines.map { parsePoint(it) }
        val grid = buildGrid(points)

        return countRegionWithinRange(grid, max)
    }
}
