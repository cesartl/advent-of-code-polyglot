package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position

object Day17 {

    @JvmInline
    value class Velocity(val vector: Position)

    data class Target(val xRange: IntRange, val yRange: IntRange)

    data class State(val position: Position = Position(0, 0), val velocity: Velocity)

    fun State.next(): State {
        val (x, y) = position
        val (vx, vy) = velocity.vector
        return State(Position(x + vx, y + vy), Velocity(vector = Position((vx - 1).coerceAtLeast(0), vy - 1)))
    }

    private fun Position.isWithin(target: Target): Boolean {
        val (x, y) = this
        return x in target.xRange && y in target.yRange
    }

    private fun Position.isOut(target: Target): Boolean {
        val (x, y) = this
        return x > target.xRange.last || y < target.yRange.first
    }

    private fun State.launch(): Sequence<State> = generateSequence(this) { it.next() }

    @JvmInline
    value class Trajectory(val positions: List<Position>)

    private fun Velocity.aim(target: Target): Trajectory? {
        val start = State(velocity = this)
        val trajectory = start.launch().takeWhile { !it.position.isOut(target) }
        return if (trajectory.any { it.position.isWithin(target) }) Trajectory(trajectory.map { it.position }.toList()) else null
    }

    private fun Target.allTrajectories(xRange: IntRange, yRange: IntRange): Sequence<Trajectory> {
        val target = this
        return sequence {
            xRange.forEach { x ->
                yRange.forEach { y ->
                    Velocity(Position(x, y)).aim(target)?.let { yield(it) }
                }
            }
        }
    }

    fun Trajectory.maxY(): Int = positions.maxOf { it.y }

    fun solve1(input: String): Int {
        val target = input.parseTarget()
        val all = target.allTrajectories(0..target.xRange.last, -500..500).toList()
        val best = all.maxByOrNull { it.maxY() }!!
        return best.maxY()
    }

    fun solve2(input: String): Int {
        val target = input.parseTarget()
        val all = target.allTrajectories(0..target.xRange.last, -500..500).toList()
        return all.size
    }

    val regex = """target area: x=([-\d]+)..([-\d]+), y=([-\d]+)..([-\d]+)""".toRegex()
    private fun String.parseTarget(): Target {
        val match = regex.matchEntire(this) ?: error(this)
        val groups = match.groupValues.drop(1).map { it.toInt() }
        val xRange = (groups[0]..groups[1])
        val yRange = (groups[2]..groups[3])
        return Day17.Target(xRange, yRange)
    }
}