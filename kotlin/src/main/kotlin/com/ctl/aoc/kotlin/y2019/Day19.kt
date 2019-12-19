package com.ctl.aoc.kotlin.y2019

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object Day19 {

    data class Point(val x: Long, val y: Long)

    fun solve1(intCode: LongArray): Int {
        val grid = exploreGrid(intCode, 0, 0,50, 50)
        printGrid(grid)
        return grid.count { it.value }
    }

    fun solve2(intCode: LongArray): Int {
        val grid = exploreGrid(intCode, 300, 600, 500, 900)
        printGrid(grid)
        return 0
    }

    private fun printGrid(grid: MutableMap<Point, Boolean>) {
        val maxY = grid.keys.maxBy { it.y }?.y ?: 0L
        val minY = grid.keys.minBy { it.y }?.y ?: 0L
        val maxX = grid.keys.maxBy { it.x }?.x ?: 0L
        val minX = grid.keys.minBy { it.x }?.x ?: 0L

        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                if (grid[Point(x, y)] == true) {
                    print('#')
                } else {
                    print('.')
                }
            }
            println()
        }
    }

    private fun exploreGrid(intCode: LongArray, minX: Long, minY: Long, maxX: Long, maxY: Long): MutableMap<Point, Boolean> {
        val grid = mutableMapOf<Point, Boolean>()

        (minY until maxY).forEach { y ->
            (minX until maxX).forEach { x ->
                grid[Point(x, y)] = explore(x, y, intCode)
            }
        }
        return grid
    }

    private fun explore(maxX: Long, maxY: Long, intCode: LongArray): Boolean {
        val n = AtomicInteger(0)
        val output = AtomicLong()
        val intCodeState = Day9.IntCodeState(intCode = intCode.copyOf(999),
                input = {
                    if (n.getAndIncrement() % 2 == 0) {
                        maxX
                    } else {
                        maxY
                    }
                },
                output = {
                    output.set(it)
                })
        Day9.run {
            intCodeState.execute()
        }
        return output.get() == 1L
    }

}