package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.rangeIntersect
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

object Day22 {

    fun IntRange.size(): Long {
        return (this.last - this.first + 1).toLong()
    }

    data class Cuboid(
        val xRange: IntRange,
        val yRange: IntRange,
        val zRange: IntRange,
    ) {
        val size: Long by lazy {
            xRange.size() * yRange.size() * zRange.size()
        }
    }

    sealed class Region() {
        abstract val cuboid: Cuboid

        data class OnRegion(override val cuboid: Cuboid) : Region()

        data class OffRegion(override val cuboid: Cuboid) : Region()

        companion object {
            val regex = """(on|off) x=([-\d]+)\.\.([-\d]+),y=([-\d]+)\.\.([-\d]+),z=([-\d]+)\.\.([-\d]+)""".toRegex()
            fun parse(s: String): Region {
                val match = regex.matchEntire(s)!!
                val values = match.groupValues
                val xRange = values[2].toInt()..values[3].toInt()
                val yRange = values[4].toInt()..values[5].toInt()
                val zRange = values[6].toInt()..values[7].toInt()
                return if (values[1] == "on") {
                    OnRegion(Cuboid(xRange, yRange, zRange))
                } else {
                    OffRegion(Cuboid(xRange, yRange, zRange))
                }
            }
        }
    }

    fun IntRange.contains(other: IntRange): Boolean {
        return other.first in this && other.last in this
    }

    fun Cuboid.contains(other: Cuboid): Boolean {
        val xCheck = this.xRange.contains(other.xRange)
        val yCheck = this.yRange.contains(other.yRange)
        val zCheck = this.zRange.contains(other.zRange)
        return xCheck && yCheck && zCheck
    }


    private fun Cuboid.intersect(other: Cuboid): Cuboid? {
        val (x1, y1, z1) = this
        val (x2, y2, z2) = other
        val X = x1.rangeIntersect(x2)
        val Y = y1.rangeIntersect(y2)
        val Z = z1.rangeIntersect(z2)
        if (X != null && Y != null && Z != null) {
            return Cuboid(X, Y, Z)
        }
        return null
    }

    fun solve1(input: Sequence<String>): Long {
        val limit = -50..50
        val boundary = Cuboid(limit, limit, limit)
        val regions = input.map { Region.parse(it) }
            .filter { boundary.contains(it.cuboid) }
            .toList()
        return smartCount(regions)
    }

    fun solve2(input: Sequence<String>): Long {
        val regions = input.map { Region.parse(it) }
            .toList()
        return smartCount(regions)
    }

    private fun smartCount(regions: List<Region>): Long {
        val allCounts = mutableMapOf<Cuboid, Long>()
        regions.forEach { region ->
            val newCounts = mutableMapOf<Cuboid, Long>()
            allCounts.forEach { (cube, c) ->
                cube.intersect(region.cuboid)?.let { intersect ->
                    newCounts[intersect] = (newCounts[intersect] ?: 0L) - c
                }
            }
            when (region) {
                is Region.OnRegion -> {
                    newCounts[region.cuboid] = (newCounts[region.cuboid] ?: 0L) + 1
                }
                is Region.OffRegion -> {
                }
            }
            newCounts.forEach { (cuboid, l) ->
                allCounts.merge(cuboid, l) { x, y -> x + y }
            }
            allCounts.filter { (_, c) -> c == 0L }.forEach { (cuboid, _) -> allCounts.remove(cuboid) }
        }
        return allCounts.asSequence().sumOf { (cube, l) -> l * cube.size }
    }
}