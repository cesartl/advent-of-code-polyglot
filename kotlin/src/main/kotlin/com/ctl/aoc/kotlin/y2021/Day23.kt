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

    private val typePoints = AmphiType.values().associate { it to "1".padEnd(it.ordinal + 1, '0').toLong() }

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

    data class Env(val tiles: Map<Position, Tile>, val amphipods: Map<Position, AmphiType>, val maxY: Int = 2) {

        val isDone: Boolean by lazy {
            amphipods.all { (pod, type) -> pod.y > 0 && pod.x == type.x() }
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

        fun goIntoRoom(x: Int, type: AmphiType): Int? {
            val wrongPod = (1..maxY).any { y -> amphipods[Position(x, y)]?.let { pod -> pod != type } ?: false }
            if (wrongPod) {
                return null
            }
            var current = Position(x, 1)
            if (amphipods[current] != null) {
                return null
            }
            while (amphipods[current] == null) {
                if (current.y == maxY) {
                    return maxY
                }
                current = current.copy(y = current.y + 1)
            }
            return current.y - 1
        }

        fun podMoves(p: Position, type: AmphiType): Sequence<Position> {
            return sequence {
                if (p.y > 0) {
                    //inside a room
                    val up = Position(p.x, p.y - 1)
                    if (p.y == 1 || amphipods[up] == null) {
                        //can exit room
                        val tolerance = 3
                        //corridor to the right
                        var current = Position(p.x + 1, 0)
                        var countRight = 0
                        val maxCountRight = if (type.x() < p.x) tolerance else 10
                        while (amphipods[current] == null && current.x <= 10 && countRight < maxCountRight) {
                            if (current.x != 2 && current.x != 4 && current.x != 6 && current.x != 8) {
                                yield(current)
                            }
                            //we are above the target room
                            if (current.x == type.x()) {
                                goIntoRoom(current.x, type)?.let { y ->
                                    yield(current.copy(y = y))
                                }
                            }
                            current = current.copy(x = current.x + 1)
                            countRight++
                        }
                        //corridor to the left
                        current = Position(p.x - 1, 0)
                        var countLeft = 0
                        val maxCountLeft = if (type.x() > p.x) tolerance else 10
                        while (amphipods[current] == null && current.x >= 0 && countLeft < maxCountLeft) {
                            if (current.x != 2 && current.x != 4 && current.x != 6 && current.x != 8) {
                                yield(current)
                            }
                            //we are above the target room
                            if (current.x == type.x()) {
                                goIntoRoom(current.x, type)?.let { y ->
                                    yield(current.copy(y = y))
                                }
                            }
                            current = current.copy(x = current.x - 1)
                            countLeft++
                        }
                    }
                } else {
                    //in a corridor
                    val deltaX = type.x() - p.x
                    val range = if (deltaX > 0) (p.x + 1..type.x()) else (type.x() until p.x)
                    val pathClear = range.all { amphipods[Position(it, 0)] == null }
                    if (pathClear) {
                        goIntoRoom(type.x(), type)?.let { y ->
                            yield(Position(type.x(), y))
                        }
                    }
                }
            }
        }

        fun allMoves2(): Sequence<Env> {
            val candidates =
                amphipods
                    .asSequence()
                    .filterNot { (p, type) -> isAtRightPlace(p, type) }
                    .flatMap { (p, type) ->
                        podMoves(p, type).map { p to it }
                    }

            return (candidates.find { it.second.y > 0 }?.let { best -> sequenceOf(best) } ?: candidates)
                .map { moveAmphi(it.first, it.second) }
        }

        fun allMoves(): Sequence<Env> {
            return sequence {
                amphipods
                    .filterNot { (p, type) -> isAtRightPlace(p, type) }
                    .forEach { (p, type) ->
                        val pathing = computeAmphiPaths(p)
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
            if (p.y in 1 until maxY) {
                return amphipods.containsKey(Position(p.x, p.y + 1))
            }
            return true
        }

        fun isAtRightPlace(p: Position, type: AmphiType): Boolean {
            if (p.x != type.x()) {
                return false
            }
            if (p.y == maxY) {
                return true
            }
            return amphipods[Position(p.x, p.y + 1)]?.let { below ->
                below == type
            } ?: false
        }

        fun moveAmphi(from: Position, to: Position): Env {
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
            fun build(lines: List<List<Char>>, depth: Int = 2): Env {
                val corridors = (0 until 11).associate { Position(it, 0) to Tile.Corridor }
                val rooms = (1..depth).flatMap { y ->
                    listOf(
                        Position(2, y) to Tile.AmphiRoom(AmphiType.A),
                        Position(4, y) to Tile.AmphiRoom(AmphiType.B),
                        Position(6, y) to Tile.AmphiRoom(AmphiType.C),
                        Position(8, y) to Tile.AmphiRoom(AmphiType.D),
                    )
                }.toMap()
                val podLines = lines.map { line -> line.map { AmphiType.valueOf(it.toString()) } }
                val xStart = 2
                val yStart = 1
                val amphipods = podLines.flatMapIndexed { y: Int, pods: List<AmphiType> ->
                    pods.mapIndexed { x, pod -> Position(2 * x + xStart, y + yStart) to pod }
                }.toMap()
                return Env(corridors + rooms, amphipods, depth)
            }

            fun parse(input: Sequence<String>): Env {
                val lines = input.drop(2)
                    .map { line -> line.filter { it.isLetter() }.toList() }
                    .take(2).toList()
                return build(lines, 2)
            }

            fun parse2(input: Sequence<String>): Env {
                val lines = input.drop(2)
                    .map { line -> line.filter { it.isLetter() }.toList() }
                    .take(2).toList()
                val newLines =
                    listOf(
                        lines[0],
                        listOf('D', 'C', 'B', 'A'),
                        listOf('D', 'B', 'A', 'C'),
                        lines[1]
                    )
                return build(newLines, 4)
            }
        }
    }

    fun distance(from: Env, to: Env): Long {
        val start = (from.amphipods.keys - to.amphipods.keys).first()
        val end = (to.amphipods.keys - from.amphipods.keys).first()
        val type = from.amphipods[start]!!
        assert(to.amphipods[end] == type)
        val ySteps = start.y + end.y
        val steps = (start.x - end.x).absoluteValue + ySteps
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
            { it.allMoves2() },
            { from, to -> distance(from, to) },
            listOf(constraint),
            heuristic = { it.heuristic() },
            debug = true
        )
    }

    fun solve1(input: Sequence<String>): Long {
        val env = Env.parse(input)
//        val moved = env
//            .moveAmphi(Position(6, 1), Position(3, 0))
//            .moveAmphi(Position(6, 2), Position(9, 0))
//            .moveAmphi(Position(3, 0), Position(6, 2))
//        println(moved.goIntoRoom(6, AmphiType.C))
//        moved.print()
        val result = bigPathing(env)
        println("done")
        return result.steps.asSequence()
            .find { it.key.isDone }
            ?.value
            ?: -1L
    }

    fun solve2(input: Sequence<String>): Long {
        val env = Env.parse2(input)
        val result = bigPathing(env)
        return result.steps.asSequence()
            .find { it.key.isDone }
            ?.value
            ?: -1L
    }
}