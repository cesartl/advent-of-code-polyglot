package com.ctl.aoc.kotlin.y2018

import java.lang.IllegalArgumentException
import java.util.regex.Pattern

val pattern = Pattern.compile("#([\\w]+) @ ([\\d]+),([\\d]+): ([\\d]+)x([\\d]+)")

data class Claim(val id: String, val left: Int, val top: Int, val width: Int, val height: Int)

typealias Position = Pair<Int, Int>

object Day3p1 {
    fun parseClaim(s: String): Claim {
        val m = pattern.matcher(s)
        if (m.matches()) {
            return Claim(
                    m.group(1),
                    m.group(2).toInt(),
                    m.group(3).toInt(),
                    m.group(4).toInt(),
                    m.group(5).toInt()
            )
        }
        throw IllegalArgumentException(s)
    }

    fun coveredAreas(claim: Claim): List<Position> {
        val list = mutableListOf<Position>()
        for (i in claim.left until claim.left + claim.width) {
            for (j in claim.top until claim.top + claim.height) {
                list.add(i to j)
            }
        }
        return list
    }

    fun intersect(c1: Claim, c2: Claim, cache: MutableMap<Claim, List<Position>> = mutableMapOf()): Boolean {
        // todo write more efficiently
        val area1 = cache.computeIfAbsent(c1) { coveredAreas(it) }
        val area2 = cache.computeIfAbsent(c2) { coveredAreas(it) }
        val both = area1 + area2
        return both.size != both.distinct().size
    }

    fun solve(lines: Sequence<String>): Int {
        val claims = lines.map { parseClaim(it) }
//        claims.forEach { println("for $it ${coveredAreas(it)}") }
        val map = mutableMapOf<Position, Int>()
        val areas = claims.flatMap { coveredAreas(it).asSequence() }
//        println(areas.toList())
        areas.forEach {
            map.merge(it, 1) { a, b -> a + b }
        }
//        println(map)
        return map.count { it.value > 1 }
    }

    fun solve2(lines: Sequence<String>): String? {
        val claims = lines.map { parseClaim(it) }
        val cache: MutableMap<Claim, List<Position>> = mutableMapOf()
        return claims.find { claim -> claims.filter { it.id != claim.id }.all { !intersect(claim, it, cache) } }?.id

    }
}