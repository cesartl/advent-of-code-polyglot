package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Position

object Day14 {

    sealed class Terrain {
        object Sand : Terrain()
        object Rock : Terrain()
    }

    data class Cave(val terrainMap: MutableMap<Position, Terrain>) {
        val xRange: IntRange
            get() = (terrainMap.keys.minOfOrNull { it.x } ?: 0)..(terrainMap.keys.maxOfOrNull { it.x } ?: 0)
        val yRange: IntRange
            get() = (terrainMap.keys.minOfOrNull { it.y } ?: 0)..(terrainMap.keys.maxOfOrNull { it.y } ?: 0)

        val yMax: Int = terrainMap.keys.maxOfOrNull { it.y } ?: 0
    }

    fun Cave.print(): String {
        val builder = StringBuilder()
        this.yRange.forEach { y ->
            this.xRange.forEach { x ->
                val p = Position(x, y)
                val s = when (terrainMap[p]) {
                    Terrain.Rock -> "#"
                    Terrain.Sand -> "o"
                    null -> "."
                }
                builder.append(s)
            }
            builder.append("\n")
        }
        return builder.toString()
    }

    private fun buildCave(input: Sequence<String>): Cave {
        val terrainMap: MutableMap<Position, Terrain> = input
            .flatMap { line ->
                line.splitToSequence(" -> ")
                    .map {
                        val s = it.split(",")
                        Position(s[0].toInt(), s[1].toInt())
                    }
                    .zipWithNext { from, to -> drawLine(from, to) }
                    .flatten()
            }.associateWith { Terrain.Rock }
            .toMutableMap()
        return Cave(terrainMap)
    }
    private fun drawLine(from: Position, to: Position): Sequence<Position> {
        val dir = (to - from).normalise()
        return generateSequence(from) { it + dir }
            .takeWhile { (it - dir) != to }
    }

    private fun Position.down(): Position = this + Position(0, 1)
    private fun Position.downLeft(): Position = this + Position(-1, 1)
    private fun Position.downRight(): Position = this + Position(1, 1)
    private fun Cave.dropSand(): Cave? {
        var current = Position(500, 0)
        while (current.y <= this.yMax) {
            val next = sequenceOf(current.down(), current.downLeft(), current.downRight())
                .firstOrNull { !this.terrainMap.containsKey(it) }
            if (next == null) {
                if (this.terrainMap.containsKey(current)) {
                    return null
                }
                this.terrainMap[current] = Terrain.Sand
                return this
            }
            current = next
        }
        return null
    }

    fun solve1(input: Sequence<String>): Int {
        val cave = buildCave(input)
        val last = generateSequence(cave) { it.dropSand() }.last()
        return last.terrainMap.count { it.value is Terrain.Sand }
    }

    fun solve2(input: Sequence<String>): Int {
        var cave = buildCavePart2(input, 160)
        val last = generateSequence(cave) { it.dropSand() }
            .last()
        println(last.print())
        return last.terrainMap.count { it.value is Terrain.Sand }
    }

    private fun buildCavePart2(input: Sequence<String>, delta: Int): Cave {
        var cave = buildCave(input)
        val y = 2 + cave.yRange.max()
        (cave.xRange.min() - delta..cave.xRange.max() + delta).forEach { x ->
            cave.terrainMap[Position(x, y)] = Terrain.Rock
        }
        cave = Cave(cave.terrainMap)
        return cave
    }
}
