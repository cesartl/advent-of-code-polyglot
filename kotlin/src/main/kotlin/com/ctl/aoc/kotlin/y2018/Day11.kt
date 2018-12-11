package com.ctl.aoc.kotlin.y2018

object Day11 {

    data class Cell(val x: Int, val y: Int) {
        val rackId: Int = x + 10

        fun powerLevel(serialNumber: Int): Int {
            val x = ((rackId * y + serialNumber) * rackId).toString()
            return x[x.length - 3] - '0' - 5
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

}