package com.ctl.aoc.kotlin.y2019

import java.util.concurrent.atomic.AtomicInteger

object Day17 {

    data class Point(val x: Int, val y: Int) {
        fun adjacents(): Sequence<Point> = sequence {
            yield(Direction.Up.move(this@Point))
            yield(Direction.Down.move(this@Point))
            yield(Direction.Right.move(this@Point))
            yield(Direction.Left.move(this@Point))
        }
    }

    sealed class Direction {
        object Up : Direction()
        object Right : Direction()
        object Down : Direction()
        object Left : Direction()

        override fun toString(): String {
            return when (this) {
                Up -> "^"
                Right -> ">"
                Down -> "v"
                Left -> "<"
            }
        }

    }


    fun Direction.turnLeft(): Direction {
        return when (this) {
            is Direction.Up -> Direction.Left   //  '^' ->  '<'
            is Direction.Left -> Direction.Down //  '<' -> 'v'
            is Direction.Down -> Direction.Right//  'v' -> '>'
            is Direction.Right -> Direction.Up  //  '>' -> '^'
        }
    }

    fun Direction.turnRight(): Direction {
        return when (this) {
            is Direction.Up -> Direction.Right
            is Direction.Right -> Direction.Down
            is Direction.Down -> Direction.Left
            is Direction.Left -> Direction.Up
        }
    }

    fun Direction.move(p: Point): Point {
        val (x, y) = p
        return when (this) {
            Direction.Right -> Point(x + 1, y)
            Direction.Left -> Point(x - 1, y)
            Direction.Down -> Point(x, y + 1)
            Direction.Up -> Point(x, y - 1)
        }
    }


    sealed class Tile {
        object Scaffold : Tile()
        object Empty : Tile()
    }

    data class Robot(val position: Point, val direction: Direction) {
        fun accessiblePointsAfterTurn(): List<Point> {
            return listOf(direction.turnRight().move(position), direction.turnRight().move(position))
        }
    }

    data class State(val grid: MutableMap<Point, Tile>, var robot: Robot) {

        fun print() {
            val maxY = grid.keys.maxByOrNull { it.y }?.y ?: 0
            val minY = grid.keys.minByOrNull { it.y }?.y ?: 0
            val maxX = grid.keys.maxByOrNull { it.x }?.x ?: 0
            val minX = grid.keys.minByOrNull { it.x }?.x ?: 0
            (minY..maxY).forEach { y ->
                (minX..maxX).forEach { x ->
                    if (Point(x, y) == robot.position) {
                        print(robot.direction)
                    } else {
                        when (grid[Point(x, y)] ?: Tile.Empty) {
                            Tile.Scaffold -> print("#")
                            Tile.Empty -> print(".")
                        }
                    }
                }
                println()
            }
        }
    }

    sealed class RotateCommand {
        object R : RotateCommand()
        object L : RotateCommand()

        override fun toString(): String {
            return when (this) {
                R -> "R"
                L -> "L"
            }
        }

        fun toChar(): Char {
            return when (this) {
                R -> 'R'
                L -> 'L'
            }
        }

        companion object {
            fun parse(s: String): RotateCommand {
                return when (s) {
                    "R" -> R
                    "L" -> L
                    else -> throw IllegalArgumentException(s)
                }
            }
        }
    }

    data class Command(val rotateCommand: RotateCommand, val move: Int) {
        override fun toString(): String {
            return "$rotateCommand$move"
        }

        companion object {
            val regex = """([RL])([0-9]+)""".toRegex()
            fun parse(s: String): Command {
                val result = regex.matchEntire(s)
                if (result != null) {
                    return Command(RotateCommand.parse(result.groupValues[1]), result.groupValues[2].toInt())
                } else {
                    throw IllegalArgumentException(s)
                }
            }
        }
    }

    data class Routine(val commands: List<Command>, val char: Char) {
        fun toAscii(): List<Int> {
            val asciis = mutableListOf<Int>()
            commands.forEachIndexed { idx, command ->
                asciis.add(command.rotateCommand.toChar().toInt())
                asciis.add(','.toInt())
                command.move.toString().forEach { asciis.add(it.toInt()) }
                if (idx < commands.size - 1) {
                    asciis.add(','.toInt())
                }
            }
            return asciis
        }

        companion object {
            fun parse(s: String, char: Char): Routine {
                return Routine(s.split(",").map { Command.parse(it) }, char)
            }
        }
    }

    data class MainRoutine(val definitions: List<Routine>, val routines: List<Routine>) {
        fun toAscii(): List<Int> {
            val asciis = mutableListOf<Int>()
            routines.forEachIndexed { idx, routine ->
                asciis.add(routine.char.toInt())
                if (idx < routines.size - 1) {
                    asciis.add(','.toInt())
                }
            }
            asciis.add('\n'.toInt())
            definitions.forEach {
                asciis.addAll(it.toAscii())
                asciis.add('\n'.toInt())
            }
            return asciis
        }
    }

    fun solve1(intCode: LongArray): Int {
        val state = buildGrid(intCode)
        state.print()
        return state.grid.entries.filter { it.value == Tile.Scaffold }
                .filter { it.key.adjacents().all { adjacent -> state.grid[adjacent] == Tile.Scaffold } }
                .map { it.key }
                .map { (x, y) -> x * y }
                .sum()
    }

    fun solve2(intCode: LongArray): Long {
        val state = buildGrid(intCode)
        val commands = visitAll(state)
//        println(commands.joinToString(separator = "\",\""))
        /*
        A = R12,R4,L12,L12
        b=R8,R10,R10
        c=R4,R8,R10,R12

        B,C,B,A,B,C,A,B,C,A
         */
        val A = Routine.parse("R12,R4,L12,L12", 'A')
        val B = Routine.parse("R8,R10,R10", 'B')
        val C = Routine.parse("R4,R8,R10,R12", 'C')
        val mainRoutine = MainRoutine(listOf(A, B, C), listOf(B, C, B, A, B, C, A, B, C, A))

        val newIntCode = intCode.copyOf(9999)
        newIntCode[0] = 2

        val inputs = mutableListOf<Int>()
        mainRoutine.toAscii().forEach { inputs.add(it) }
        inputs.add('n'.toInt())
        inputs.add('\n'.toInt())

        val iterator = inputs.iterator()
        val output = mutableListOf<Long>()
        val intCodeState = Day9.IntCodeState(intCode = newIntCode, input = {
            println("output: ${output.size}")
            val n = iterator.next().toLong()
            println("sending $n (${n.toChar()})")
            n
        }, output = {
            if (it == 88L) {
                throw IllegalArgumentException("Fell into space")
            }
            output.add(it)
        })
        Day9.run {
            intCodeState.execute()
        }
        return output.last()
    }

    private fun visitAll(state: State): List<Command> {
        val toVisit = state.grid.values.filter { it == Tile.Scaffold }.count()
        val visited = mutableSetOf<Point>()
        var robot = state.robot
        val commands = mutableListOf<Command>()
        while (visited.size < toVisit) {
            var newDirection: Direction
            var rotateCommand: RotateCommand
            when (Tile.Scaffold) {
                state.grid[robot.direction.turnRight().move(robot.position)] -> {
                    newDirection = robot.direction.turnRight()
                    rotateCommand = RotateCommand.R
                }
                state.grid[robot.direction.turnLeft().move(robot.position)] -> {
                    newDirection = robot.direction.turnLeft()
                    rotateCommand = RotateCommand.L
                }
                else -> {
                    throw IllegalArgumentException("Could not turn right or left")
                }
            }
            var count = 0
            var nextPosition = newDirection.move(robot.position)
            do {
                robot = Robot(nextPosition, newDirection)
                visited.add(nextPosition)
                count++
                nextPosition = newDirection.move(robot.position)
            } while (state.grid[nextPosition] == Tile.Scaffold)
            commands.add(Command(rotateCommand, count))
        }
        return commands
    }

    private fun buildGrid(intCode: LongArray): State {
        val grid: MutableMap<Point, Tile> = mutableMapOf()
        val x = AtomicInteger(0)
        val y = AtomicInteger(0)

        lateinit var robot: Robot

        val intCodeState = Day9.IntCodeState(intCode = intCode.copyOf(9999), output = {
            when (it.toChar()) {
                '#' -> {
                    grid[Point(x.getAndIncrement(), y.get())] = Tile.Scaffold
                }
                '.' -> {
                    grid[Point(x.getAndIncrement(), y.get())] = Tile.Empty
                }
                '>' -> {
                    robot = Robot(Point(x.getAndIncrement(), y.get()), Direction.Right)
                }
                'v' -> {
                    robot = Robot(Point(x.getAndIncrement(), y.get()), Direction.Down)
                }
                '<' -> {
                    robot = Robot(Point(x.getAndIncrement(), y.get()), Direction.Left)
                }
                '^' -> {
                    robot = Robot(Point(x.getAndIncrement(), y.get()), Direction.Up)
                }
                '\n' -> {
                    y.incrementAndGet()
                    x.set(0)
                }
                else -> {
                    throw IllegalArgumentException("Unknown ASCII $it (${it.toChar()}")
                }
            }
        })
        Day9.run {
            intCodeState.execute()
        }
        return State(grid, robot)
    }
}