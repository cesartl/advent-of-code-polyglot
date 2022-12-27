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
                if (wrappingMap.isNotEmpty()) {
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

    sealed class Arete {

        abstract val orientation: Orientation

        data class HArete(val xRange: IntProgression, val y: Int, override val orientation: Orientation) : Arete()
        data class VArete(val x: Int, val yRange: IntProgression, override val orientation: Orientation) : Arete()

        fun points(): Sequence<Position> = when (this) {
            is HArete -> sequence {
                xRange.forEach { x ->
                    yield(Position(x, y))
                }
            }

            is VArete -> sequence {
                yRange.forEach { y ->
                    yield(Position(x, y))
                }
            }
        }

        fun reversed(): Arete = when (this) {
            is HArete -> copy(xRange = xRange.reversed())
            is VArete -> copy(yRange = yRange.reversed())
        }
    }

    data class Region(val i: Int, val j: Int, val size: Int) {
        val xRange = (i * size + 1..(i + 1) * size)
        val yRange = (j * size + 1..(j + 1) * size)

        val top: Arete = Arete.HArete(xRange, yRange.first, S)
        val bottom: Arete = Arete.HArete(xRange, yRange.last, N)
        val left: Arete = Arete.VArete(xRange.first, yRange, E)
        val right: Arete = Arete.VArete(xRange.last, yRange, W)
    }

    infix fun Arete.connectWith(with: Arete): Map<Position, Pair<Position, Orientation>> {
        return this.points().zip(with.points()).flatMap { (a, b) ->
            sequenceOf(
                a to (b to with.orientation),
                b to (a to this.orientation),
            )
        }.toMap()
    }

    class WrappingMapBuilder(val init: WrappingMapBuilder.() -> Unit) {
        private val wrappingMap: MutableMap<Position, Pair<Position, Orientation>> = mutableMapOf()

        infix fun Arete.connects(with: Arete) {
            wrappingMap.putAll(this connectWith with)
        }

        fun build(): Map<Position, Pair<Position, Orientation>> {
            init()
            return wrappingMap.toMap()
        }
    }

    private fun wrappingMap(size: Int, grid: Map<Position, Grid>): Map<Position, Pair<Position, Orientation>> {
        return if (size == 4) {
            val r1 = Region(2, 0, size)
            val r2 = Region(0, 1, size)
            val r3 = Region(1, 1, size)
            val r4 = Region(2, 1, size)
            val r5 = Region(2, 2, size)
            val r6 = Region(3, 2, size)
            WrappingMapBuilder {
                r1.left connects r3.top
                r1.top connects r2.top
                r1.right connects r6.right.reversed()

                r2.left connects r6.bottom.reversed()
                r2.bottom connects r5.bottom.reversed()

                r3.bottom connects r5.left.reversed()
                r4.right connects r6.top.reversed()
            }.build()
        } else {
            val r1 = Region(1, 0, size)
            val r2 = Region(2, 0, size)
            val r3 = Region(1, 1, size)
            val r4 = Region(0, 2, size)
            val r5 = Region(1, 2, size)
            val r6 = Region(0, 3, size)
            WrappingMapBuilder {
                r1.top connects r6.left
                r1.left connects r4.left.reversed()

                r2.top connects r6.bottom
                r2.right connects r5.right.reversed()
                r2.bottom connects r3.right

                r3.left connects r4.top

                r5.bottom connects r6.right
            }.build()
        }
    }

    fun solve2(input: String, size: Int): Int {

        val (instructions, w) = parse(input)
        val wrappingMap: Map<Position, Pair<Position, Orientation>> = wrappingMap(size, w.grid)
        val world = w.copy(wrappingMap = wrappingMap)
        instructions.forEach { instruction ->
            world.apply(instruction)
//            world.print()
        }
        println(world.position)
        return return world.score()
    }
}
