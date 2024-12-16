package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Position

object Day14 {

    data class Robot(val p: Position, val v: Position) {
        fun move(boundary: Position): Robot {
            return Robot((p + v + boundary) % boundary, v)
        }

        fun move1(boundary: Position, n: Int): Robot {
            return generateSequence(this) { it.move(boundary) }
                .drop(n)
                .first()
        }

        fun move2(boundary: Position, n: Int): Robot {
            val (x, y) = p
            val (vx, vy) = p
            val newX = (x + (vx) * n) % (boundary.x)
            val newY = (y + (vy) * n) % (boundary.y)
            return Robot(Position(newX, newY), v)
        }
    }

    fun solve1(input: Sequence<String>, width: Int = 101, height: Int = 103): Int {
        val boundary = Position(width, height)

        val robots = input.map { parseRobot(it) }
            .toList()

        val afterMove = robots
            .map { it.move1(boundary, 100) }
            .toList()

        printRobots(afterMove, height, width)
        val q = countQuadrants(afterMove, boundary)
        return q.asSequence()
            .map { it.value }
            .fold(1) { a, b -> a * b }
    }

    fun solve2(input: Sequence<String>, width: Int = 101, height: Int = 103): Int {
        val boundary = Position(width, height)
        val robots = input.map { parseRobot(it) }
            .toList()
        val (i, tree) = generateSequence(robots) { robots ->
            robots.map { it.move(boundary) }
        }.withIndex()
            .take(1000000)
            .dropWhile { !isChristmasTree(it.value) }
            .first()
        println("i = $i")
        printRobots(tree, height, width)
        return i
    }

    private fun isChristmasTree(robots: List<Robot>): Boolean{
        val positions = robots.asSequence().map { it.p }.toSet()
        val percent = 0.7
        val count =  robots.count { robot ->
            robot.p.neighbours().count { positions.contains(it) } >= 1
        }
        return count > percent * robots.size
    }

    private fun printRobots(
        robots: List<Robot>,
        height: Int,
        width: Int
    ) {
        println()
        val counts = robots.groupBy { it.p }.mapValues { it.value.size }
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                print(counts[Position(x, y)] ?: '.')
            }
            println()
        }
        println()
    }

    private fun countQuadrants(robots: List<Robot>, boundary: Position): Map<Position, Int> {
        val half = Position(boundary.x / 2, boundary.y / 2)
        return robots
            .asSequence()
            .filter { it.p.x != half.x && it.p.y != half.y }
            .map { it.p to (it.p / (half + Position(1, 1))) }
            .groupBy { it.second }
            .mapValues { it.value.size }
    }

    private val regex = """p=(-?\d+,-?\d+) v=(-?\d+,-?\d+)""".toRegex()

    private fun parseRobot(line: String): Robot {
        val match = regex.matchEntire(line) ?: throw IllegalArgumentException("Invalid line $line")
        val (px, py) = match.groupValues[1].split(",").map { it.toInt() }
        val (vx, vy) = match.groupValues[2].split(",").map { it.toInt() }
        return Robot(Position(px, py), Position(vx, vy))
    }
}
