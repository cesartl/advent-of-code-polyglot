package com.ctl.aoc.kotlin.y2018

import java.util.regex.Pattern

object Day23 {

    val pattern = Pattern.compile("pos=<([-\\d]+),([-\\d]+),([-\\d]+)>, r=([\\d]+)")

    data class Nanobot(val x: Long, val y: Long, val z: Long, val r: Long) {
        var d0 = Math.abs(x) + Math.abs(y) + Math.abs(z)
        fun distance(other: Nanobot): Long = Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z)

        fun plus(other: Nanobot) = copy(x = x + other.x, y = y + other.y, z = z + other.z)

        fun intersect(other: Nanobot) = other.distance(this) <= other.r + r
    }

    fun parse(string: String): Nanobot {
        val m = pattern.matcher(string)
        if (m.matches()) {
            val x = m.group(1).toLong()
            val y = m.group(2).toLong()
            val z = m.group(3).toLong()
            val r = m.group(4).toLong()
            return Nanobot(x, y, z, r)
        }
        throw IllegalArgumentException(string)
    }

    fun solve1(lines: Sequence<String>): Int {
        val bots = lines.map { parse(it) }

        val strongest = bots.maxBy { it.r }!!

        return bots.count { it.distance(strongest) <= strongest.r }
    }

    fun foo(nanobot: Nanobot) {
        val (x, y, z) = nanobot
        listOf(x, y, z)
    }

    fun foo(lines: Sequence<String>): Long {
        val origin = Nanobot(0, 0, 0, 0)
        val bots = lines.map { parse(it) }.toList()

        val maxDistance = bots.maxBy { it.d0 }?.d0 ?: 0L
        println("maxDistance $maxDistance")
        var max = 0
        var bestDistance = 0L
        var count: Int
        var start = 79253312L
        var remaining = bots
        for (d in start..maxDistance) {
            remaining = remaining.filter { it.d0 > d + it.r }
            count = (bots.size - remaining.size)
            if (count > max) {
                max = count
                bestDistance = d
                println("max: $max d: $d")
            }

        }
        return bestDistance
    }

    fun solve2(lines: Sequence<String>): Long {
        val origin = Nanobot(0, 0, 0, 0)
        val bots = lines.map { parse(it) }.toList()

        val sum = bots.fold(Nanobot(0, 0, 0, 0)) { acc, (x, y, z, r) ->
            Nanobot(x = acc.x + x * r, y = acc.y + y * r, z = acc.z + z * r, r = 0)
        }

        val sumR = bots.map { it.r }.sum()

        val avg = sum.copy(x = sum.x / sumR, y = sum.y / sumR, z = sum.z / sumR)

        var target = Nanobot(x = 14977516, y = 34183016, z = 57162559, r = 0)

        val xRange = 50L
        val yRange = 50L
        val zRange = 50L
        var current: Nanobot
        var maxCount = 0
        var bestDistance = Long.MAX_VALUE
        var count: Int
        for (x in (target.x - xRange)..(target.x + xRange)) {
            for (y in (target.y - yRange)..(target.y + yRange)) {
                for (z in (target.z - zRange)..(target.z + zRange)) {
                    current = Nanobot(x, y, z, 0)
                    count = bots.count { it.distance(current) <= it.r }
                    if (count > maxCount || (count == maxCount && current.d0 < bestDistance)) {
                        bestDistance = current.d0
                        maxCount = count
                        println("max: $maxCount bestDistance: $bestDistance")
                    }
                }
            }
        }

//        val min = listOf(avg.x, avg.y, avg.z).min()!!
//        val vect = avg.copy(x = -avg.x / min, y = -avg.y / min, z = -avg.z / min)
//        println(vect)
//
//        current = avg
//        while (current.d0 > 1000) {
//            count = bots.count { it.distance(current) <= it.r }
//            if (count > maxCount || (count == maxCount && current.d0 < bestDistance)) {
//                bestDistance = current.d0
//                maxCount = count
//                println("max: $maxCount bestDistance: $bestDistance current: $current")
//            }
//            current = current.plus(vect)
//        }
        return bestDistance
    }

    fun solve2bis(lines: Sequence<String>) {
        val bots = lines.map { parse(it) }.toList()

        val bestSet = bots.map { b -> bots.filter { it.intersect(b) } }.maxBy { it.size }!!
        println(bestSet.size)
        println(bestSet)
    }
}