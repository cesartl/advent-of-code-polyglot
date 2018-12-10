package com.ctl.aoc.kotlin.y2018

import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.util.regex.Pattern

object Day10 {

    data class Point(val x: Int, val y: Int, val vx: Int, val vy: Int) {
        fun move(): Point = Point(x + vx, y + vy, vx, vy)
    }

    val pattern = Pattern.compile("position=<([- \\d]+),([- \\d]+)> velocity=<([- \\d]+), ([- \\d]+)>")

    fun pointsToMap(points: Sequence<Point>): MutableMap<Int, MutableMap<Int, List<Point>>> {
        val map = mutableMapOf<Int, MutableMap<Int, List<Point>>>()
        points.forEach {
            val row = map.computeIfAbsent(it.x) { mutableMapOf() }
            row.merge(it.y, listOf(it)){ l, x -> l + x}
        }
        return map
    }

    data class Board(val map: MutableMap<Int, MutableMap<Int, List<Point>>>, val time: Int) {

        val maxX = map.keys.max() ?: 0
        val minX = map.keys.min() ?: 0
        val maxY = map.values.flatMap { it.keys }.max() ?: 0
        val minY = map.values.flatMap { it.keys }.min() ?: 0

        val width = maxX - minX
        val height = maxY - minY

        fun incrementTime(): Board {
            val newPoints = map.values.asSequence().flatMap { it.values.asSequence() }.flatten().map { it.move() }
            return Board(pointsToMap(newPoints), time + 1)
        }

//        fun avgPerRow(): Double {
//            return map.values.map { row -> row.count { it.value } }.average()
//        }
    }

    fun print(board: Board): String {
        val builder = StringBuilder()

        val min = Math.min(board.minX, board.minY)
        val max = Math.max(board.maxX, board.maxY)

        for (x in min..board.maxX) {
            val row = board.map[x]

            for (y in (board.maxY + 1) downTo  (min -1)) {
                if (row?.get(y)?.isNotEmpty() == true) {
                    builder.append("#")
                } else {
                    builder.append(".")
                }
            }
            builder.append("\n")
        }
        builder.append("\n")
        return builder.toString()
    }

    fun parse(line: String): Point {
        val m = pattern.matcher(line)
        if (m.matches()) {
            return Point(m.group(1).trim().toInt(), m.group(2).trim().toInt(), m.group(3).trim().toInt(), m.group(4).trim().toInt())
        }
        throw IllegalArgumentException(line)
    }

    fun solve1(lines: Sequence<String>, iterations: Int, maxHeight: Int): Board? {
        val points = lines.map { parse(it) }
        var board = Board(pointsToMap(points), 0)

        var maxBoard = board
        var width = Int.MAX_VALUE
        var height = Int.MAX_VALUE
        for (i in 1..iterations) {
            if(board.height < height){
                height = board.height
//                println("height $height")
            }
            if (board.height < maxHeight) {
                return board
            }
            board = board.incrementTime()
        }
        return null
    }
}