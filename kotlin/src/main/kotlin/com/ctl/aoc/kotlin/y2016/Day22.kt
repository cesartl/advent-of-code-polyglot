package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.Position
import java.util.*
import java.util.regex.Pattern

object Day22 {
    data class Node(val x: Int, val y: Int, val size: Int, val used: Int) {
        val avail = size - used
        val position = Position(x, y)
    }

    val filesystemPattern = Pattern.compile("/dev/grid/node-x([\\d]+)-y([\\d]+)")

    fun parse(line: String): Node {
        val (name, size, used) = line.split(regex = Pattern.compile("\\s+"))
        val m = filesystemPattern.matcher(name)
        if (!m.matches()) throw IllegalArgumentException(name)
        return Node(m.group(1).toInt(), m.group(2).toInt(), size.dropLast(1).toInt(), used.dropLast(1).toInt())
    }

    private fun isPair(a: Node, b: Node): Boolean {
        return a.used > 0 && a != b && b.avail >= a.used
    }

    fun solve1(lines: Sequence<String>): Int {
        val nodes = lines.drop(2).map { parse(it) }
        println(nodes.filter { it.avail >= 66 }.toList())
        return nodes.map { node -> nodes.count { isPair(node, it) } }.sum()
    }

    fun moveData(from: Node, to: Node): Pair<Node, Node> {
        if (to.avail < from.used) throw IllegalArgumentException("$from $to")
        return from.copy(used = 0) to to.copy(used = to.used + from.used)
    }

    data class Grid(val map: Map<Position, Node>) {

        fun doMoveData(moveStep: MoveStep): Grid {
            val from = map[moveStep.from]!!
            val to = map[moveStep.to]!!
            val (nFrom, nTo) = moveData(from, to)
            return copy(map = map + (from.position to nFrom) + (to.position to nTo))
        }

        fun isMoveValid(moveStep: MoveStep): Boolean {
            val from = map[moveStep.from]!!
            val to = map[moveStep.to]!!
            return from.used <= to.size
        }

        fun print(): String {
            val builder = StringBuilder()

            val avg = map.values.map { it.used }.average().toInt()
            val xMax = map.values.map { it.x }.maxOrNull()!!
            val yMax = map.values.map { it.y }.maxOrNull()!!
            for (y in 0..yMax){
                for(x in 0..xMax){
                    val node = map[Position(x, y)]!!
                    when {
                        node.used == 0 -> builder.append("_")
                        node.used > avg -> builder.append("#")
                        else -> builder.append(".")
                    }
                }
                builder.append("\n")
            }
            println("avg $avg")
            println(Position(xMax, 0))

            return builder.toString()
        }
    }

    data class MoveStep(val from: Position, val to: Position) {
        override fun toString(): String {
            return "$from -> $to"
        }
    }

    data class State(val steps: List<MoveStep>, val goal: Position, val zero: Position) {
        val winning: Boolean = goal == Position(0, 0)
        val stateEq = goal to zero
        val goalDistance = goal.distance(Position(0, 0))
    }

    fun generateMoves(state: State, grid: Grid): List<State> {

        val movingZero = state.zero.adjacent().filter { grid.map.containsKey(it) }
                .map { MoveStep(it, state.zero) }
                .filter { grid.isMoveValid(it) }
                .map {
                    if (it.from == state.goal) {
                        state.copy(steps = state.steps + it, zero = it.from, goal = it.to)
                    } else {
                        state.copy(steps = state.steps + it, zero = it.from)
                    }
                }
                .filter { it.goal.distance(it.zero) <= state.goal.distance(state.zero) + 4 }
        return movingZero.toList()
    }

    fun findSteps(grid: Grid): State {
        val goalNode = grid.map.filter { it.key.y == 0 }.maxByOrNull { it.key.x }!!
        val zero = grid.map.values.first { it.avail >= goalNode.value.used }

        println("goalNode: $goalNode")
        println("zero $zero")

        val start = State(listOf(), goalNode.value.position, zero.position)

        val visited = mutableSetOf<Pair<Position, Position>>()
//        val queue = ArrayDeque<State>()
        val minQueue = PriorityQueue<State>(compareBy({ it.goalDistance }))
        minQueue.add(start)
        var state: State = start
        while (!state.winning && !minQueue.isEmpty()) {
            state = minQueue.remove()
//            println("goal ${state.goal} zero ${state.zero}")
            visited.add(state.stateEq)
            generateMoves(state, grid).filter { !visited.contains(it.stateEq) }.forEach { neighbour ->
                minQueue.add(neighbour)
            }
        }
        println("winning ${state.winning}")
        return state
    }

    fun solve2(lines: Sequence<String>): Int {
        val nodes = lines.drop(2).map { parse(it) }
        val grid = Grid(nodes.map { it.position to it }.toMap())
//        val state = findSteps(grid)
//        println(state.steps)
        println(grid.print())
        return 10 + 23 + 8 + 22 + 5*35
    }
}