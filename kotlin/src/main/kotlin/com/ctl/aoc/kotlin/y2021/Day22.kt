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

    // -- coordinate compression

    fun solve1Bis(input: Sequence<String>): Long {
        val limit = -50..50
        val boundary = Cuboid(limit, limit, limit)
        val regions = input.map { Region.parse(it) }
            .filter { boundary.contains(it.cuboid) }
            .toList()
        return coordinateCompressionCount(regions)
    }

    fun solve2Bis(input: Sequence<String>): Long {
        val regions = input.map { Region.parse(it) }
            .toList()
        return coordinateCompressionCount(regions)
    }

    @JvmInline
    value class Grid(val grid: Array<Array<Array<Boolean>>>) {

        fun set(x: Int, y: Int, z: Int) {
            grid[x][y][z] = true
        }

        fun unSet(x: Int, y: Int, z: Int) {
            grid[x][y][z] = false
        }

        fun test(x: Int, y: Int, z: Int): Boolean {
            return grid[x][y][z]
        }

        companion object {
            fun empty(size: Int): Grid {
                val grid = Array(size) {
                    Array(size) {
                        Array(size) { false }
                    }
                }
                return Grid(grid)
            }
        }
    }

    private fun coordinateCompressionCount(regions: List<Region>): Long {
        val tmpX = ArrayList<Int>(regions.size)
        val tmpY = ArrayList<Int>(regions.size)
        val tmpZ = ArrayList<Int>(regions.size)

        regions.forEach { region ->
            val cuboid = region.cuboid
            tmpX.add(cuboid.xRange.first)
            tmpX.add(cuboid.xRange.last)
            tmpY.add(cuboid.yRange.first)
            tmpY.add(cuboid.yRange.last)
            tmpZ.add(cuboid.zRange.first)
            tmpZ.add(cuboid.zRange.last)
        }
        val n = tmpX.size
        val compressedX = tmpX.sorted()
        val compressedY = tmpY.sorted()
        val compressedZ = tmpZ.sorted()

        val grid = Grid.empty(n)

        regions.forEach { region ->
            val (xRange, yRange, zRange) = region.cuboid
            val xRangeCompressed = compressedX.compressedRange(xRange)
            val yRangeCompressed = compressedY.compressedRange(yRange)
            val zRangeCompressed = compressedZ.compressedRange(zRange)

            xRangeCompressed.forEach { x ->
                yRangeCompressed.forEach { y ->
                    zRangeCompressed.forEach { z ->
                        region.restart(grid, x, y, z)
                    }
                }
            }
        }

        var count = 0L
        (0 until n - 1).forEach { xIdx ->
            (0 until n - 1).forEach { yIdx ->
                (0 until n - 1).forEach { zIdx ->
                    if (grid.test(xIdx, yIdx, zIdx)) {
                        var tmp = (compressedX[xIdx + 1] - compressedX[xIdx] + 1).toLong()
                        tmp *= (compressedY[yIdx + 1] - compressedY[yIdx] + 1).toLong()
                        tmp *= (compressedZ[zIdx + 1] - compressedZ[zIdx] + 1).toLong()
//                        assert(tmp != 0) { "zero" }
                        assert(tmp >= 0) { "overflow" }
                        count += tmp
                    }
                }
            }
        }
        return count
    }

    private fun Region.restart(grid: Grid, x: Int, y: Int, z: Int) {
        when (this) {
            is Region.OffRegion -> grid.unSet(x, y, z)
            is Region.OnRegion -> grid.set(x, y, z)
        }
    }

    private fun List<Int>.compressedRange(range: IntRange): IntRange {
        val startIdx = this.binarySearch { it - range.first }
        val endIdx = this.binarySearch { it - range.last }
        assert(startIdx != -1) { "start idx ${range.first} not found" }
        assert(endIdx != -1) { "end idx ${range.last} not found" }
        return (startIdx until endIdx)
    }
}