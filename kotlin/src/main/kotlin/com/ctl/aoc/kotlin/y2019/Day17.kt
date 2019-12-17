package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException
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
            return this.javaClass.simpleName
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

    data class Robot(val position: Point, val direction: Direction)

    data class State(val grid: MutableMap<Point, Tile>, var robot: Robot)

    fun solve1(intCode: LongArray): Int {
        val state = buildGrid(intCode)
        return state.grid.entries.filter { it.value == Tile.Scaffold }
                .filter { it.key.adjacents().all { adjacent -> state.grid[adjacent] == Tile.Scaffold } }
                .map { it.key }
                .map { (x, y) -> x * y }
                .sum()
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