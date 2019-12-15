package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException

object Day15 {

    sealed class Command {
        object N : Command()
        object S : Command()
        object W : Command()
        object E : Command()


        fun code(): Long {
            return when (this) {
                N -> 1
                S -> 2
                W -> 3
                E -> 4
            }
        }

        fun backtrack(): Command {
            return when (this) {
                N -> S
                S -> N
                W -> E
                E -> W
            }
        }

        override fun toString(): String {
            return this.javaClass.simpleName
        }

        companion object {
            fun fromCode(code: Int): Command {
                return when (code) {
                    1 -> N
                    2 -> S
                    3 -> W
                    4 -> E
                    else -> throw IllegalArgumentException("Unknown code $code")
                }
            }
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun moveTo(command: Command): Point {
            return when (command) {
                Command.N -> this.copy(y = y + 1)
                Command.S -> this.copy(y = y - 1)
                Command.W -> this.copy(x = x - 1)
                Command.E -> this.copy(x = x + 1)
            }

        }
    }

    sealed class Tile {
        object Wall : Tile()
        object Empty : Tile()
        object Oxygen : Tile()
    }

    data class ExplorationOption(val point: Point, val command: Command, val isBackTrack: Boolean = false)

    data class ExplorationState(var currentPosition: Point = Point(0, 0),
                                val currentPath: MutableList<Command> = mutableListOf(),
                                val visitedTiles: MutableMap<Point, Tile> = mutableMapOf(Point(0, 0) to Tile.Empty),
                                var nextCommand: ExplorationOption? = null,
                                var bestPath: List<Command> = listOf()
    ) {
        private fun neighours(): List<ExplorationOption> = listOf(
                Command.N, Command.S, Command.W, Command.E
        ).map { ExplorationOption(currentPosition.moveTo(it), it) }

        private fun nextNeighbour(): ExplorationOption? {
            return neighours().firstOrNull { !visitedTiles.containsKey(it.point) }
        }

        fun exploreNext() {
            nextCommand = nextNeighbour() ?: backTrack()
        }

        fun updateBestPath() {
            if (bestPath.isEmpty() || currentPath.size < bestPath.size && currentPath.isNotEmpty()) {
                bestPath = currentPath.toList()
            }
        }

        private fun backTrack(): ExplorationOption? {
            return currentPath.lastOrNull()?.backtrack()?.let { ExplorationOption(currentPosition.moveTo(it), it, true) }
        }

        fun printState() {
            val minX = visitedTiles.keys.minBy { it.x }?.x ?: 0
            val maxX = visitedTiles.keys.maxBy { it.x }?.x ?: 0
            val minY = visitedTiles.keys.minBy { it.y }?.y ?: 0
            val maxY = visitedTiles.keys.maxBy { it.y }?.y ?: 0

            (minY..maxY).forEach { y ->
                (minX..maxX).forEach { x ->
                    if (Point(x, y) == Point(0, 0)) {
                        print("*")
                    } else if (Point(x, y) == currentPosition) {
                        print("x")
                    } else {
                        when (visitedTiles[Point(x, y)]) {
                            Tile.Wall -> print("#")
                            Tile.Empty -> print(".")
                            Tile.Oxygen -> print("O")
                            null -> print(" ")
                        }
                    }
                }
                println()
            }
            println("----------------")
            println()
        }
    }

    fun solve1(intCode: LongArray): Int {
        val state = ExplorationState()

        val prgm = Day9.IntCodeState(intCode = intCode, input = {
            state.exploreNext()
            val c = state.nextCommand?.command
            if (c != null) {
//                println("Going $c from ${state.currentPosition}")
                c.code()
            } else {
                0
            }
        }, output = { status ->
            val next = state.nextCommand!!
            when (status) {
                0L -> {
                    //we hit a wall
                    state.visitedTiles[next.point] = Tile.Wall
                }
                1L -> {
                    state.visitedTiles[next.point] = Tile.Empty
                    state.currentPosition = next.point
                    if (next.isBackTrack) {
                        state.currentPath.removeAt(state.currentPath.size - 1)
                    } else {
                        state.currentPath.add(next.command)
                    }
                }
                2L -> {
                    state.visitedTiles[next.point] = Tile.Oxygen
                    state.currentPosition = next.point
                    if (next.isBackTrack) {
                        state.currentPath.removeAt(state.currentPath.size - 1)
                    } else {
                        state.currentPath.add(next.command)
                        state.updateBestPath()
                    }
                }
            }
//            state.printState()
//            println("Visited ${state.visitedTiles.size}")
        })
        Day9.run {
            prgm.execute()
        }
        state.printState()
        return state.bestPath.size
    }
}