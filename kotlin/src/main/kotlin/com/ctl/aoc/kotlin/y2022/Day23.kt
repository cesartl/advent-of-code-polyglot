package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.*

object Day23 {

    data class Rule(
        val check: List<Position>,
        val orientation: Orientation
    )

    data class World(
        val elves: Set<Position>,
        val rules: List<Rule>
    ) {
        val xRange: IntRange by lazy { elves.minBy { it.x }.x..elves.maxBy { it.x }.x }
        val yRange: IntRange by lazy { elves.minBy { it.y }.y..elves.maxBy { it.y }.y }
        fun next(): World {
            val (moving, static) = elves.partition { elf ->
                elf.neighbours().any { elves.contains(it) }
            }
            val moveTo = mutableSetOf<Position>()
            val dontMove = mutableSetOf<Position>()
            moving.map { elf ->
                val match = rules.firstOrNull { checkRule(elf, it) }
                elf to if (match != null) {
                    val candidate = match.orientation.move(elf)
                    if (moveTo.contains(candidate)) {
                        moveTo.remove(candidate)
                    } else {
                        moveTo.add(candidate)
                    }
                    candidate
                } else {
                    elf
                }
            }.forEach { (elf, candidate) ->
                if (!moveTo.contains(candidate)) {
                    dontMove.add(elf)
                }
            }
            val newElves = static + moveTo + dontMove
            val newRules = rules.drop(1) + rules.first()
            assert(newElves.size == elves.size)
            return World(elves = newElves.toSet(), rules = newRules)
        }

        private fun checkRule(elf: Position, rule: Rule): Boolean {
            return rule.check.map { it + elf }.none { elves.contains(it) }
        }

        fun countEmpty(): Int {
            var count = 0
            yRange.forEach { y ->
                xRange.forEach { x ->
                    if (!elves.contains(Position(x, y))) {
                        count++
                    }
                }
            }
            return count
        }

        fun print() {

            println()
            yRange.forEach { y ->
                xRange.forEach { x ->
                    if (elves.contains(Position(x, y))) {
                        print("#")
                    } else {
                        print(".")
                    }
                }
                println()
            }
            println()
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val world = parseWolrd(input)
        val after = generateSequence(world) { it.next() }
            .mapIndexed { i, w ->
//                println("round $i")
//                w.print()
                w
            }
            .drop(10)
            .first()
        return after.countEmpty()
    }

    private fun parseWolrd(input: Sequence<String>): World {
        val elves = mutableSetOf<Position>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    elves.add(Position(x, y))
                }
            }
        }
        val rules = listOf(
            Rule(listOf(Position(0, -1), Position(1, -1), Position(-1, -1)), N),
            Rule(listOf(Position(0, 1), Position(1, 1), Position(-1, 1)), S),
            Rule(listOf(Position(-1, 0), Position(-1, -1), Position(-1, 1)), W),
            Rule(listOf(Position(1, 0), Position(1, -1), Position(1, 1)), E),
        )
        return World(elves, rules)
    }

    fun solve2(input: Sequence<String>): Int {
        val world = parseWolrd(input)
        val r = generateSequence(world) { it.next() }
            .zipWithNext()
            .withIndex()
            .first { (i, pair) -> pair.first.elves == pair.second.elves }
        return r.index + 1
    }
}
