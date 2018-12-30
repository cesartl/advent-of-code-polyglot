package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.findPath
import com.ctl.aoc.kotlin.y2018.Day22.Tool.*
import com.ctl.aoc.util.FibonacciHeap

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
            fun forErosion(erosion: Long): Region {
                return when (erosion % 3) {
                    0L -> Rocky
                    1L -> Wet
                    2L -> Narrow
                    else -> TODO("erosion = $erosion")
                }
            }
        }
    }

    data class Cave(val target: Position, val depth: Int, val regionMap: MutableMap<Int, MutableMap<Int, Region>> = mutableMapOf(), val erosionMap: MutableMap<Int, MutableMap<Int, Long>> = mutableMapOf(), val xCap: Int? = null) {
        fun erosion(position: Position): Long {
            val (x, y) = position
            val index = if (x == 0 && y == 0) 0L
            else if (position == target) 0L
            else if (y == 0) 16807L * x
            else if (x == 0) 48271L * y
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
            position.adjacent().filter { it.x > 0 && it.y > 0 && (xCap == null || it.x < xCap) }.map { it to regionAt(it) }.filter { it.second == null }.forEach {
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
        val currentRegion = caveExploration.region
        val around = position.adjacent().map { it to cave.regionAt(it) }.filter { it.second != null }.map { it.first to it.second!! }

        val tools = toolsForRegion(currentRegion).asSequence()

        return tools.flatMap { newTool -> around.filter { isMoveValid(newTool, it.second) }.map { CaveExploration(it.first, it.second, newTool) } }
    }

    fun explorationTime(from: CaveExploration, to: CaveExploration): Long = (if (from.tool != to.tool) 8 else 1)

    fun heuristic(node: CaveExploration, target: Position): Long =  node.position.distance(target).toLong()

    fun solve2(depth: Int, target: Position, xCap: Int? = null): Long {
        val cave = Cave(target, depth, xCap = xCap)
        cave.explore()
//        println(cave.print())

        val start = CaveExploration(Position(0, 0), Region.Rocky, Torch)
        val end = CaveExploration(target, Region.Rocky, Torch)

        val pathing = Dijkstra.traverse(start, end, nodeGenerator = { explorationMoves(cave, it) }, distance = { from, to -> explorationTime(from, to) }, queue = FibonacciHeap(), heuristic = { heuristic(it, target) })
//        val path = pathing.findPath(end)
        val p = pathing.findPath(end)

        val result: Pair<Long, Tool> = pathing.findPath(end).drop(1).fold(0L to Torch as Tool) { (time, tool), exp ->
            if (exp.tool != tool) (time + 8) to exp.tool
            else (time + 1) to tool
        }
        return result.first
    }
}