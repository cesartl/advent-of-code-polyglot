package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.*
import kotlin.math.absoluteValue

object Day23 {

    enum class AmphiType {
        A, B, C, D;
    }

    fun AmphiType.x(): Int {
        return 2 + this.ordinal * 2
    }

    private val typePoints = AmphiType.values().associateWith { "1".padEnd(it.ordinal + 1, '0').toInt() }

    data class Env(
        val pods: Map<Position, AmphiType>,
        val maxY: Int = 2
    ) {

        val isDone: Boolean by lazy {
            pods.all { (pod, type) -> pod.y > 0 && pod.x == type.x() }
        }

        fun print() {
            (-1..2).forEach { y ->
                (-1..11).forEach { x ->
                    val p = Position(x, y)
                    val pod = pods[p]
                    if (pod != null) {
                        print(pod)
                    } else {
                        if (y == 0 && x in (0..10)) {
                            print('.')
                        } else if (y > 0 && (x == 2 || x == 4 || x == 6 || x == 8)) {
                            print('.')
                        } else {
                            print('#')
                        }
                    }
                }
                println()
            }
        }

        fun goIntoRoom(x: Int, type: AmphiType): Int? {
            val wrongPod = (1..maxY).any { y -> pods[Position(x, y)]?.let { pod -> pod != type } ?: false }
            if (wrongPod) {
                return null
            }
            var current = Position(x, 1)
            if (pods[current] != null) {
                return null
            }
            while (pods[current] == null) {
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
                    if (p.y == 1 || pods[up] == null) {
                        //can exit room
                        val tolerance = 3
                        //corridor to the right
                        var current = Position(p.x + 1, 0)
                        var countRight = 0
                        val maxCountRight = if (type.x() < p.x) tolerance else 10
                        while (pods[current] == null && current.x <= 10 && countRight < maxCountRight) {
                            //we are above the target room
                            if (current.x == type.x()) {
                                goIntoRoom(current.x, type)?.let { y ->
                                    yield(current.copy(y = y))
                                }
                            }
                            //go in corridor
                            if (current.x != 2 && current.x != 4 && current.x != 6 && current.x != 8) {
                                yield(current)
                            }

                            current = current.copy(x = current.x + 1)
                            countRight++
                        }
                        //corridor to the left
                        current = Position(p.x - 1, 0)
                        var countLeft = 0
                        val maxCountLeft = if (type.x() > p.x) tolerance else 10
                        while (pods[current] == null && current.x >= 0 && countLeft < maxCountLeft) {
                            //we are above the target room
                            if (current.x == type.x()) {
                                goIntoRoom(current.x, type)?.let { y ->
                                    yield(current.copy(y = y))
                                }
                            }

                            //go into corridor
                            if (current.x != 2 && current.x != 4 && current.x != 6 && current.x != 8) {
                                yield(current)
                            }

                            current = current.copy(x = current.x - 1)
                            countLeft++
                        }
                    }
                } else {
                    //in a corridor
                    val deltaX = type.x() - p.x
                    val range = if (deltaX > 0) (p.x + 1..type.x()) else (type.x() until p.x)
                    val pathClear = range.all { pods[Position(it, 0)] == null }
                    if (pathClear) {
                        goIntoRoom(type.x(), type)?.let { y ->
                            yield(Position(type.x(), y))
                        }
                    }
                }
            }
        }

        fun allMoves(): Sequence<Env> {
            val candidates = pods
                .asSequence()
                .filterNot { (p, type) -> isAtRightPlace(p, type) }
                .flatMap { (p, type) ->
                    podMoves(p, type).map { p to it }
                }

            return (candidates.find { it.second.y > 0 }?.let { best -> sequenceOf(best) } ?: candidates)
                .map { movePod(it.first, it.second) }
        }

        private fun isAtRightPlace(p: Position, type: AmphiType): Boolean {
            if (p.x != type.x()) {
                return false
            }
            if (p.y == maxY) {
                return true
            }
            return pods[Position(p.x, p.y + 1)]?.let { below ->
                below == type
            } ?: false
        }

        private fun movePod(from: Position, to: Position): Env {
            val mutablePods = pods.toMutableMap()
            val type = mutablePods.remove(from)!!
            mutablePods[to] = type
            return copy(pods = mutablePods)
        }


        override fun toString(): String {
            return "Env(amphipods=$pods, isDone=$isDone)"
        }

        companion object {
            fun build(lines: List<List<Char>>, depth: Int = 2): Env {
                val podLines = lines.map { line -> line.map { AmphiType.valueOf(it.toString()) } }
                val xStart = 2
                val yStart = 1
                val pods = podLines.flatMapIndexed { y: Int, pods: List<AmphiType> ->
                    pods.mapIndexed { x, pod -> Position(2 * x + xStart, y + yStart) to pod }
                }.toMap()
                return Env(pods, depth)
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

    fun distance(from: Env, to: Env): Int {
        val start = (from.pods.keys - to.pods.keys).first()
        val end = (to.pods.keys - from.pods.keys).first()
        val type = from.pods[start]!!
        assert(to.pods[end] == type)
        val ySteps = start.y + end.y
        val steps = (start.x - end.x).absoluteValue + ySteps
        return steps * typePoints[type]!!
    }

    fun Env.heuristic(): Int {
        return pods
            .asSequence()
            .filter { e -> e.key.y > 0 }
            .sumOf { (pod, type) ->
                val xDistance = (type.x() - pod.x).absoluteValue
                xDistance * typePoints[type]!!
            }
    }

    fun bigPathing(start: Env): PathingResultInt<Env> {
        val constraint = CustomConstraintInt<Env> { current, _ -> !current.isDone }
        return Dijkstra.traverseInt(
            start,
            null,
            { it.allMoves() },
            { from, to -> distance(from, to) },
            listOf(constraint),
            heuristic = { it.heuristic() }
        )
    }

    fun solve1(input: Sequence<String>): Int {
        val env = Env.parse(input)
        env.print()
        val result = timedMs { bigPathing(env) }
        println(result.first)
        return result.second.steps.asSequence()
            .find { it.key.isDone }
            ?.value
            ?: -1
    }

    fun solve2(input: Sequence<String>): Int {
        val env = Env.parse2(input)
        val result = bigPathing(env)
        return result.steps.asSequence()
            .find { it.key.isDone }
            ?.value
            ?: -1
    }
}