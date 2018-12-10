package com.ctl.aoc.kotlin.y2018

import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.util.regex.Pattern

object Day10 {

    data class Point(val x: Int, val y: Int, val vx: Int, val vy: Int) {
        fun move(): Point = Point(x + vx, y + vy, vx, vy)
    }

    val pattern = Pattern.compile("position=<([- \\d]+),([- \\d]+)> velocity=<([- \\d]+), ([- \\d]+)>")

    data class Grid(val points: Sequence<Point>, val time: Int, val xMax: Int, val yMax: Int) {
        fun incrementTime(): Grid {
            val result = points.fold(sequenceOf<Point>() to (0 to 0)) { acc, point ->
                val newPoint = point.move()
                (acc.first + sequenceOf(newPoint)) to (Math.max(acc.second.first, newPoint.x) to Math.max(acc.second.second, newPoint.y))
            }
            return Grid(result.first, time + 1, result.second.first, result.second.second)
        }
    }

    fun pointsToMap(points: Sequence<Point>): Map<Int, Map<Int, Boolean>> {
        val map = mutableMapOf<Int, MutableMap<Int, Boolean>>()
        points.forEach {
            val row = map.computeIfAbsent(it.x) { mutableMapOf() }
            row[it.y] = true
        }
        return map.toMap()
    }

    data class Board(val points: Sequence<Point>, val map: Map<Int, Map<Int, Boolean>>, val time: Int) {

        val maxX = map.keys.max() ?: 0
        val minX = map.keys.min() ?: 0
        val maxY = map.values.flatMap { it.keys }.max() ?: 0
        val minY = map.values.flatMap { it.keys }.min() ?: 0

        fun incrementTime(): Board {
            val newPoints = points.map { it.move() }
            return Board(newPoints, pointsToMap(newPoints), time + 1)
        }

        fun avgPerRow(): Double {
            return map.values.map { row -> row.count { it.value } }.average()
        }
    }

    fun print(board: Board): String {
        val builder = StringBuilder()

        for (x in board.minX..board.maxX) {
            val row = board.map[x]

            for (y in board.minY..board.maxY) {
                if (row?.get(y) == true) {
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

    fun solve1(lines: Sequence<String>, iterations: Int, maxX: Int, maxY: Int): Board? {
        val points = lines.map { parse(it) }
        var board = Board(points, pointsToMap(points), 0)

        var maxBoard = board
        for (i in 1..iterations) {
//            if (board.avgPerRow() > maxBoard.avgPerRow()) {
//                maxBoard = board
//            }
            board = board.incrementTime()
            if (board.maxX <= maxX && board.maxY <= maxY){
                return board
            }
        }
        return null
    }
}