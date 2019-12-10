package com.ctl.aoc.kotlin.y2019

import kotlin.math.atan2
import kotlin.math.sign

object Day10 {

    fun solve1(lines: Sequence<String>): Int {
        val grid = Grid.parse(lines)
        val best = findBest(grid)
        return best.second
    }

    fun solve2(lines: Sequence<String>): Int {
        val grid = Grid.parse(lines)
        val destroyed = laserDestroy(grid).drop(199).first()
        println(destroyed)
        return destroyed.x * 100 + destroyed.y
    }

    data class Position(val x: Int, val y: Int)

    operator fun Position.plus(other: Position) = Position(this.x + other.x, this.y + other.y)

    data class Grid(val asteroids: Set<Position>, val bottomRight: Position) {
        companion object {
            fun parse(lines: Sequence<String>): Grid {
                val asteroids = mutableSetOf<Position>()
                lines.forEachIndexed { y, s ->
                    s.forEachIndexed { x, c ->
                        if (c == '#') {
                            asteroids.add(Position(x, y))
                        }
                    }
                }
                val bottomRight = Position(lines.first().length - 1, lines.count() - 1)
                return Grid(asteroids, bottomRight)
            }
        }
    }


    private fun laserDestroy(grid: Grid): Sequence<Position> {
        val (asteroids, bottomRight) = grid


        val t1 = System.currentTimeMillis()
        val best = findBest(grid).first
        val t2 = System.currentTimeMillis()
        println("Finding best in ${t2 - t1}ms")
        val vectors = findCircularVectors(best, bottomRight, asteroids)

        return sequence {
            var i = 0
            var destroyed: Position? = null
            val remaining = asteroids.toMutableSet()
            do {
                val vector = vectors[i % vectors.size].first
                destroyed = vector.forward(best, bottomRight).find { remaining.contains(it) }
                if (destroyed != null) {
                    remaining.remove(destroyed)
                    yield(destroyed!!)
                }
                i++
            } while (remaining.isNotEmpty())
        }
    }

    private fun findCircularVectors(center: Position, bottomRight: Position, asteroids: Set<Position>): List<Pair<Vector, Double>> {
        val vectors = mutableSetOf<Vector>()
        (asteroids).forEach { (x, y) ->
            if (Position(x, y) != center) {
                vectors.add(Vector(x - center.x, y - center.y).simplify())
            }
        }

        return vectors.toList()
                .map { it to atan2(-it.aX.toDouble(), it.bY.toDouble()) }
                .sortedBy { it.second }
    }

    private fun findBest(grid: Grid): Pair<Position, Int> {
        val (asteroids, bottomRight) = grid
        return asteroids.map { it to countLineOfSight(it, asteroids, bottomRight) }.maxBy { it.second }!!
    }

    data class Vector(val aX: Int, val bY: Int) {
        fun simplify(): Vector {
            return if (aX != 0 && bY != 0) {
                val gcd = aX.toBigInteger().gcd(bY.toBigInteger()).toInt()
                Vector(aX / gcd, bY / gcd)
            } else if (aX == 0) {
                Vector(0, bY.sign)
            } else {
                Vector(aX.sign, 0)
            }
        }


        fun forward(start: Position, bottomRight: Position): Sequence<Position> {
            return sequence {
                var y = start.y
                var x = start.x
                var position: Position
                do {
                    x += aX
                    y += bY
                    position = Position(x, y)
                    if (position.isInLimit(bottomRight)) {
                        yield(position)
                    }
                } while (position.isInLimit(bottomRight))
            }
        }

        fun backward(start: Position, bottomRight: Position): Sequence<Position> {
            return sequence {
                var y = start.y
                var x = start.x
                var position: Position
                do {
                    x -= aX
                    y -= bY
                    position = Position(x, y)
                    if (position.isInLimit(bottomRight)) {
                        yield(position)
                    }
                } while (position.isInLimit(bottomRight))
            }
        }

    }

    private fun countLineOfSight(center: Position, asteroids: Set<Position>, bottomRight: Position): Int {
        val vectors = mutableSetOf<Vector>()
        asteroids.forEach { (x, y) ->
            if (Position(x, y) != center) {
                vectors.add(Vector(x - center.x, y - center.y).simplify())
            }
        }
        return vectors.size
    }

    fun Position.isInLimit(bottomRight: Position): Boolean {
        return x >= 0 && y >= 0 && x <= bottomRight.x && y <= bottomRight.y
    }
}