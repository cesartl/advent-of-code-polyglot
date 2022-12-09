package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.*


private fun Position.follow(previous: Position): Position {
    if (this.isTouching(previous)) {
        return this
    }
    val dX = if (previous.x < x) -1 else 1
    val dY = if (previous.y < y) -1 else 1
    return when {
        previous.x == x -> this.copy(y = y + dY)
        previous.y == y -> this.copy(x = x + dX)
        else -> this + Position(dX, dY)
    }
}

object Day9 {

    data class Rope(val h: Position, val t: Position) {
        fun move(orientation: Orientation): Rope {
            val (h, t) = this
            val newHead = orientation.move(h)
            return Rope(newHead, t.follow(newHead))
        }
    }

    data class LongRope(val elements: List<Position>) {
        fun move(orientation: Orientation): LongRope {
            val newElements = elements
                .asSequence()
                .drop(1)
                .runningFold(orientation.move(elements.first())) { previous, current ->
                    current.follow(previous)
                }
                .toList()
            return LongRope(newElements)
        }
    }

    fun LongRope.print(xRange: IntRange, yRange: IntRange) {
        val index =
            elements.withIndex().groupBy { it.value }.mapValues { entry -> entry.value.minBy { it.index }.index }
        println("--")
        yRange.forEach { y ->
            xRange.forEach { x ->
                val p = Position(x, y)
                if (p == Position(0, 0)) {
                    print("s")
                } else {
                    print((index[p] ?: "."))
                }
            }
            println()
        }
        println("--")
    }

    fun solve1(input: Sequence<String>): Int {
        val moves = parseMoves(input)
        val p0 = Position(0, 0)
        val start = Rope(p0, p0)
        val positions = moves
            .runningFold(start) { rope, o -> rope.move(o) }
            .toList()
        return positions
            .map { it.t }
            .toSet()
            .count()
    }

    private fun parseMoves(input: Sequence<String>): Sequence<Orientation> {
        val moves = input.map { line ->
            val split = line.split(" ")
            val direction = split[0]
            val n = split[1].toInt()
            val o = when (direction) {
                "U" -> N
                "D" -> S
                "R" -> E
                "L" -> W
                else -> error(direction)
            }
            (o to n)
        }.flatMap { (o, n) ->
            generateSequence(o) { it }.take(n)
        }
        return moves
    }

    fun solve2(input: Sequence<String>): Int {
        val moves = parseMoves(input)
        val start = LongRope((0..9).map { Position(0, 0) })
        val positions = moves
            .runningFold(start) { rope, o -> rope.move(o) }
            .toList()

//        positions.forEach {
//            it.print(-6..8, -10..2)
//        }

        return positions
            .map { it.elements.last() }
            .toSet()
            .size
    }
}
