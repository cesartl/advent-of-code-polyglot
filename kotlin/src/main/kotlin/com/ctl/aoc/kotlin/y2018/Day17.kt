package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.y2018.Day17.Terrain.*
import java.io.File
import java.util.regex.Pattern

object Day17 {

    val pattern1 = Pattern.compile("y=([\\d]+), x=([\\d]+)\\.\\.([\\d]+)")
    val pattern2 = Pattern.compile("x=([\\d]+), y=([\\d]+)\\.\\.([\\d]+)")

    data class Position(val x: Int, val y: Int) {
        fun down() = copy(x = x, y = y + 1)
        fun up() = copy(x = x, y = y - 1)
        fun left() = copy(x = x - 1, y = y)
        fun right() = copy(x = x + 1, y = y)
    }

    sealed class Terrain {

        override fun toString(): String = this.javaClass.simpleName

        object Clay : Terrain()
        object FallingWater : Terrain()
        object RestWater : Terrain()
    }

    fun buildMap(lines: Sequence<String>): MutableMap<Int, MutableMap<Int, Terrain>> {
        val map = mutableMapOf<Int, MutableMap<Int, Terrain>>()
        lines.forEach { line ->
            val m1 = pattern1.matcher(line)
            if (m1.matches()) {
                val y = m1.group(1).toInt()
                val x1 = m1.group(2).toInt()
                val x2 = m1.group(3).toInt()
                for (x in x1..x2) {
                    map.computeIfAbsent(y) { mutableMapOf() }[x] = Clay
                }
            } else {
                val m2 = pattern2.matcher(line)
                if (m2.matches()) {
                    val x = m2.group(1).toInt()
                    val y1 = m2.group(2).toInt()
                    val y2 = m2.group(3).toInt()
                    for (y in y1..y2) {
                        map.computeIfAbsent(y) { mutableMapOf() }[x] = Clay
                    }
                } else {
                    throw IllegalArgumentException(line)
                }
            }
        }
        return map
    }

    data class Ground(val map: MutableMap<Int, MutableMap<Int, Terrain>>) {
        val minX = map.values.flatMap { it.keys }.minOrNull() ?: 0
        val maxX = map.values.flatMap { it.keys }.maxOrNull() ?: 0
        val maxY = map.keys.maxOrNull() ?: 0
        val minY = map.keys.minOrNull() ?: 0


        fun print(): String {
            val buider = StringBuilder()
            for (y in 0..maxY + 1) {
                for (x in minX-1..maxX + 1) {
                    val terrain = map[y]?.get(x)
                    val s = when (terrain) {
                        is Clay -> "#"
                        is FallingWater -> "|"
                        is RestWater -> "~"
                        else -> "."
                    }
                    buider.append(s)
                }
                buider.append("\n")
            }
            return buider.toString()
        }

        fun at(p: Position): Terrain? = map[p.y]?.get(p.x)
        fun set(p: Position, terrain: Terrain) {
            map.computeIfAbsent(p.y) { mutableMapOf() }[p.x] = terrain
        }

        fun row(y: Int): Map<Int, Terrain> = map.computeIfAbsent(y) { mutableMapOf() }

        fun all() = map.values.flatMap { it.values }
    }

    fun flowWaterDown(p: Position, ground: Ground) {
//        println("flowdown at x=${p.x} y=${p.y}")
        val down = p.down()
        if (down.y <= ground.maxY && ground.at(down) == null) {
            // case 1 water falls down
            ground.set(down, FallingWater)
            flowWaterDown(down, ground)
        } else if ((ground.at(down) == Clay || ground.at(down) == RestWater)) {
            flowWaterHorizontal(p, ground)
        }
    }

    fun flowWaterHorizontal(p: Position, ground: Ground) {
        if (ground.at(p.right()) == FallingWater && ground.at(p.left()) == FallingWater) {
            return
        }
//        println()
//        println("flowWaterHorizontal $p")
//        println(ground.print())
//        println()
        val row = ground.row(p.y)
        val below = ground.row(p.y + 1)

        var left: Int? = null
        var right: Int? = null
        for (x in p.x downTo ground.minX - 1) {
            if (below[x] != Clay && below[x] != RestWater) {
                break
            }
            if (row[x] == Clay) {
                left = x
                break
            }
        }
        for (x in p.x..ground.maxX + 1) {
            if (below[x] != Clay && below[x] != RestWater) {
                break
            }
            if (row[x] == Clay) {
                right = x
                break
            }
        }
//        println("left: $left right: $right")
        if (left != null && right != null) {
            // we are surrounded by clay, we fill the gap
            for (x in (left + 1) until right) {
//                println("rest water at x=$x y=${p.y}")
                ground.set(Position(x, p.y), RestWater)
            }
            // then we need to flow down from one above
            flowWaterDown(p.up(), ground)
        } else {
            // need to find where water can flow down
            var belowLeft: Int? = null
            var belowRight: Int? = null


            for (x in p.x downTo ground.minX - 1) {
                if (below[x] == FallingWater) {
                    break
                }
                if (below[x] == null) {
                    belowLeft = x
                    break
                }
            }
            for (x in p.x..ground.maxX + 1) {
                if (below[x] == FallingWater) {
                    break
                }
                if (below[x] == null) {
                    belowRight = x
                    break
                }
            }

            val leftLimit = left?.let { it + 1 } ?: belowLeft ?: p.x
            val rightLimit = right?.let { it - 1 } ?: belowRight ?: p.x

            for (x in (leftLimit)..(rightLimit)) {
                ground.set(Position(x, p.y), FallingWater)
            }


            if (left == null && belowLeft != null) {
//                println("belowLeft $belowLeft")
                ground.set(Position(belowLeft, p.y), FallingWater)
                flowWaterDown(Position(belowLeft, p.y), ground)
            }

            if (right == null && belowRight != null) {
//                println("belowRight $belowRight")
                ground.set(Position(belowRight, p.y), FallingWater)
                flowWaterDown(Position(belowRight, p.y), ground)
            }
        }
    }

    fun solve1(lines: Sequence<String>, start: Position = Position(500, 0)): Int {
        val ground = Ground(buildMap(lines))
        val minY = ground.minY
        val maxY = ground.maxY

        val originalClay = ground.all().filterIsInstance(Clay.javaClass).count()

        println(ground.print())
        flowWaterDown(start, ground)
        println(ground.print())

        File("day17.txt").writeText(ground.print())

        println("minX: " +ground .minX)
        println("maxX: " +ground .maxX)

        val finalClay = ground.all().filterIsInstance(Clay.javaClass).count()
        if (originalClay != finalClay) {
            throw java.lang.IllegalArgumentException("$originalClay $finalClay")
        }

        val all = ground.map.entries.filter { it.key in minY..maxY }.flatMap { it.value.values }

        println("falling " + all.filterIsInstance(FallingWater.javaClass).count())
        println("rest " + all.filterIsInstance(RestWater.javaClass).count())

        return all.count {
            when (it) {
                is FallingWater, RestWater -> true
                else -> false
            }
        }
    }

    fun solve2(lines: Sequence<String>, start: Position = Position(500, 0)): Int {
        val ground = Ground(buildMap(lines))
        val minY = ground.minY
        val maxY = ground.maxY

        val originalClay = ground.all().filterIsInstance(Clay.javaClass).count()

        println(ground.print())
        flowWaterDown(start, ground)
        println(ground.print())

        val finalClay = ground.all().filterIsInstance(Clay.javaClass).count()
        if (originalClay != finalClay) {
            throw java.lang.IllegalArgumentException("$originalClay $finalClay")
        }

        val all = ground.map.entries.filter { it.key in minY..maxY }.flatMap { it.value.values }

        return all.filterIsInstance(RestWater.javaClass).count()
    }

}