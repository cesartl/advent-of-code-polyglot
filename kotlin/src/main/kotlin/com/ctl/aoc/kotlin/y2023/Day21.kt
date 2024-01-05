package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.Grid
import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.parseGrid


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
){

}

object Day21 {
    fun solve1(input: Sequence<String>, maxStep: Int): Int {
        val grid = parseGrid(input)
        val walls = grid.map.filter { it.value == '#' }.keys.toSet()
        val startPosition = grid.map.entries.find { it.value == 'S' }?.key ?: error("No start")

        val start = WalkingState(maxStep, startPosition)

        val result = Dijkstra.traverseInt(
            start = start,
            end = null,
            nodeGenerator = { current ->
                current.next().filter { grid.inScope(it.position) }.filterNot { walls.contains(it.position) }
            },
            distance = { _, _ -> 1 }
        )

//        val outreach = walk(start, grid).drop(maxStep).first().toSet()


//        grid.yRange.forEach { y ->
//            grid.xRange.forEach { x ->
//                val p = Position(x, y)
//                if (outreach.contains(p)) {
//                    print('0')
//                } else if (walls.contains(p)) {
//                    print('#')
//                } else {
//                    print('.')
//                }
//            }
//            println()
//        }

        return result.steps.count { it.key.remainingSteps == 0 }
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
