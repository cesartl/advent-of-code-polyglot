package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*

data class CrucibleState(
    val heading: Heading,
    val straightCount: Int = 0
) {
    private fun moveStraight(): CrucibleState {
        return this.copy(heading = heading.advance(), straightCount = straightCount + 1)
    }

    private fun moveRight(): CrucibleState {
        return this.copy(heading = heading.turnRight().advance(), straightCount = 1)
    }

    private fun moveLeft(): CrucibleState {
        return this.copy(heading = heading.turnLeft().advance(), straightCount = 1)
    }

    fun nextStates(): Sequence<CrucibleState> = sequence {
        if (straightCount < 3) {
            yield(moveStraight())
        }
        yield(moveRight())
        yield(moveLeft())
    }

    fun nextStatesUltra(): Sequence<CrucibleState> = sequence {
        if (straightCount < 10) {
            yield(moveStraight())
        }
        if(straightCount >= 4) {
            yield(moveRight())
            yield(moveLeft())
        }

    }
}


object Day17 {
    fun solve1(input: Sequence<String>): Int {
        val grid = parseGrid(input) { it.digitToInt() }
        val start = CrucibleState(Heading(Position(0, 0), E))
        val result = Dijkstra.traverseIntPredicate(
            start = start,
            end = { it?.heading?.position == grid.bottomRight },
            nodeGenerator = { it.nextStates().filter { h -> grid.inScope(h.heading.position) } },
            distance = { _, to -> grid.map[to.heading.position] ?: error("Invalid $to") }
        )
        return result.steps[result.lastNode!!] ?: 0
    }

    fun solve2(input: Sequence<String>): Int {
        val grid = parseGrid(input) { it.digitToInt() }
        val start = CrucibleState(Heading(Position(0, 0), E))




        val result = Dijkstra.traverseIntPredicate(
            start = start,
            end = { it?.heading?.position == grid.bottomRight && it.straightCount >= 4 },
            nodeGenerator = { it.nextStatesUltra().filter { h -> grid.inScope(h.heading.position) } },
            distance = { _, to -> grid.map[to.heading.position] ?: error("Invalid $to") }
        )
        return result.steps[result.lastNode!!] ?: 0
    }
}
