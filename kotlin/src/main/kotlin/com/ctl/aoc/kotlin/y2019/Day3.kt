package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException
import kotlin.math.abs

object Day3 {

    data class Point(val x: Int, val y: Int)

    sealed class Direction {
        object R : Direction()
        object L : Direction()
        object D : Direction()
        object U : Direction()


        companion object {
            fun parse(s: String): Direction {
                return when (s) {
                    "R" -> R
                    "L" -> L
                    "D" -> D
                    "U" -> U
                    else -> throw IllegalArgumentException(s)
                }
            }
        }

    }

    fun Direction.move(p: Point): Point {
        val (x, y) = p
        return when (this) {
            Direction.R -> Point(x + 1, y)
            Direction.L -> Point(x - 1, y)
            Direction.D -> Point(x, y - 1)
            Direction.U -> Point(x, y + 1)
        }
    }

    fun Direction.traceMove(p: Point, n: Int): List<Point> {
        return (0 until n).fold(listOf(p)) { acc, _ -> acc + this.move(acc.last()) }.drop(1)
    }


    fun Point.distance(p: Point): Int {
        return abs(this.x - p.x) + abs(this.y - p.y);
    }

    data class WireTurn(val direction: Direction, val n: Int) {
        companion object {
            val regex = """([UDLR])([0-9]+)""".toRegex()
            fun parse(s: String): WireTurn {
                val matchEntire = regex.matchEntire(s);
                val direction = Direction.parse(matchEntire?.groups?.get(1)?.value!!)
                val n = matchEntire.groups[2]?.value?.toInt()!!
                return WireTurn(direction, n)
            }
        }

        fun move(p: Point): List<Point> = direction.traceMove(p, n)
    }


    fun solve1(lines: Sequence<String>): Int {
        val wires = lines.map { line -> line.split(",").map { WireTurn.parse(it) } }

        val tmp: List<List<Point>> = wires.map { wire -> wire.fold(listOf(Point(0, 0))) { acc, turn -> acc + turn.move(acc.last()) } }.toList()
        val wirePoints = tmp
                .map { points -> points.drop(1).toSet() }.toList()

        val intersect = wirePoints[0].intersect(wirePoints[1])
        val (distance, point) = intersect.map { it.distance(Point(0, 0)) to it }.minBy { it.first }!!
        return distance
    }

    fun solve2(lines: Sequence<String>): Int {
        val wires = lines.map { line -> line.split(",").map { WireTurn.parse(it) } }

        val tmp: List<List<Point>> = wires.map { wire -> wire.fold(listOf(Point(0, 0))) { acc, turn -> acc + turn.move(acc.last()) } }.toList()
        val wirePoints = tmp
                .map { points -> points.drop(1).toSet() }.toList()

        val maps: List<Map<Point, Int>> = wirePoints.map { points ->
            points.foldIndexed(mutableMapOf<Point, Int>()) { idx, acc, current ->
                if (acc.containsKey(current)) {
                    println("crossover"); acc
                } else acc[current] = (idx + 1);
                acc
            }
        }

        val intersect = wirePoints[0].intersect(wirePoints[1])

        val (distance, point) = intersect.map { (maps[0][it]!! + maps[1][it]!!) to it }.minBy { it.first }!!

        return distance
    }
}