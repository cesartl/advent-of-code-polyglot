package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger


object Day11 {


    data class Point(val x: Int, val y: Int)

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
            is Direction.Right -> Direction.Up  //  '>' -> '6'
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
            Direction.Down -> Point(x, y - 1)
            Direction.Up -> Point(x, y + 1)
        }
    }

    data class Robot(val position: Point, val direction: Direction)

    sealed class PaintColour {
        object Black : PaintColour()
        object White : PaintColour()

        fun toNumber(): Long {
            return when (this) {
                Black -> 0
                White -> 1
            }
        }

        companion object {
            fun fromNumber(n: Long): PaintColour {
                return when (n) {
                    0L -> Black
                    1L -> White
                    else -> throw IllegalArgumentException("Unknown colour $n")
                }
            }
        }
    }

    data class HullState(var painedBlocks: MutableMap<Point, PaintColour>, var robot: Robot) {
        fun currentColour(): Long {
            return (painedBlocks[robot.position] ?: PaintColour.Black).toNumber()
        }

        fun drawColour(colourNumber: Long) {
            painedBlocks[robot.position] = PaintColour.fromNumber(colourNumber)
        }

        fun rotateAndMoveRobot(n: Long) {
            val direction = robot.direction
            val newDirection = if (n == 0L) direction.turnLeft() else direction.turnRight()
            robot = robot.copy(direction = newDirection, position = newDirection.move(robot.position))
        }
    }

    fun solve1(puzzleInput: LongArray): Int {
        val hullState = HullState(mutableMapOf(), Robot(Point(0, 0), Direction.Up))
        runRobot(puzzleInput, hullState)
        displayHull(hullState)
        return hullState.painedBlocks.size
    }

    fun solve2(puzzleInput: LongArray) {
        val hullState = HullState(mutableMapOf(), Robot(Point(0, 0), Direction.Up))
        hullState.painedBlocks[Point(0, 0)] = PaintColour.White
        runRobot(puzzleInput, hullState)
        displayHull(hullState)
    }

    private fun displayHull(hullState: HullState) {
        val maxX = hullState.painedBlocks.keys.maxBy { it.x }!!.x
        val maxY = hullState.painedBlocks.keys.maxBy { it.y }!!.y
        val minX = hullState.painedBlocks.keys.minBy { it.x }!!.x
        val minY = hullState.painedBlocks.keys.minBy { it.y }!!.y

        (maxY downTo minY).forEach { y ->
            (minX..maxX).forEach { x ->
                if (hullState.painedBlocks[Point(x, y)] == PaintColour.White) {
                    print("#")
                } else {
                    print(" ")
                }
            }
            println()
        }
    }

    private fun runRobot(puzzleInput: LongArray, hullState: HullState) {
        val isFirstOutput = AtomicInteger(0)
        val intCodeState = Day9.IntCodeState(intCode = puzzleInput.copyOf(9999), input = { hullState.currentColour() }, output = {
            if (isFirstOutput.get() % 2 == 0) {
                hullState.drawColour(it)
            } else {
                hullState.rotateAndMoveRobot(it)
            }
            isFirstOutput.incrementAndGet()
        })
        Day9.run {
            intCodeState.execute()
        }
    }
}