package com.ctl.aoc.kotlin.utils

data class IntegralTable(val sumMap: Map<Int, Map<Int, Long>>) {

    fun sumAt(x: Int, y: Int): Long = (sumMap[y]?.get(x) ?: 0)

    fun areaValue(x0: Int, x1: Int, y0: Int, y1: Int): Long =
            sumAt(x1, y1) + sumAt(x0, y0) - sumAt(x1, y0) - sumAt(x0, y1)
}

fun buildIntegralTable(minX: Int = 0, maxX: Int, minY: Int, maxY: Int, valueFunction: (Pair<Int, Int>) -> Long): IntegralTable {
    val map = mutableMapOf<Int, MutableMap<Int, Long>>()
    println("minX $minX")
    for (y in minY..maxY) {
        val row = map.computeIfAbsent(y) { mutableMapOf() }
        for (x in minX..maxX) {
            row[x] = valueFunction(x to y) + (map[y - 1]?.get(x) ?: 0) + (map[y]?.get(x - 1)
                    ?: 0) - (map[y - 1]?.get(x - 1) ?: 0)
        }
    }
    return IntegralTable(map.toMap())
}