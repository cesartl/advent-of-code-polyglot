package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.Matrix22
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.frequency
import kotlin.math.sqrt


object Day20 {

    fun solve1(input: String): Long {
        val tiles = input.split("\n\n").map { Tile.parse(it) }
        println(tiles.size)
        val n = sqrt(tiles.size.toDouble()).toInt()
        println("n: $n")
//        val grid = generateAllGrids(currentGrid = Grid(listOf(), n), allTiles = tiles.toSet(), size = n).firstOrNull()
//        println(grid)

        val borders = mutableListOf<List<Boolean>>()
        tiles.forEach {
            borders.addAll(it.allBorders())
        }
        val freq = borders.toList().frequency()
        return tiles.map { tile ->
            tile.id to tile.allBorders().count { freq[it] == 1 }
        }.filter { it.second == 4 }
                .map { it.first }.fold(1L) { acc, i -> acc * i }
    }

    fun generateAllGrids(currentGrid: Grid, allTiles: Set<Tile>, size: Int): Sequence<Grid> = sequence {
        if (allTiles.isEmpty()) {
            yield(currentGrid)
        } else {
            allTiles.forEach { tile ->
                val nextTiles = allTiles - tile
                tile.allVariations().forEach { variation ->
                    if (currentGrid.canAdd(variation)) {
                        yieldAll(generateAllGrids(currentGrid.copy(tiles = currentGrid.tiles + variation), nextTiles, size))
                    }
                }
            }
        }
    }

    data class Grid(val tiles: List<Tile>, val n: Int) {

        fun isValid(): Boolean {
            return allRowsValid() && allColumnsValid()
        }

        fun canAdd(tile: Tile): Boolean {
            val i = tiles.size
//            println("i $i")
            val x = i % n
            val y = i / n
//            val c1 = { getOrNull(x + 1, y)?.let { it.leftBorder == tile.rightBorder } ?: true }
            val c2 = {
                getOrNull(x - 1, y)?.let { it.rightBorder == tile.leftBorder || it.rightBorder.reversed() == tile.leftBorder }
                        ?: true
            }
//            val c3 = { getOrNull(x, y + 1)?.let { it.topBorder == tile.bottomBorder } ?: true }
            val c4 = {
                getOrNull(x, y - 1)?.let { it.bottomBorder == tile.topBorder || it.bottomBorder.reversed() == tile.topBorder }
                        ?: true
            }
            return c2() && c4()
        }

        private fun allRowsValid() = (0 until n).all { isRowValid(it) }
        private fun allColumnsValid() = (0 until n).all { isColumnValid(it) }

        private fun isRowValid(y: Int): Boolean {
            return (0 until n - 1).all { x -> get(x, y).rightBorder == get(x + 1, y).leftBorder }
        }

        private fun isColumnValid(x: Int): Boolean {
            return (0 until n - 1).all { y -> get(x, y).bottomBorder == get(x, y + 1).topBorder }
        }

        private fun getOrNull(x: Int, y: Int): Tile? {
            return tiles.getOrNull(x + n * y)
        }

        private fun get(x: Int, y: Int): Tile {
            return tiles[x + n * y]
        }
    }

    data class Tile(val id: Long, val pixels: Set<Position>) {

        val xs by lazy { pixels.map { it.x } }
        val ys by lazy { pixels.map { it.y } }
        val xRange by lazy { (xs.min() ?: 0)..(xs.max() ?: 0) }
        val yRange by lazy { (ys.min() ?: 0)..(ys.max() ?: 0) }


        val topBorder: List<Boolean> by lazy {
            xRange.map { x -> hasPixel(x, yRange.last) }
        }

        val bottomBorder: List<Boolean> by lazy {
            xRange.map { x -> hasPixel(x, yRange.first) }
        }

        val leftBorder: List<Boolean> by lazy {
            yRange.map { y -> hasPixel(xRange.first, y) }
        }

        val rightBorder: List<Boolean> by lazy {
            yRange.map { y -> hasPixel(xRange.last, y) }
        }


        fun allBorders(): List<List<Boolean>> {
            val borders = mutableListOf<List<Boolean>>()
            borders.add(this.topBorder)
            borders.add(this.topBorder.reversed())
            borders.add(this.rightBorder)
            borders.add(this.rightBorder.reversed())
            borders.add(this.bottomBorder)
            borders.add(this.bottomBorder.reversed())
            borders.add(this.leftBorder)
            borders.add(this.leftBorder.reversed())
            return borders
        }

        fun hasPixel(x: Int, y: Int) = pixels.contains(Position(x, y))


        private fun rotate(matrix22: Matrix22): Tile {
            val newPixels = pixels.map { matrix22 x it }.map { (x00, x10) -> Position(x00, x10) }.toSet()
            return this.copy(pixels = newPixels)
        }

        fun rotate90() = rotate(Matrix22.rotate90)
        fun rotate180() = rotate(Matrix22.rotate180)
        fun rotate270() = rotate(Matrix22.rotate270)

        fun flipH(): Tile {
            return this.copy(pixels = pixels.map { (x, y) -> Position(x, -y) }.toSet())
        }

        fun flipV(): Tile {
            return this.copy(pixels = pixels.map { (x, y) -> Position(-x, y) }.toSet())
        }

        fun allRotated(): Sequence<Tile> = generateSequence(this) { it.rotate90() }.take(4)

        fun allVariations(): Sequence<Tile> = sequence {
            yieldAll(allRotated())
            val flipV = flipV()
            yield(flipV.rotate90())
            yield(flipV.rotate180())
            val flipH = flipH()
            yield(flipH.rotate90())
            yield(flipH.rotate180())
        }

        fun print() {
            println("x:$xRange y:$yRange")
            (yRange).reversed().forEach { y ->
                (xRange).forEach { x ->
                    if (hasPixel(x, y)) {
                        print('#')
                    } else {
                        print('.')
                    }
                }
                println()
            }
            println("----------")
        }

        companion object {
            private val idRegex = """Tile ([\d]+):""".toRegex()
            fun parse(s: String): Tile {
                val lines = s.split("\n")
                val id = idRegex.matchEntire(lines[0])?.let { it.groupValues[1].toLong() } ?: error(lines[0])
                val pixels = mutableSetOf<Position>()
                lines.drop(1).forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        if (c == '#') {
                            pixels.add(Position(x, 9 - y))
                        }
                    }
                }
                return Tile(id, pixels)
            }
        }
    }

    fun solve2(input: String): Int {
        TODO()
    }
}