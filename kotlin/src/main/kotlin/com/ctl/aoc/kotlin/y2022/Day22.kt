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
        var orientation: Orientation = E,
        val wrappingMap: Map<Position, Pair<Position, Orientation>> = mapOf()
    ) {

        fun score(): Int {
            val facing = when (orientation) {
                E -> 0
                S -> 1
                W -> 2
                N -> 3
            }
            return 4 * position.x + 1000 * position.y + facing
        }

        val xRange = grid.keys.minBy { it.x }.x..grid.keys.maxBy { it.x }.x
        val yRange = grid.keys.minBy { it.y }.y..grid.keys.maxBy { it.y }.y
        fun apply(instruction: Instruction) {
            val (newPosition, newOrientation) = move(position, instruction.n, orientation)
            position = newPosition
            orientation = newOrientation
            orientation = when (instruction.turn) {
                Turn.Left -> orientation.rotate(90)
                Turn.Right -> orientation.rotate(-90)
                null -> orientation
            }
        }

        tailrec fun move(current: Position, n: Int, orientation: Orientation): Pair<Position, Orientation> {
            if (n == 0) {
                return current to orientation
            }
            val (nextPosition, nextOrientation) = nextOrWrap(current, orientation)
            return when (grid[nextPosition]) {
                is Grid.Wall -> {
                    current to orientation
                }

                null -> {
                    error("Should not be null after wrap at $current")
                }

                else -> {
                    move(nextPosition, n - 1, nextOrientation)
                }
            }
        }

        private fun nextOrWrap(p: Position, orientation: Orientation): Pair<Position, Orientation> {
            val next = orientation.move(p, 1)
            return if (!grid.containsKey(next)) {
                if (wrappingMap.containsKey(p)) {
                    wrappingMap[p]!!
                } else {
                    wrap(p, orientation) to orientation
                }
            } else {
                next to orientation
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
        instructions.forEach { instruction ->
            world.apply(instruction)
//            world.print()
        }
        println(world.position)
        return return world.score()
    }

    private fun parse(input: String, size: Int = 4): Pair<Sequence<Instruction>, World> {
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
        val xStart = grid.keys.filter { it.y == 1 }.minBy { it.x }.x
        val world = World(grid = grid, position = Position(xStart, 1))
        return Pair(instructions, world)
    }

    private fun wrappingMap(size: Int, grid: Map<Position, Grid>): MutableMap<Position, Pair<Position, Orientation>> {
        val r1 = (1..size)
        val r2 = (size + 1..2 * size)
        val r3 = (2 * size + 1..3 * size)
        val r4 = (3 * size + 1..4 * size)

        val c1X = r3
        val c1Y = r1

        val c2X = r1
        val c2Y = r2

        val c3X = r2
        val c3Y = r2

        val c4X = r3
        val c4Y = r2

        val c5X = r3
        val c5Y = r3

        val c6X = r4
        val c6Y = r3

        val wrappingMap: MutableMap<Position, Pair<Position, Orientation>> = mutableMapOf()


        //1<->2
        c1X.forEachIndexed { i, x ->
            val from = Position(x, c1Y.first)
            val to = Position(c2X.toList()[i], c2Y.first)
            assert(grid.containsKey(from)) { "$from" }
            assert(grid.containsKey(to)) { "$to" }
            wrappingMap[from] = to to S
            wrappingMap[to] = from to S
        }

        //1<->3
        c1Y.forEachIndexed { i, y ->
            val from = Position(c1X.first, y)
            val to = Position(c3X.toList()[i], c3Y.first)
            wrappingMap[from] = to to S
            wrappingMap[to] = from to E
        }

        //1<->6
        c1Y.forEachIndexed { i, y ->
            val from = Position(c1X.last, y)
            val to = Position(c6X.last, c6Y.reversed().toList()[i])
            wrappingMap[from] = to to W
            wrappingMap[to] = from to W
        }

        //2<->6
        c2Y.forEachIndexed { i, y ->
            val from = Position(c2X.first, y)
            val to = Position(c6X.reversed().toList()[i], c6Y.last)
            wrappingMap[from] = to to N
            wrappingMap[to] = from to E
        }

        //2<->5
        c2X.forEachIndexed { i, x ->
            val from = Position(x, c2Y.last)
            val to = Position(c5X.reversed().toList()[i], c5Y.last)
            wrappingMap[from] = to to N
            wrappingMap[to] = from to N
        }

        //3<->5
        c3X.forEachIndexed { i, x ->
            val from = Position(x, c3Y.last)
            val to = Position(c5X.first, c5Y.reversed().toList()[i])
            wrappingMap[from] = to to E
            wrappingMap[to] = from to N
        }

        //4<->6
        c4Y.forEachIndexed { i, y ->
            val from = Position(c4X.last, y)
            val to = Position(c6X.reversed().toList()[i], c6Y.first)
            wrappingMap[from] = to to S
            wrappingMap[to] = from to W
        }
        return wrappingMap
    }

    fun solve2(input: String, size: Int): Int {

        val (instructions, w) = parse(input)
        val wrappingMap: MutableMap<Position, Pair<Position, Orientation>> = wrappingMap(size, w.grid)
        val world = w.copy(wrappingMap = wrappingMap)
        instructions.forEach { instruction ->
            world.apply(instruction)
//            world.print()
        }
        println(world.position)
        return return world.score()
    }
}
