package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.y2018.Day22.Tool.*

object Day22 {
    sealed class Region {
        override fun toString(): String = this.javaClass.simpleName
        fun riskLevel(): Int {
            return when (this) {
                Rocky -> 0
                Wet -> 1
                Narrow -> 2
            }
        }

        fun print(): String {
            return when (this) {
                Rocky -> "."
                Wet -> "="
                Narrow -> "|"
            }
        }

        object Rocky : Region()
        object Wet : Region()
        object Narrow : Region()

        companion object {
            fun forErosion(erosion: Int): Region {
                return when (erosion % 3) {
                    0 -> Rocky
                    1 -> Wet
                    2 -> Narrow
                    else -> TODO("erosion = $erosion")
                }
            }
        }
    }

    data class Cave(val target: Position, val depth: Int, val regionMap: MutableMap<Int, MutableMap<Int, Region>> = mutableMapOf(), val erosionMap: MutableMap<Int, MutableMap<Int, Int>> = mutableMapOf()) {
        fun erosion(position: Position): Int {
            val (x, y) = position
            val index = if (x == 0 && y == 0) 0
            else if (position == target) 0
            else if (y == 0) 16807 * x
            else if (x == 0) 48271 * y
            else {
                val erosion = erosionMap.computeIfAbsent(y) { mutableMapOf() }[x]
                if (erosion != null) return erosion
                (erosion(Position(x - 1, y)) * erosion(Position(x, y - 1)))
            }
            val erosion = (index + depth) % 20183
            erosionMap.computeIfAbsent(y) { mutableMapOf() }[x] = erosion
            return erosion
        }

        fun explore() {
            val (tx, ty) = target
            for (y in 0..ty) {
                val row = regionMap.computeIfAbsent(y) { mutableMapOf() }
                for (x in 0..tx) {
                    val erosion = erosion(Position(x, y))
                    row[x] = Region.forErosion(erosion)
                }
            }
        }

        fun exploreAround(position: Position) {
            position.adjacents().map { it to regionAt(it) }.filter { it.second == null }.forEach {
                val (x, y) = it.first
                val erosion = erosion(Position(x, y))
                regionMap.computeIfAbsent(y) { mutableMapOf() }[x] = Region.forErosion(erosion)
            }
        }

        fun riskLevel(): Int = regionMap.values.flatMap { it.values }.map { it.riskLevel() }.sum()
        fun print(): String {
            val builder = StringBuilder()
            val (tx, ty) = target
            builder.append("\n")
            for (y in 0..ty) {
                for (x in 0..tx) {
                    builder.append(regionMap[y]?.get(x)?.print() ?: '?')
                }
                builder.append("\n")
            }
            return builder.toString()
        }

        fun regionAt(position: Position): Region? = regionMap[position.y]?.get(position.x)
    }

    fun solve1(depth: Int, target: Position): Int {
        val cave = Cave(target, depth)
        cave.explore()
        println(cave.print())
        return cave.riskLevel()
    }

    sealed class Tool {
        override fun toString(): String = this.javaClass.simpleName

        object Torch : Tool()
        object ClimbingGear : Tool()
        object Neither : Tool()
    }

    fun toolsForRegion(region: Region): List<Tool> {
        return when (region) {
            Region.Rocky -> listOf(ClimbingGear, Torch)
            Region.Wet -> listOf(ClimbingGear, Neither)
            Region.Narrow -> listOf(Torch, Neither)
        }
    }

    data class CaveExploration(val position: Position, val region: Region, val tool: Tool)

    fun isMoveValid(tool: Tool, target: Region): Boolean = toolsForRegion(target).contains(tool)

    fun explorationMoves(cave: Cave, caveExploration: CaveExploration): Sequence<CaveExploration> {
        val position = caveExploration.position
        cave.exploreAround(position)
        val currentTool = caveExploration.tool
        val currentRegion = caveExploration.region
        val around = position.adjacents().map { it to cave.regionAt(it) }.filter { it.second != null }.map { it.first to it.second!! }

        val withCurrentTool = around.filter { isMoveValid(currentTool, it.second) }.map { CaveExploration(it.first, it.second, currentTool) }

        val newTools = toolsForRegion(currentRegion).asSequence().filter { it != currentTool }

        val changingTool = newTools.flatMap { newTool -> around.filter { isMoveValid(newTool, it.second) }.map { CaveExploration(it.first, it.second, newTool) } }

        return withCurrentTool + changingTool
    }

    fun explorationTime(from: CaveExploration, to: CaveExploration): Long = if (from.tool == to.tool) 8 else 1

    fun solve2(depth: Int, target: Position): Long {
        val cave = Cave(target, depth)
        cave.explore()
        println(cave.print())

        val start = CaveExploration(Position(0, 0), Region.Rocky, Torch)
        val end = CaveExploration(target, Region.Rocky, Torch)

        val pathing = Dijkstra.traverse(start, end, nodeGenerator = { explorationMoves(cave, it) }, distance = { from, to -> explorationTime(from, to) })
        return pathing.steps[end] ?: 0
    }
}