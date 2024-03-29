package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*
import com.ctl.aoc.kotlin.y2021.Day22.size
import java.math.BigInteger


typealias Positions = List<Position>

fun walk(start: Position, grid: Grid<Char>): Sequence<Positions> {
    val walls = grid.map.filter { it.value == '#' }.keys.toSet()
    val init: Positions = listOf(start)
    return generateSequence(init) { current ->
        current.flatMap { p -> p.adjacent().filter { grid.inScope(it) }.filterNot { walls.contains(it) } }
    }
}

data class WalkingState(
    val remainingSteps: Int,
    val position: Position
) {
    fun next(): Sequence<WalkingState> {
        if (remainingSteps == 0) {
            return sequenceOf()
        }
        return position.adjacent()
            .map { WalkingState(remainingSteps - 1, it) }
    }
}

data class InfiniteGrid(
    val grid: Grid<Char>
) {

    val xMod = grid.xRange.size().toInt()
    val yMod = grid.yRange.size().toInt()

    val startPosition = grid.map.entries.find { it.value == 'S' }?.key ?: error("No start")
    fun get(p: Position): Char? {
        val (x, y) = p
        val xi = x.mod(xMod)
        val yi = y.mod(yMod)
        return grid.map[Position(xi, yi)]
    }

    fun reachablePlots(maxStep: Int): Long {
        val result = Dijkstra.traverseInt(
            start = startPosition,
            end = null,
            nodeGenerator = { current ->
                current.adjacent().filterNot { get(it) == '#' }
            },
            constraints = listOf(StepConstraintInt(maxStep)),
            distance = { _, _ -> 1 }
        )
        val parity = maxStep % 2
        return result.steps.count { it.value % 2 == parity }.toLong()
    }

}

object Day21 {
    fun solve1(input: Sequence<String>, maxStep: Int): Long {
        val grid = InfiniteGrid(parseGrid(input))
        return grid.reachablePlots(maxStep)
    }

    fun solve2(input: Sequence<String>): Long {
        val grid = InfiniteGrid(parseGrid(input))

        val target = 26501365
        val n = (target - 65) / grid.xMod

        val interpolate = 3

        val seed = generateSequence(65) { it + grid.xMod }
            .map { grid.reachablePlots(it) }
            .take(interpolate)
            .toList()

        println("seed: $seed")


        val r = generateSequence(seed) { it.drop(1) + Day9.extrapolateRight(it) }
            .drop(n - interpolate + 1)
            .first()

        return r.last()
    }

    fun solve2Bis(input: Sequence<String>): Long {
        //https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
        val grid = parseGrid(input)
        val startPosition = grid.map.entries.find { it.value == 'S' }?.key ?: error("No start")
        val pathing = Dijkstra.traverseInt(
            start = startPosition,
            end = null,
            nodeGenerator = { current ->
                current.adjacent().filter { grid.inScope(it) }.filterNot { grid.map[it] == '#' }
            },
            distance = { _, _ -> 1 }
        )

        val target = 26501365
        val n = ((target - 65) / grid.xRange.size().toInt()).toBigInteger()

        val evenCells = pathing.steps.count { it.value % 2 == 0 }.toBigInteger()
        val oddCells = pathing.steps.count { it.value % 2 == 1 }.toBigInteger()

        val evenCorners = pathing.steps.count { it.value % 2 == 0 && it.value > 65 }.toBigInteger()
        val oddCorners = pathing.steps.count { it.value % 2 == 1 && it.value > 65 }.toBigInteger()

        val r = (n + BigInteger.ONE) * (n + BigInteger.ONE) * oddCells + n * n * evenCells - (n + BigInteger.ONE) * oddCorners + n * evenCorners
        return r.toLong()
    }
}
