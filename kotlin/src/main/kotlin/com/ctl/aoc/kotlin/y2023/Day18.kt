package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*

data class DigInstruction(
    val orientation: Orientation,
    val amount: Int,
    val rbg: String
) {
    fun unfold(): Sequence<Orientation> {
        return generateSequence(orientation) { it }.take(amount)
    }
}

private val regex = """([UDRL]) (\d+) \(#(\w+)\)""".toRegex()

private fun String.parseDigInstruction(): DigInstruction {
    val match = regex.matchEntire(this) ?: error(this)
    val o = match.groupValues[1].parseOrientation()
    val amount = match.groupValues[2].toInt()
    val rgb = match.groupValues[3]
    return DigInstruction(o, amount, rgb)
}

private fun String.parseOrientation(): Orientation = when (this) {
    "U" -> N
    "R" -> E
    "D" -> S
    "L" -> W
    else -> error("Invalid orientation $this")
}

object Day18 {
    fun solve1(input: Sequence<String>): Int {
        val holes = input.map { it.parseDigInstruction() }
            .flatMap { it.unfold() }
            .runningFold(Position(0, 0)) { acc, orientation ->
                orientation.move(acc)
            }.toSet()

        val minX = holes.minOf { it.x }
        val maxX = holes.maxOf { it.x }
        val xRange = minX..maxX

        val minY = holes.minOf { it.y }
        val maxY = holes.maxOf { it.y }
        val yRange = minY..maxY

        val inside = tracing2(holes, xRange, yRange)
//        val allInside = findInsidePoints(inside, holes, xRange, yRange)
        return (holes + inside).size
    }

    private fun findInsidePoints(
        start: Position,
        boundaries: Set<Position>,
        xRange: IntRange,
        yRange: IntRange
    ): Set<Position> {
        return traversal(
            startNode = start,
            storage = Queue(),
            index = { it.toString() },
            nodeGenerator = {
                it.adjacent().filterNot { p -> boundaries.contains(p) }.filter { (x, y) ->
                    x in xRange && y in yRange
                }
            }
        ).toSet()
    }

    private fun print(
        yRange: IntRange,
        xRange: IntRange,
        holes: Set<Position>
    ) {
        yRange.forEach { y ->
            xRange.forEach { x ->
                val p = Position(x, y)
                val c = if (holes.contains(p)) {
                    '#'
                } else {
                    '.'
                }
                print(c)
            }
            println()
        }
    }

    private fun tracing(boundaries: Set<Position>, xRange: IntRange, yRange: IntRange): Sequence<Position> = sequence {
        yRange.forEach { y ->
            var inside = false
            var x = xRange.first
            while (x in xRange) {
                val p = Position(x, y)
                if (boundaries.contains(p)) {
                    inside = !inside
                    while (boundaries.contains(Position(x + 1, y))) {
                        x++
                    }
                } else {
                    if (inside) {
                        yield(p)
                    }
                }
                x++
            }
        }
    }

    private fun tracing2(boundaries: Set<Position>, xRange: IntRange, yRange: IntRange): Set<Position> {


        val insidePoints = mutableSetOf<Position>()

        yRange.forEach { y ->
            var inside = false
            var x = xRange.first
            while (x in xRange) {
                val p = Position(x, y)
                if (boundaries.contains(p) && boundaries.contains(Position(x, y - 1))) {
                    inside = !inside
                } else {
                    if (inside) {
                        insidePoints.add(p)
                    }
                }
                x++
            }
        }

        return insidePoints
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
