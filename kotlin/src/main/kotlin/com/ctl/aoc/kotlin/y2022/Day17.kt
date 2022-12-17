package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Position
import kotlin.math.absoluteValue

object Day17 {

    sealed class Shape {

        fun pattern(): List<Position> = when (this) {
            HLine -> listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0))
            Cross -> listOf(Position(0, -1), Position(1, 0), Position(1, -1), Position(1, -2), Position(2, -1))
            L -> listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, -1), Position(2, -2))
            Square -> listOf(Position(0, 0), Position(1, 0), Position(1, -1), Position(0, -1))
            VLine -> listOf(Position(0, 0), Position(0, -1), Position(0, -2), Position(0, -3))
        }

        fun spwan(at: Position): List<Position> {
            return pattern().map { it + at }
        }

        object HLine : Shape()
        object Cross : Shape()
        object L : Shape()
        object VLine : Shape()
        object Square : Shape()

    }

    private fun Char.moveOffset(): Position = when (this) {
        '>' -> Position(1, 0)
        '<' -> Position(-1, 0)
        else -> error(this)
    }

    data class Tetris(
        val blocks: MutableSet<Position>,
        val xBoundary: IntRange = (1..7)
    ) {

        fun height(): Long {
            return blocks.minBy { it.y }.y.absoluteValue.toLong()
        }

        fun addShape(shape: Shape, gas: Iterator<Char>) {
            val minY = blocks.minByOrNull { it.y }?.y ?: 0
            val spawnAt = Position(3, minY - 4)
//            println("spawnAt $spawnAt")
            val newBlocks = shape.spwan(spawnAt)
            var current = newBlocks
            while (current.intersect(blocks).isEmpty()) {
                current = move(current, gas)
            }
            current = current.map { it + Position(0, -1) }
            blocks.addAll(current)
        }

        private fun move(newBlocks: List<Position>, gas: Iterator<Char>): List<Position> {
            val char = gas.next()
//            println("moving $char")
            val moveOffset = char.moveOffset()
            val afterGas = newBlocks.map { it + moveOffset }
            return if (afterGas.all { it.x in xBoundary } && afterGas.intersect(blocks).isEmpty()) {
//                println("moving")
                afterGas
            } else {
//                println("nothing")
                newBlocks
            }.map { it + Position(0, 1) }
        }

        fun print() {
            val minY = blocks.minByOrNull { it.y }?.y ?: 0
            (minY..0).forEach { y ->
                (0..8).forEach { x ->
                    if (y == 0) {
                        print("_")
                    } else if (x == 0 || x == 8) {
                        print("|")
                    } else if (blocks.contains(Position(x, y))) {
                        print("#")
                    } else {
                        print(".")
                    }
                }
                println("")
            }
        }

    }

    private fun gasIterator(input: String): Iterator<Char> {
        return generateSequence(input) { input }
            .flatMap { it.asSequence() }
            .iterator()
    }

    private fun shapeSequence(): Sequence<Shape> {
        val order = listOf(Shape.HLine, Shape.Cross, Shape.L, Shape.VLine, Shape.Square)
        return generateSequence(order) { order }
            .flatMap { it }
    }

    fun solve1(input: String): Int {
        val gas = gasIterator(input.trim())
        val tetris = Tetris((1..7).map { Position(it, 0) }.toMutableSet())
        shapeSequence()
            .take(2022)
            .forEach {
                tetris.addShape(it, gas)
            }
        tetris.print()
        return tetris.height().toInt()
    }

    fun solve2(input: String, minCycleLength: Int): Long {
        val deltaHeightList = generateHeightDiffSequence(input)
            .take((2.5 * minCycleLength).toInt())
            .toList()

        val (offset, cycle) = deltaHeightList.findCycle(10, minCycleLength)
        println("$offset")
        println("$cycle")

        val pattern = deltaHeightList.subList(offset + 1, offset + cycle + 1)
        println("pattern: ${pattern.joinToString(separator = "")}")
        val start = deltaHeightList.subList(0, offset + 1)
        println("start: ${start.joinToString(separator = "")}")

        val l = 1000000000000L
        val n = (l - offset - 1) / cycle
        val r = (l - offset - 1) % cycle
        val startHeight = start.sum()
        val patternHeight = pattern.sum()
        val reminder = pattern.take(r.toInt()).sum()
        return startHeight + patternHeight * n + reminder
    }

    private fun <T> List<T>.findCycle(searchWidth: Int, minCycleLength: Int): Pair<Int, Int> {
        val l = this.size
        val pattern = this.subList(l - searchWidth, this.size)
        var i = l - minCycleLength
        while (i >= searchWidth && this.subList(i - searchWidth, i) != pattern) {
            i--
        }
        if (i < searchWidth) {
            error("No Cycle found")
        }
        val cycle = this.size - i
        var offset = 0
        while (offset < l && this.subList(offset, offset + cycle) != this.subList(offset + cycle, offset + 2 * cycle)) {
            offset++
        }
        return offset to cycle
    }

    private fun generateHeightDiffSequence(input: String): Sequence<Long> {
        val gas = gasIterator(input.trim())
        val tetris = Tetris((1..7).map { Position(it, 0) }.toMutableSet())
        return shapeSequence()
            .map {
                tetris.addShape(it, gas)
                tetris.height()
            }
            .zipWithNext { a, b -> b - a }
    }
}
