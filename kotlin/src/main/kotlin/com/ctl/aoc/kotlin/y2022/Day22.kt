package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.*

object Day22 {
    val regex = "(\\d+)([LR])?".toRegex()

    sealed class Turn {
        object Right : Turn()
        object Left : Turn()
    }

    data class Instruction(val n: Int, val turn: Turn?)

    sealed class Grid {
        object Wall : Grid()
        object Empty : Grid()
    }

    data class World(
        val grid: Map<Position, Grid>,
        var position: Position,
        var orientation: Orientation = E
    ) {

        val xRange = grid.keys.minBy { it.x }.x..grid.keys.maxBy { it.x }.x
        val yRange = grid.keys.minBy { it.y }.y..grid.keys.maxBy { it.y }.y
        fun apply(instruction: Instruction) {
            position = move(position, instruction.n, orientation)
            orientation = when (instruction.turn) {
                Turn.Left -> orientation.rotate(90)
                Turn.Right -> orientation.rotate(-90)
                null -> orientation
            }
        }

        tailrec fun move(current: Position, n: Int, orientation: Orientation): Position {
            if (n == 0) {
                return current
            }
            val next = nextOrWrap(current, orientation)
            return when (grid[next]) {
                is Grid.Wall -> {
                    current
                }

                null -> {
                    error("Should not be null after wrap")
                }

                else -> {
                    move(next, n - 1, orientation)
                }
            }
        }

        private fun nextOrWrap(p: Position, orientation: Orientation): Position {
            val next = orientation.move(p, 1)
            return if (!grid.containsKey(next)) {
                wrap(p, orientation)
            } else {
                next
            }
        }

        private fun wrap(p: Position, orientation: Orientation): Position {
            return when (orientation) {
                E -> grid.keys.filter { it.y == p.y }.minBy { it.x }
                W -> grid.keys.filter { it.y == p.y }.maxBy { it.x }
                N -> grid.keys.filter { it.x == p.x }.maxBy { it.y }
                S -> grid.keys.filter { it.x == p.x }.minBy { it.y }
            }
        }

        fun print() {
            println()
            yRange.forEach { y ->
                xRange.forEach { x ->
                    val p = Position(x, y)
                    if (p == position) {
                        when (orientation) {
                            E -> ">"
                            N -> "^"
                            S -> "v"
                            W -> "<"
                        }
                    } else {
                        when (grid[p]) {
                            Grid.Empty -> "."
                            Grid.Wall -> "#"
                            null -> " "
                        }
                    }.run { print(this) }
                }
                println()
            }
            println()
        }
    }

    fun solve1(input: String): Int {
        val (instructions, world) = parse(input)
//        world.print()
        instructions.forEach { instruction ->
            world.apply(instruction)
//            world.print()
        }
        val facing = when (world.orientation) {
            E -> 0
            S -> 1
            W -> 2
            N -> 3
        }
        println(world.position)
        return 4 * world.position.x + 1000 * world.position.y + facing
    }

    private fun parse(input: String): Pair<Sequence<Instruction>, World> {
        val (mapString, directions) = input.split("\n\n")
        val instructions = regex.findAll(directions).map {
            val n = it.groupValues[1].toInt()
            val turn = when (it.groupValues[2]) {
                "R" -> Turn.Right
                "L" -> Turn.Left
                "" -> null
                else -> error("")
            }
            Instruction(n, turn)
        }
        val grid = mutableMapOf<Position, Grid>()
        mapString.splitToSequence("\n").forEachIndexed { y, line ->
            line.splitToSequence("").forEachIndexed { x, c ->
                val p = Position(x, y + 1)
                when (c) {
                    "." -> Grid.Empty
                    "#" -> Grid.Wall
                    else -> null
                }?.let {
                    grid[p] = it
                }
            }
        }
        val x = grid.keys.filter { it.y == 1 }.minBy { it.x }.x
        val world = World(grid, Position(x, 1))
        return Pair(instructions, world)
    }

    fun solve2(input: String): Int {
        TODO()
    }
}
