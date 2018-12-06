package com.ctl.aoc.kotlin.y2018




object Day6 {

    data class CoordinateValue(val coordinate: String, val distance: Int)
    data class Point(val x: Int, val y: Int)

    data class Grid(val maxX: Int, val maxY: Int, val map: MutableMap<Point, CoordinateValue>) {
        fun print() {
            println()
            for (x in 0..maxX) {
                for (y in 0..maxY) {
                    val value = map[Point(x, y)] ?: CoordinateValue(".", -1)
                    if (value.distance == 0) print(value.coordinate.toUpperCase()) else print(value.coordinate)
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
        return Grid(maxX + 1, maxY + 1, map.toMutableMap())
    }

    private fun computeGridDistances(grid: Grid): Grid {
        val copy = grid.copy()
        val originalPoints = grid.map.filter { it.value.distance == 0 }.entries

        originalPoints.forEach { (point, value) ->
            for (x in 0..grid.maxX) {
                for (y in 0..grid.maxY) {
                    val current = Point(x, y)
                    if (current != point) {
                        val currentValue = copy.map[current]
                        val distance = point.distance(current)
                        if (currentValue == null) {
                            // new value we were there first
                            copy.map[current] = CoordinateValue(value.coordinate, distance)
                        } else {
                            if (distance < currentValue.distance) {
                                // we have a better value
                                copy.map[current] = CoordinateValue(value.coordinate, distance)
                            } else if (distance == currentValue.distance) {
                                // if equal it's a tie
                                copy.map[current] = CoordinateValue(".", distance)
                            }
                        }
                    }
                }
            }
        }
        return copy
    }

    private fun findLargestArea(grid: Grid): Int{
        // first we elminate the coordinate on the borders
        val disqualified = mutableSetOf<String>()
        disqualified.addAll(grid.map.filter { it.key.x == 0 }.map { it.value.coordinate })
        disqualified.addAll(grid.map.filter { it.key.y == 0 }.map { it.value.coordinate })
        disqualified.addAll(grid.map.filter { it.key.x == grid.maxX -1 }.map { it.value.coordinate })
        disqualified.addAll(grid.map.filter { it.key.y == grid.maxY -1 }.map { it.value.coordinate })
        println(disqualified)
        val grouped = grid.map.filter { !disqualified.contains(it.value.coordinate) }.asSequence().groupingBy { it.value.coordinate }
        return grouped.eachCount().maxBy { it.value }?.value ?: 0
    }

    fun solve1(lines: Sequence<String>): Int {
        val points = lines.map { parsePoint(it) }
        val grid = buildGrid(points)
//        grid.print()
        val g2 = computeGridDistances(grid)
//        g2.print()
        return findLargestArea(grid)
    }
}