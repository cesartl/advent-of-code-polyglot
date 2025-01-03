package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day6 {

    data class PatrolMap(val grid: Grid<Char>, val heading: Heading, val obstacle: Position? = null) {
        fun next(): PatrolMap? {
            val next = heading.advance()
            return if (grid.inScope(next.position)) {
                val c = grid.map[next.position]
                if (c == '#' || next.position == obstacle) {
                    this.goTo(heading.turnRight())
                } else {
                    this.goTo(heading = next)
                }
            } else {
                null
            }
        }

        fun isLoop(): Boolean{
            val visited = mutableSetOf<Heading>()
            generateSequence(this) { it.next() }
                .forEach {
                    if(visited.contains(it.heading)){
                        return true
                    }
                    visited.add(it.heading)
                }
            return false
        }

        private fun goTo(heading: Heading): PatrolMap {
            return copy(heading = heading)
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val map = parseInput(input)
        val visited: Set<Position> = generateSequence(map) { it.next() }
            .map { it.heading.position }
            .toSet()
        return visited.size
    }

    fun solve2(input: Sequence<String>): Int {
        val map = parseInput(input)
        return generateSequence(map) { it.next() }
            .map { it.heading.position }
            .drop(1)
            .filter { obstacle ->
                val newMap = map.copy(obstacle = obstacle)
                newMap.isLoop()
            }
            .toSet()
            .size
    }

    private fun parseInput(input: Sequence<String>): PatrolMap {
        val grid = parseGrid(input){
            when(it){
                '#' -> it
                '^' -> it
                else -> null
            }
        }
        val start = grid.map.entries.first { it.value == '^' }.key
        return PatrolMap(grid, Heading(start, N))
    }
}
