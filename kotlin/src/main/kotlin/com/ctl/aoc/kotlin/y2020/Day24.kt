package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.frequency

object Day24 {

    fun solve1(input: Sequence<String>): Int {
        val allTiles = input.map { Direction.parseDirections(it) }.toList()
        val offsets = allTiles
                .map { directions -> directions.fold(CubeCoordinates(0, 0, 0)) { acc, d -> d.apply(acc) } }
                .toList()
                .frequency()

        return offsets.filter { it.value % 2 == 1 }.count()
    }

    fun solve2(input: Sequence<String>): Int {
        val allTiles = input.map { Direction.parseDirections(it) }.toList()
        val offsets = allTiles
                .map { directions -> directions.fold(CubeCoordinates(0, 0, 0)) { acc, d -> d.apply(acc) } }
                .toList()
                .frequency()
        val grid = Grid(offsets.filter { it.value % 2 == 1 }.map { it.key }.toSet())
        return grid.next(100).blacks
    }

    tailrec fun Grid.next(n: Int): Grid {
        return if (n == 0) this else this.next().next(n - 1)
    }

    data class Grid(val blackTiles: Set<CubeCoordinates>) {

        val blacks by lazy {
            blackTiles.size
        }

        val xs by lazy {
            blackTiles.map { it.x }
        }
        val ys by lazy {
            blackTiles.map { it.y }
        }
        val zs by lazy {
            blackTiles.map { it.z }
        }

        val xRange by lazy {
            (xs.minOrNull()!! - 1)..(xs.maxOrNull()!! + 1)
        }

        val yRange by lazy {
            (ys.minOrNull()!! - 1)..(ys.maxOrNull()!! + 1)
        }

        val zRange by lazy {
            (zs.minOrNull()!! - 1)..(zs.maxOrNull()!! + 1)
        }

        fun countSurroundingBlack(cubeCoordinates: CubeCoordinates): Int {
            return cubeCoordinates.adjacents().count { blackTiles.contains(it) }
        }

        fun next(): Grid {
            val newBlackTiles = mutableSetOf<CubeCoordinates>()
            xRange.forEach { x ->
                yRange.forEach { y ->
                    zRange.forEach { z ->
                        if (x + y + z == 0) {
                            val tile = CubeCoordinates(x, y, z)
                            val blackTile = blackTiles.contains(tile)
                            val surroundingBlack = countSurroundingBlack(tile)
                            if (blackTile) {
                                if (!(surroundingBlack == 0 || surroundingBlack > 2)) {
                                    newBlackTiles.add(tile)
                                }
                            } else {
                                if (surroundingBlack == 2) {
                                    newBlackTiles.add(tile)
                                }
                            }
                        }
                    }
                }
            }
            return Grid(newBlackTiles)
        }
    }

    fun apply(line: String): CubeCoordinates {
        return Direction.parseDirections(line).fold(CubeCoordinates(0, 0, 0)) { acc, d -> d.apply(acc) }
    }

    data class CubeCoordinates(val x: Int, val y: Int, val z: Int) {
        fun adjacents(): Sequence<CubeCoordinates> = sequence {
            listOf(Direction.E, Direction.SE, Direction.SW, Direction.W, Direction.NW, Direction.NE)
                    .map { direction -> direction.apply(this@CubeCoordinates) }
                    .forEach { yield(it) }

        }
    }

    sealed class Direction {
        object E : Direction()
        object SE : Direction()
        object SW : Direction()
        object W : Direction()
        object NW : Direction()
        object NE : Direction()

        fun apply(cubeCoordinates: CubeCoordinates): CubeCoordinates {
            val (x, y, z) = cubeCoordinates
            return when (this) {
                E -> CubeCoordinates(x + 1, y - 1, z)
                SE -> CubeCoordinates(x, y - 1, z + 1)
                SW -> CubeCoordinates(x - 1, y, z + 1)
                W -> CubeCoordinates(x - 1, y + 1, z)
                NW -> CubeCoordinates(x, y + 1, z - 1)
                NE -> CubeCoordinates(x + 1, y, z - 1)
            }
        }


        companion object {
            fun parseDirections(line: String): List<Direction> {
                var from = 0
                var to = 0
                val directions = mutableListOf<Direction>()
                while (to < line.length) {
                    val s = line.substring(from..to)
                    if (s.length > 2) {
                        error(s)
                    }
                    val direction = parse(s)
                    if (direction == null) {
                        to++
                    } else {
                        directions.add(direction)
                        from = to + 1
                        to += 1
                    }
                }
                return directions
            }

            fun parse(s: String): Direction? {
                return when (s) {
                    "e" -> E
                    "se" -> SE
                    "sw" -> SW
                    "w" -> W
                    "nw" -> NW
                    "ne" -> NE
                    else -> null
                }
            }
        }

        override fun toString(): String {
            return when (this) {
                E -> "E"
                SE -> "SE"
                SW -> "SW"
                W -> "W"
                NW -> "NW"
                NE -> "NE"
            }
        }
    }
}