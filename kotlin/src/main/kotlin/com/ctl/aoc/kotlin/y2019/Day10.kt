package com.ctl.aoc.kotlin.y2019

object Day10 {

    data class Position(val x: Int, val y: Int)

    operator fun Position.plus(other: Position) = Position(this.x + other.x, this.y + other.y)

    fun solve1(lines: Sequence<String>): Int {
        val asteroids = mutableSetOf<Position>()
        lines.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#') {
                    asteroids.add(Position(x, y))
                }
            }
        }
        val bottomRight = Position(lines.first().length - 1, lines.count() - 1)
        return asteroids.map { countLineOfSight(it, asteroids, bottomRight) }.max() ?: 0
    }

    data class Coefficient(val aX: Int, val bY: Int) {
        fun simplify(): Coefficient {
            return if (aX < 0 && bY < 0) {
                Coefficient(-aX, -bY).simplify()
            } else
                if (aX != 0 && bY != 0) {
                    val gcd = aX.toBigInteger().gcd(bY.toBigInteger()).toInt()
                    Coefficient(aX / gcd, bY / gcd)
                } else if (aX == 0) {
                    Coefficient(0, 1)
                } else {
                    Coefficient(1, 0)
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
        val insightMap = mutableMapOf<Coefficient, List<Position>>()
        (0..maxX).forEach { x ->
            (0..maxY).forEach { y ->
                if (x != center.x || y != center.y) {
                    val c = Coefficient(x - center.x, y - center.y).simplify()
                    if (!insightMap.containsKey(c)) {
                        insightMap[c] = countInSight(center, c, asteroids, bottomRight)
                    }
                }
            }
        }
        val visibleAsteroids = insightMap.values.flatMap { it.toList() }.toSet()
        return visibleAsteroids.size
    }

    private fun countInSight(center: Position, coefficient: Coefficient, asteroids: Set<Position>, bottomRight: Position): List<Position> {
        val a = coefficient.forward(center, bottomRight).filter { asteroids.contains(it) }.take(1).toList()
        val b = coefficient.backward(center, bottomRight).filter { asteroids.contains(it) }.take(1).toList()
        return a + b
    }

    fun Position.isInLimit(bottomRight: Position): Boolean {
        return x >= 0 && y >= 0 && x <= bottomRight.x && y <= bottomRight.y
    }
}