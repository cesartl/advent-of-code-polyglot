package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.frequency
import com.ctl.aoc.kotlin.utils.timedMs
import kotlin.math.sqrt


object Day20 {

    fun solve1(input: String): Long {
        val tiles = input.split("\n\n").map { Tile.parse(it) }

        val borders = mutableListOf<List<Boolean>>()
        tiles.forEach {
            borders.addAll(it.allBorders())
        }
        val freq = borders.toList().frequency()
        val corners = tiles.map { tile ->
            tile.id to tile.allBorders().count { freq[it] == 1 }
        }.filter { it.second == 4 }.map { it.first }
        println("Corners: $corners")
        return corners.fold(1L) { acc, i -> acc * i }
    }

    fun solve2(input: String): Int {
        val tiles = input.split("\n\n").map { Tile.parse(it) }
        val n = sqrt(tiles.size.toDouble()).toInt()

        val tilesByBorder = mutableMapOf<List<Boolean>, MutableList<Tile>>()
        tiles.forEach { tile ->
            tile.allBorders().forEach { border ->
                tilesByBorder.computeIfAbsent(border) { mutableListOf() }.add(tile)
            }
        }
        val corners = tiles.filter { tile ->
            tile.allBorders().filter { (tilesByBorder[it] ?: mutableListOf()).size == 2 }.count() == 4
        }
        println("Corners: ${corners.map { it.id }}")

        val tilesSearch = TilesSearch(tilesByBorder)

        val grid = corners.asSequence()
                .flatMap { it.allVariations() }
                .mapNotNull { buildGrid(it, tilesSearch, n) }
                .firstOrNull() ?: error("not found")

        println(grid.corners().map { it.id })
        println(grid.corners().map { it.id }.fold(1L) { acc, i -> acc * i })
        val merged = grid.merge()
        val (finalTile, positions) = merged.allVariations()
                .map {
                    val (time, positions) = timedMs { it.findPattern(Tile.seaMonsterPositions) }
                    println("Found sea monsters in $time")
                    it to positions
                }
                .find { (tile, positions) -> positions.isNotEmpty() }
                ?: error("not found")
        val seaMonsters = positions.flatMap { (xOffset, yOffset) ->
            val offset = Position(xOffset, yOffset)
            Tile.seaMonsterPositions.map { it + offset }
        }
        println("Sea monsters: $positions")
        merged.print(seaMonsters.toSet())
        return (finalTile.pixels - seaMonsters).size
    }

    private fun buildGrid(start: Tile, tilesSearch: TilesSearch, n: Int): Grid? {
        val currentGrid = Grid(mutableListOf(), n)
        var current = start
        var firstInRow: Tile
        (0 until n).forEach { k ->
            firstInRow = current
            currentGrid.tiles.add(firstInRow)
            (0 until n - 1).forEach { _ ->
                val matchingTile = tilesSearch.findMatch(current, currentGrid) { it.rightBorder }
                        ?: return null
                val matches = matchingTile.allVariations().filter { it.leftBorder == current.rightBorder }
                if (matches.count() > 1) {
                    error("too many matches: ${matches.count()}")
                }
                val tile = matches.firstOrNull() ?: error("Could not rotate tile to match")
                currentGrid.tiles.add(tile)
                current = tile
            }
            if (k < n - 1) {
                val matchingNextRow = tilesSearch.findMatch(firstInRow, currentGrid) { it.bottomBorder }
                        ?: return null
                val matches = matchingNextRow.allVariations()
                        .filter { it.topBorder == firstInRow.bottomBorder }
                if (matches.count() > 1) {
                    error("too many matches: ${matches.count()}")
                }
                current = matches.firstOrNull() ?: error("Could not rotate tile to match")
            }
        }
        println("Found grid with ${currentGrid.tiles.size}")
        return currentGrid
    }

    data class TilesSearch(val tilesByBorder: Map<List<Boolean>, List<Tile>>) {
        fun findMatch(tile: Tile, grid: Grid, from: (Tile) -> List<Boolean>): Tile? {
            val fromBorder = from(tile)
            val matches = listOf(fromBorder, fromBorder.reversed())
                    .flatMap { border ->
                        tilesByBorder[border]?.let { tiles -> tiles.filter { it.id != tile.id && !grid.usedTiles().contains(it.id) } }
                                ?: listOf()
                    }
                    .distinct()
            if (matches.size > 1) {
                error("too many matches: ${matches.size}")
            }
            return matches.firstOrNull()
        }
    }

    data class Grid(val tiles: MutableList<Tile>, val n: Int) {
        fun merge(): Tile {
            val k = 8
            val mergedPixels = tiles.mapIndexed { index, tile ->
                val xOffset = (index % n) * k
                val yOffset = (n - 1 - index / n) * k
                tile.removeBorders().pixels.map { (x, y) -> Position(x + xOffset, y + yOffset) }
            }.flatten().toSet()
            return Tile(0, mergedPixels)
        }

        fun corners(): List<Tile> = listOf(
                tiles[0], tiles[n - 1], tiles[n * n - n], tiles[n * n - 1]
        )

        fun usedTiles(): Set<Long> = tiles.map { it.id }.toSet()

    }

    data class Tile(val id: Long, val pixels: Set<Position>) {

        val xs by lazy { pixels.map { it.x } }
        val ys by lazy { pixels.map { it.y } }
        val xRange by lazy { (xs.minOrNull() ?: 0)..(xs.maxOrNull() ?: 0) }
        val yRange by lazy { (ys.minOrNull() ?: 0)..(ys.maxOrNull() ?: 0) }


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

        fun normalise(): Tile {
            var xOffset = 0
            val delta = xRange.last - xRange.first
            xOffset += (-xRange.first).coerceAtLeast(0)
            xOffset -= (delta - xRange.last).coerceAtMost(0)

            var yOffset = 0
            yOffset += (-yRange.first).coerceAtLeast(0)
            yOffset -= (delta - yRange.last).coerceAtMost(0)

            val offset = Position(xOffset, yOffset)
            return this.copy(pixels = pixels.map { it + offset }.toSet())
        }

        fun removeBorders(): Tile {
            return this.copy(pixels = pixels.mapNotNull { (x, y) ->
                if (x == xRange.first || x == xRange.last || y == yRange.first || y == yRange.last) {
                    null
                } else {
                    Position(x - 1, y - 1)
                }
            }.toSet())
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


        private fun rotate(r: (Position) -> Position): Tile {
            val newPixels = pixels.map { r(it) }.toSet()
            return this.copy(pixels = newPixels)
        }

        private fun rotate90() = rotate { it.rotate90() }
        private fun rotate180() = rotate { it.rotate180() }

        fun flipH(): Tile {
            return this.copy(pixels = pixels.map { (x, y) -> Position(x, -y) }.toSet())
        }

        fun flipV(): Tile {
            return this.copy(pixels = pixels.map { (x, y) -> Position(-x, y) }.toSet())
        }

        private fun allRotated(): Sequence<Tile> = generateSequence(this) { it.rotate90() }.take(4)

        fun allVariations(): Sequence<Tile> = sequence {
            yieldAll(allRotated())
            val flipV = flipV()
            yield(flipV.rotate90())
            yield(flipV.rotate180())
            val flipH = flipH()
            yield(flipH.rotate90())
            yield(flipH.rotate180())
        }.map { it.normalise() }

        fun findPattern(positions: Set<Position>): Set<Position> {
            val found = mutableSetOf<Position>()
            (yRange).reversed().forEach { yOffset ->
                (xRange).forEach { xOffset ->
                    val offset = Position(xOffset, yOffset)
                    val ps = positions.asSequence().map { it + offset }
                    if (ps.all { pixels.contains(it) }) {
                        found.add(Position(xOffset, yOffset))
                    }
                }
            }
            return found
        }

        fun print(specialPositions: Set<Position> = setOf()) {
            println("x:$xRange y:$yRange")
            (yRange).reversed().forEach { y ->
                (xRange).forEach { x ->
                    when {
                        specialPositions.contains(Position(x, y)) -> {
                            print('O')
                        }
                        hasPixel(x, y) -> {
                            print('#')
                        }
                        else -> {
                            print('.')
                        }
                    }
                }
                println()
            }
            println("----------")
        }

        companion object {
            private val idRegex = """Tile ([\d]+):""".toRegex()

            private const val seaMonsterPattern = """                  # 
#    ##    ##    ###
 #  #  #  #  #  #"""

            val seaMonsterPositions = parsePixels(seaMonsterPattern.split("\n"))

            fun parse(s: String): Tile {
                val lines = s.split("\n")
                val id = idRegex.matchEntire(lines[0])?.let { it.groupValues[1].toLong() } ?: error(lines[0])
                return Tile(id, parsePixels(lines))
            }

            private fun parsePixels(lines: List<String>): Set<Position> {
                val pixels = mutableSetOf<Position>()
                lines.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        if (c == '#') {
                            pixels.add(Position(x, lines.size - 1 - y))
                        }
                    }
                }
                return pixels
            }
        }
    }
}