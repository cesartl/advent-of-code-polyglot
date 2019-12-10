package com.ctl.aoc.kotlin.y2019

import kotlin.math.atan2

object Day10 {

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
                val (maxX, maxY) = bottomRight
                val vectors = mutableSetOf<Vector>()

                (0..maxX).forEach { x ->
                    (0..maxY).forEach { y ->
                        val (xx, yy) = Vector(x, y).simplify()
                        vectors.add(Vector(xx, yy))
                        vectors.add(Vector(-xx, yy))
                        vectors.add(Vector(xx, -yy))
                        vectors.add(Vector(-xx, -yy))
                    }
                }
                return Grid(asteroids, bottomRight)
            }
        }
    }

    fun solve1(lines: Sequence<String>): Int {
        val grid = Grid.parse(lines)
        val best = findBest(grid)
        return best.second
    }

    fun laserDestroy(grid: Grid): Sequence<Position> {
        val (asteroids, bottomRight) = grid
        val vectors = findCircularVectors(bottomRight)

        val best = findBest(grid).first

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

    fun solve2(lines: Sequence<String>): Int {
        val grid = Grid.parse(lines)
        val destroyed = laserDestroy(grid).drop(199).first()
        println(destroyed)
        return destroyed.x * 100 + destroyed.y
    }

    private fun findCircularVectors(bottomRight: Position): List<Pair<Vector, Double>> {
        val (maxX, maxY) = bottomRight
        val vectors = mutableSetOf<Vector>()
        (0..maxX).forEach { x ->
            (0..maxY).forEach { y ->
                val (xx, yy) = Vector(x, y).simplify()
                vectors.add(Vector(xx, yy))
                vectors.add(Vector(-xx, yy))
                vectors.add(Vector(xx, -yy))
                vectors.add(Vector(-xx, -yy))
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
//            return if (aX < 0 && bY < 0) {
//                Coefficient(-aX, -bY).simplify()
//            } else
            return if (aX != 0 && bY != 0) {
                val gcd = aX.toBigInteger().gcd(bY.toBigInteger()).toInt()
                Vector(aX / gcd, bY / gcd)
            } else if (aX == 0) {
                Vector(0, 1)
            } else {
                Vector(1, 0)
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
        val (maxX, maxY) = bottomRight
        val insightMap = mutableMapOf<Vector, List<Position>>()
        (0..maxX).forEach { x ->
            (0..maxY).forEach { y ->
                if (x != center.x || y != center.y) {
                    val c = Vector(x - center.x, y - center.y).simplify()
                    if (!insightMap.containsKey(c)) {
                        insightMap[c] = countInSight(center, c, asteroids, bottomRight)
                    }
                }
            }
        }
        val visibleAsteroids = insightMap.values.flatMap { it.toList() }.toSet()
        return visibleAsteroids.size
    }

    private fun countInSight(center: Position, vector: Vector, asteroids: Set<Position>, bottomRight: Position): List<Position> {
        val a = vector.forward(center, bottomRight).filter { asteroids.contains(it) }.take(1).toList()
        val b = vector.backward(center, bottomRight).filter { asteroids.contains(it) }.take(1).toList()
        return a + b
    }

    fun Position.isInLimit(bottomRight: Position): Boolean {
        return x >= 0 && y >= 0 && x <= bottomRight.x && y <= bottomRight.y
    }
}