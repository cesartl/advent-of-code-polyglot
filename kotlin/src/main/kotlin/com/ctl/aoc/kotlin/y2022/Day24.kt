package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.*

object Day24 {

    data class Blizzard(
        val position: Position,
        val orientation: Orientation
    )

    data class World(
        val blizzards: List<Blizzard>,
        val xRange: IntRange,
        val yRange: IntRange
    ) {
        val blizzardPositions: Set<Position> by lazy {
            blizzards.map { it.position }.toSet()
        }

        fun next(): World {
            return copy(blizzards = blizzards.map { it.move() })
        }

        fun inRange(p: Position): Boolean {
            return p.x in xRange && p.y in yRange
        }

        fun print() {
            println()
            yRange.forEach { y ->
                xRange.forEach { x ->
                    val p = Position(x, y)
                    val found = blizzards.filter { it.position == p }
                    val c = if (found.isEmpty()) {
                        "."
                    } else if (found.size == 1) {
                        when (found.first().orientation) {
                            E -> ">"
                            N -> "^"
                            S -> "v"
                            W -> "<"
                        }
                    } else {
                        found.size
                    }
                    print(c)
                }
                println()
            }
            println()
        }

        private fun Blizzard.move(): Blizzard {
            val next = orientation.move(position)
            return if (next.x in xRange && next.y in yRange) {
                copy(position = next)
            } else {
                wrap()
            }
        }

        private fun Blizzard.wrap(): Blizzard {
            return when (orientation) {
                E -> copy(position = position.copy(x = xRange.first))
                W -> copy(position = position.copy(x = xRange.last))
                N -> copy(position = position.copy(y = yRange.last))
                S -> copy(position = position.copy(y = yRange.first))
            }
        }
    }

    data class State(
        val world: World,
        val position: Position,
        val clock: Int
    ) {
        fun neighbours(goal: Position, worldAt: (Int) -> World): Sequence<State> {
            val nextWorld = worldAt(clock + 1)
            return sequence {
                position.adjacent()
                    .filter { it == goal || it == Position(1, 0) || nextWorld.inRange(it) && !nextWorld.blizzardPositions.contains(it) }
                    .map { State(nextWorld, it, clock + 1) }
                    .forEach { yield(it) }
                if (!nextWorld.blizzardPositions.contains(position)) {
                    yield(copy(clock = clock + 1, world = nextWorld))
                }
            }
        }
    }

    fun worldAtClock(clock: Int, cache: MutableMap<Int, World>): World {
        val cached = cache[clock]
        return if (cached == null) {
            val next = worldAtClock(clock - 1, cache).next()
            cache[clock] = next
            next
        } else {
            cached
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val world = parseWorld(input)
        val goal = Position(world.xRange.last, world.yRange.last + 1)
        val start = State(world, Position(1, 0), 0)
        val cache = mutableMapOf<Int, World>()
        cache[0] = world
        val final = traversal(
            startNode = start,
            storage = Queue(),
            index = { "${it.position}-${it.clock}" },
            nodeGenerator = { it.neighbours(goal) { clock -> worldAtClock(clock, cache) } }
        ).first { it.position == goal }
        return final.clock
    }

    fun solve2(input: Sequence<String>): Int {
        val world = parseWorld(input)
        val goal = Position(world.xRange.last, world.yRange.last + 1)
        val start = State(world, Position(1, 0), 0)
        val cache = mutableMapOf<Int, World>()
        cache[0] = world
        val step1 = traversal(
            startNode = start,
            storage = Queue(),
            index = { "${it.position}-${it.clock}" },
            nodeGenerator = { it.neighbours(goal) { clock -> worldAtClock(clock, cache) } }
        ).first { it.position == goal }

        val step2 = traversal(
            startNode = step1,
            storage = Queue(),
            index = { "${it.position}-${it.clock}" },
            nodeGenerator = { it.neighbours(goal) { clock -> worldAtClock(clock, cache) } }
        ).first { it.position == start.position }

        val step3 = traversal(
            startNode = step2,
            storage = Queue(),
            index = { "${it.position}-${it.clock}" },
            nodeGenerator = { it.neighbours(goal) { clock -> worldAtClock(clock, cache) } }
        ).first { it.position == goal }

        return step3.clock
    }

    private fun parseWorld(input: Sequence<String>): World {
        val blizzards = mutableListOf<Blizzard>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c != '#' && c != '.') {
                    blizzards.add(Blizzard(Position(x, y), Orientation.parse2(c)))
                }
            }
        }
        val width = input.first().length
        val height = input.count()
        val xRange = 1 until width - 1
        val yRange = 1 until height - 1
        return World(blizzards, xRange, yRange)
    }
}
