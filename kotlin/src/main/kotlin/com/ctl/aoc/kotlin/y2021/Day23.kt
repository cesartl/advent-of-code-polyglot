package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.CustomConstraint
import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.PathingResult
import com.ctl.aoc.kotlin.utils.Position
import kotlin.math.absoluteValue

object Day23 {

    enum class AmphiType {
        A, B, C, D
    }

    fun AmphiType.x(): Int {
        return 2 + this.ordinal * 2
    }

    val typePoints = AmphiType.values().associate { it to "1".padEnd(it.ordinal + 1, '0').toLong() }

    sealed class Tile {
        object Corridor : Tile()
        data class AmphiRoom(val type: AmphiType) : Tile()

        fun isRoom(type: AmphiType): Boolean = when (this) {
            is AmphiRoom -> this.type == type
            Corridor -> false
        }

        override fun toString(): String = when (this) {
            is AmphiRoom -> "Room(${this.type})"
            Corridor -> "Corridor"
        }


    }

    data class Env(val tiles: Map<Position, Tile>, val amphipods: Map<Position, AmphiType>) {

        val isDone: Boolean by lazy {
            amphipods.all { (pod, type) -> tiles[pod]?.let { it.isRoom(type) } ?: false }
        }

        fun print() {
            (-1..2).forEach { y ->
                (-1..11).forEach { x ->
                    val p = Position(x, y)
                    val pod = amphipods[p]
                    if (pod != null) {
                        print(pod)
                    } else {
                        val tile = tiles[p]
                        when (tile) {
                            is Tile.AmphiRoom -> print('.')
                            Tile.Corridor -> print('.')
                            null -> print('#')
                        }
                    }
                }
                println()
            }
        }

        fun allMoves(): Sequence<Env> {
            return sequence {
                amphipods
                    .filterNot { (p, type) -> isAtRightPlace(p, type) }
                    .forEach { (p, type) ->
                        val pathing = amphiPaths(p)
                        val moveTo = pathing.steps.keys
                            .asSequence()
                            .filter { it != p }
                            .filterNot { isOutsideOfRoom(it) }
                            .filter { canMoveIntoRoom(it, type) }
                            .filter { candidate ->
                                tiles[p]?.let { currentTile ->
                                    when (currentTile) {
                                        is Tile.AmphiRoom -> true
                                        Tile.Corridor -> tiles[candidate]!! is Tile.AmphiRoom
                                    }
                                } ?: true
                            }
                            .filter { isAllTheWayDown(it) }
                            .toList()
                        moveTo.forEach { to ->
                            yield(moveAmphi(p, to))
                        }
                    }
            }
        }

        fun isAllTheWayDown(p: Position): Boolean {
            if (p.y == 1) {
                return amphipods.containsKey(Position(p.x, p.y + 1))
            }
            return true
        }

        fun isAtRightPlace(p: Position, type: AmphiType): Boolean {
            if (p.x != type.x()) {
                return false
            }
            if (p.y == 2) {
                return true
            }
            return amphipods[Position(p.x, p.y - 1)]?.let { below ->
                below == type
            } ?: false
        }

        private fun moveAmphi(from: Position, to: Position): Env {
            val mutablePods = amphipods.toMutableMap()
            val type = mutablePods.remove(from)!!
            mutablePods[to] = type
            return copy(amphipods = mutablePods)
        }

        private fun isOutsideOfRoom(p: Position): Boolean {
            return p.y == 0 && setOf(2, 4, 6, 8).contains(p.x)
        }

        private fun canMoveIntoRoom(p: Position, amphiType: AmphiType): Boolean {
            return tiles[p]?.let { tile ->
                when (tile) {
                    is Tile.Corridor -> true
                    is Tile.AmphiRoom -> {
                        tile.type == amphiType && p.adjacent().mapNotNull { amphipods[it] }.all { it == amphiType }
                    }
                }
            } ?: error("wrong move")
        }

        fun amphiPaths(p: Position): PathingResult<Position> {
            return pathingCache.computeIfAbsent(p) { computeAmphiPaths(it) }
        }

        private val pathingCache = mutableMapOf<Position, PathingResult<Position>>()
        private fun computeAmphiPaths(amphiPosition: Position): PathingResult<Position> {
            return Dijkstra.traverse(amphiPosition, null, { it.amphiAdjacent() }, { _, _ -> 1L })
        }

        private fun Position.amphiAdjacent(): Sequence<Position> {
            return this.adjacent()
                .filter { p -> tiles[p] != null && amphipods[p] == null }
        }

        override fun toString(): String {
            return "Env(amphipods=$amphipods, isDone=$isDone)"
        }

        companion object {
            fun build(lines: List<List<Char>>): Env {
                val corridors = (0 until 11).associate { Position(it, 0) to Tile.Corridor }
                val rooms = mapOf<Position, Tile>(
                    Position(2, 1) to Tile.AmphiRoom(AmphiType.A),
                    Position(2, 2) to Tile.AmphiRoom(AmphiType.A),
                    Position(4, 1) to Tile.AmphiRoom(AmphiType.B),
                    Position(4, 2) to Tile.AmphiRoom(AmphiType.B),
                    Position(6, 1) to Tile.AmphiRoom(AmphiType.C),
                    Position(6, 2) to Tile.AmphiRoom(AmphiType.C),
                    Position(8, 1) to Tile.AmphiRoom(AmphiType.D),
                    Position(8, 2) to Tile.AmphiRoom(AmphiType.D),
                )

                val podLines = lines.map { line -> line.map { AmphiType.valueOf(it.toString()) } }
                val xStart = 2
                val yStart = 1
                val amphipods = podLines.flatMapIndexed { y: Int, pods: List<AmphiType> ->
                    pods.mapIndexed { x, pod -> Position(2 * x + xStart, y + yStart) to pod }
                }.toMap()
                return Env(corridors + rooms, amphipods)
            }

            fun parse(input: Sequence<String>): Env {
                val lines = input.drop(2)
                    .map { line -> line.filter { it.isLetter() }.toList() }
                    .take(2).toList()
                return build(lines)
            }
        }
    }

    fun distance(from: Env, to: Env): Long {
        val start = (from.amphipods.keys - to.amphipods.keys).first()
        val end = (to.amphipods.keys - from.amphipods.keys).first()
        val type = from.amphipods[start]!!
        assert(to.amphipods[end] == type)
        val steps = from.amphiPaths(start).steps[end] ?: error("No steps")
        return steps * typePoints[type]!!
    }

    fun Env.heuristic(): Long {
        return amphipods
            .asSequence()
            .filter { e -> e.key.y > 0 }
            .sumOf { (pod, type) ->
                val xDistance = (type.x() - pod.x).absoluteValue
                xDistance * typePoints[type]!!
            }
    }

    fun bigPathing(start: Env): PathingResult<Env> {
        val constraint = CustomConstraint<Env> { current, _ -> !current.isDone }
        return Dijkstra.traverse(
            start,
            null,
            { it.allMoves() },
            { from, to -> distance(from, to) },
            listOf(constraint),
            heuristic = { it.heuristic() },
            debug = true
        )
    }

    fun solve1(input: Sequence<String>): Long {
        val env = Env.parse(input)
        val result = bigPathing(env)
        return result.steps.asSequence()
            .find { it.key.isDone }
            ?.value
            ?: -1L
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}