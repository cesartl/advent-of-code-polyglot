package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Position

object Day14 {

    sealed class Terrain {
        object Sand : Terrain()
        object Rock : Terrain()
    }

    data class Cave(val terrainMap: Map<Position, Terrain>) {
        val xRange = (terrainMap.keys.minOfOrNull { it.x } ?: 0)..(terrainMap.keys.maxOfOrNull { it.x } ?: 0)
        val yRange = (terrainMap.keys.minOfOrNull { it.y } ?: 0)..(terrainMap.keys.maxOfOrNull { it.y } ?: 0)
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

    private fun drawLine(from: Position, to: Position): Sequence<Position> {
        val dir = (to - from).normalise()
        return generateSequence(from) { it + dir }
            .takeWhile { (it - dir) != to }
    }

    private fun buildCave(input: Sequence<String>): Cave {
        val terrainMap: MutableMap<Position, Terrain> = mutableMapOf()
        input.flatMap { line ->
            line.splitToSequence(" -> ")
                .map {
                    val s = it.split(",")
                    Position(s[0].toInt(), s[1].toInt())
                }
                .zipWithNext { from, to -> drawLine(from, to) }
                .flatten()
        }.forEach { terrainMap[it] = Terrain.Rock }
        return Cave(terrainMap)
    }

    private fun Position.down(): Position = this + Position(0, 1)
    private fun Position.downLeft(): Position = this + Position(-1, 1)
    private fun Position.downRight(): Position = this + Position(1, 1)
    private fun Cave.dropSand(): Cave? {
        val copy = terrainMap.toMutableMap()
        var current = Position(500, 0)
        while (current.y <= this.yRange.max()) {
            val next = sequenceOf(current.down(), current.downLeft(), current.downRight())
                .firstOrNull { !this.terrainMap.containsKey(it) }
            if (next == null) {
                copy[current] = Terrain.Sand
                return Cave(copy)
            }
            current = next
        }
        return null
    }

    fun solve1(input: Sequence<String>): Int {
        val cave = buildCave(input)
        val last = generateSequence(cave) { cave -> cave.dropSand() }.last()
        return last.terrainMap.count { it.value is Terrain.Sand }
    }

    fun solve2(input: Sequence<String>): Int {
        val cave = buildCave(input)
        val copy = cave.terrainMap.toMutableMap()
        val delta = 50
        val y = 2 + cave.yRange.max()
        (cave.xRange.min() - delta..cave.xRange.max() + delta).forEach { x ->
            copy[Position(x, y)] = Terrain.Rock
        }
        val newCave = Cave(copy)
        val last = generateSequence(newCave) { it.dropSand() }
            .last()
//            .first { it.terrainMap[Position(500, 0)] != null }
        println(last.print())
        return last.terrainMap.count { it.value is Terrain.Sand }
    }
}
