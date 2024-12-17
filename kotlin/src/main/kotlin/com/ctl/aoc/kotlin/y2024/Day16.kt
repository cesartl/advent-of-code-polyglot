package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day16 {

    fun solve1(input: Sequence<String>): Int {
        val grid = parseGrid(input) {
            if (it == '#') null else it
        }
        return bestScore(grid)
    }

    private fun bestScore(grid: Grid<Char>): Int {
        val startPoint = grid.map.entries.single { it.value == 'S' }.key
        val endPoint = grid.map.entries.single { it.value == 'E' }.key

        val start = Heading(startPoint, E)
        val result = Dijkstra.traverseIntPredicate(
            start = start,
            end = { it?.position == endPoint },
            nodeGenerator = { nextState(grid, it) },
            distance = ::score,
        )
        val endStates = result.steps.keys.filter { it.position == endPoint }

        return endStates.mapNotNull { result.steps[it] }.min()
    }

    private fun score(a: Heading, b: Heading): Int {
        return if (a.orientation != b.orientation) 1000 else 1
    }

    private fun nextState(grid: Grid<Char>, state: Heading): Sequence<Heading> {
        return sequenceOf(state.advance(), state.turnLeft(), state.turnRight())
            .filter { grid.map.containsKey(it.position) }
    }


    fun solve2(input: Sequence<String>): Int {
        val grid = parseGrid(input) {
            if (it == '#') null else it
        }
        val startPoint = grid.map.entries.single { it.value == 'S' }.key
        val endPoint = grid.map.entries.single { it.value == 'E' }.key

        val start = Heading(startPoint, E)
        val result = Dijkstra.traverseMultiIntPredicate(
            start = start,
            end = { it?.position == endPoint },
            nodeGenerator = { nextState(grid, it) },
            distance = ::score,
        )

        val endStates = result.steps.keys.filter { it.position == endPoint }

        val cells = endStates
            .asSequence()
            .flatMap { result.findPaths(it) }
            .flatten()
            .map { it.position }
            .toSet()

        return cells.size
    }
}
