package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Lists

object Day9 {

    data class Distance(val from: String, val to: String, val d: Int) {
        companion object {
            val regex = """([\w]+) to ([\w]+) = ([\d]+)""".toRegex()
            fun parse(string: String): Distance {
                return regex.matchEntire(string)?.let { Distance(it.groupValues[1], it.groupValues[2], it.groupValues[3].toInt()) }
                        ?: throw IllegalArgumentException(string)
            }
        }
    }

    data class Route(val destinations: List<String>)

    data class DistanceMap(val map: Map<String, Map<String, Int>>) {

        val cities: Set<String> = map.keys + map.values.flatMap { it.keys }

        fun distance(from: String, to: String): Int = map[from]?.get(to) ?: (map[to]!![from]!!)

        fun distance(destinations: List<String>): Int {
            val first = destinations.first()
            val foo = destinations.drop(1).fold(first to 0) { (prev, total), current -> current to (total + distance(prev, current)) }
            return foo.second
        }

        companion object {
            fun from(distances: Sequence<Distance>): DistanceMap {
                val map = mutableMapOf<String, MutableMap<String, Int>>()

                distances.forEach { (from, to, d) -> map.computeIfAbsent(from) { mutableMapOf() }[to] = d }

                return DistanceMap(map)
            }
        }
    }

    fun solve1(lines: Sequence<String>): Int {
        val distanceMap = DistanceMap.from(lines.map { Distance.parse(it) })
        val destinations = distanceMap.cities.toList()
        println(destinations)
        return Lists.permutations(destinations).map { distanceMap.distance(it) }.minOrNull() ?: 0
    }

    fun solve2(lines: Sequence<String>): Int {
        val distanceMap = DistanceMap.from(lines.map { Distance.parse(it) })
        val destinations = distanceMap.cities.toList()
        println(destinations)
        return Lists.permutations(destinations).map { distanceMap.distance(it) }.minOrNull() ?: 0
    }
}