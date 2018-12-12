package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.buildIntegralTable

object Day11 {

    data class Cell(val x: Int, val y: Int) {
        val rackId: Int = x + 10

        fun powerLevel(serialNumber: Int): Int {
            val x = (rackId * y + serialNumber) * rackId
            return x / 100 % 10 - 5
        }
    }


    fun solve1(serialNumber: Int): Pair<Int, Int>? {
        val map = mutableMapOf<Cell, Int>()
        var max = Int.MIN_VALUE
        var tmp: Int = 0
        var result: Pair<Int, Int>? = null
        for (x in 1..300) {
            for (y in 1..300) {
                tmp = powerSquare(x to y, map, serialNumber, 3)

                if (tmp > max) {
                    result = x to y
                    max = tmp
                }
            }
        }

        return result
    }

    data class Area(val x: Int, val y: Int, val size: Int)

    fun solve2(serialNumber: Int): Area? {
        val map = mutableMapOf<Cell, Int>()
        var max = Int.MIN_VALUE
        var tmp: Int = 0
        var result: Area? = null
        for (size in 1..300) {
            println("size $size")
            for (x in 1..300) {
                for (y in 1..300) {
                    tmp = powerSquare(x to y, map, serialNumber, size)
                    if (tmp > max) {
                        result = Area(x, y, size)
                        println(result)
                        max = tmp
                    }
                }
            }
        }
        return result
    }

    private fun powerSquare(point: Pair<Int, Int>, map: MutableMap<Cell, Int>, serialNumber: Int, size: Int): Int {
        var power = 0
        val (x, y) = point
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (x + i <= 300 && y + j <= 300)
                    power += map.computeIfAbsent(Cell(x + i, y + j)) { k -> k.powerLevel(serialNumber) }
            }
        }
        return power
    }

    fun solve3(serialNumber: Int): Area? {
        val map = mutableMapOf<Int, MutableMap<Int, Int>>()
        for (y in 1..300) {
            for (x in 1..300) {
                val col = map.computeIfAbsent(y) { mutableMapOf() }
                col[x] = Cell(x, y).powerLevel(serialNumber) + (map[y - 1]?.get(x) ?: 0) + (map[y]?.get(x - 1)
                        ?: 0) - (map[y - 1]?.get(x - 1) ?: 0)
            }
        }

        val gridSize = 300
        val table = buildIntegralTable(1, gridSize, 1, gridSize) { (x, y) -> Cell(x, y).powerLevel(serialNumber).toLong() }

        var result: Area? = null
        var max = Long.MIN_VALUE
        var total: Long
        for (size in 1..300) {
//            println("size $size")
            for (y in size..300) {
                for (x in size..300) {
                    total = table.areaValue(x-size, x, y - size, y)
                    if (total > max) {
                        result = Area(x - size + 1, y - size + 1, size)
                        max = total
//                        println(total)
                    }
                }
            }
        }

        return result
    }
}