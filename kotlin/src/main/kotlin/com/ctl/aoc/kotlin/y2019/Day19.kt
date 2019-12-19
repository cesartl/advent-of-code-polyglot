package com.ctl.aoc.kotlin.y2019

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object Day19 {

    data class Point(val x: Long, val y: Long)

    fun solve1(intCode: LongArray): Int {
        val grid = exploreGrid(intCode, 0, 0, 50, 50)
        printGrid(grid)
        return grid.values.flatMap { it.values }.count { it }
    }

    fun exploreAndPrint(intCode: LongArray, minX: Long, minY: Long, maxX: Long, maxY: Long) {
        val grid = exploreGrid(intCode, minX, minY, maxX, maxY)
        printGrid(grid)
    }

    fun solve2(intCode: LongArray): Long {
        var y = 3L
        var x = 2L
        assert(explore(x, y, intCode))
        while (true) {
            println("Doing y=$y")
            assert(explore(x, y, intCode))
            //find last x
            var affected = false
            do {
                x++
                affected = explore(x, y, intCode)
            } while (affected)
            x--

//            println("x: $x")
            assert(explore(x, y, intCode))
            assert(!explore(x + 1, y, intCode))

            if (squareFit(x, y, intCode)) {
                assert(!squareFit(x, y - 1, intCode))
                assert(!squareFit(x - 1, y, intCode))
                break
            }
            y++
            x++
            while (!explore(x, y, intCode)) {
                x--
            }
        }
        return (x - 99) * 10000 + y
    }

    private fun squareFit(x: Long, y: Long, intCode: LongArray): Boolean {
        val topRight = explore(x, y, intCode)
        val topLeft = explore(x - 99, y, intCode)
        val bottomLeft = explore(x - 99, y + 99, intCode)
        val bottomRight = explore(x, y + 99, intCode)
        return topRight && topLeft && bottomLeft && bottomRight
    }

    private fun printGrid(grid: Map<Long, Map<Long, Boolean>>) {
        val maxY = grid.keys.maxBy { it } ?: 0L
        val minY = grid.keys.minBy { it } ?: 0L
        val maxX: Long = grid.values.flatMap { it.keys }.max() ?: 0L
        val minX: Long = grid.values.flatMap { it.keys }.min() ?: 0L

        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                if (grid[y]?.get(x) == true) {
                    print('#')
                } else {
                    print('.')
                }
            }
            println()
        }
    }

    private fun exploreGrid(intCode: LongArray, minX: Long, minY: Long, maxX: Long, maxY: Long, grid: MutableMap<Long, MutableMap<Long, Boolean>> = mutableMapOf()): MutableMap<Long, MutableMap<Long, Boolean>> {
        (minY until maxY).forEach { y ->
            (minX until maxX).forEach { x ->
                grid.computeIfAbsent(y) { mutableMapOf() }[x] = explore(x, y, intCode)
            }
        }
        return grid
    }

    private fun explore(x: Long, y: Long, intCode: LongArray): Boolean {
        val n = AtomicInteger(0)
        val output = AtomicLong()
        val intCodeState = Day9.IntCodeState(intCode = intCode.copyOf(999),
                input = {
                    if (n.getAndIncrement() % 2 == 0) {
                        x
                    } else {
                        y
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