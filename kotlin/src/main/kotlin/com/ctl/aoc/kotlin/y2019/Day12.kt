package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException
import kotlin.math.abs

object Day12 {

    data class Vector(val x: Int, val y: Int, val z: Int) {
        companion object {
            val vectorRegex = """<x=([-0-9]+), y=([-0-9]+), z=([-0-9]+)>""".toRegex()
            fun parse(string: String): Vector {
                val match = vectorRegex.matchEntire(string)
                if (match != null) {
                    return Vector(match.groups[1]!!.value.toInt(), match.groups[2]!!.value.toInt(), match.groups[3]!!.value.toInt())
                }
                throw IllegalArgumentException(string)
            }
        }

        operator fun plus(other: Vector): Vector {
            return Vector(this.x + other.x, this.y + other.y, this.z + other.z)
        }

        fun energy(): Int = abs(x) + abs(y) + abs(z)
    }


    data class Moon(val position: Vector, val velocity: Vector = Vector(0, 0, 0)) {
        fun applyVelocity(): Moon = this.copy(position = this.position + this.velocity)

        fun potentialEnergy(): Int = position.energy()

        fun kineticEnergy(): Int = velocity.energy()

        fun totalEnergy(): Int = potentialEnergy() * kineticEnergy()
    }

    fun applyGravity(m1: Moon, m2: Moon): Pair<Moon, Moon> {
        val x = applyGravity(m1 to m2, { it.position.x }, { v, value -> v.copy(x = v.x + value) })
        val y = applyGravity(x, { it.position.y }, { v, value -> v.copy(y = v.y + value) })
        return applyGravity(y, { it.position.z }, { v, value -> v.copy(z = v.z + value) })
    }

    private fun applyGravity(moons: Pair<Moon, Moon>, axis: (Moon) -> Int, updateVelocity: (Vector, Int) -> Vector): Pair<Moon, Moon> {
        val (m1, m2) = moons
        val axis1 = axis(m1)
        val axis2 = axis(m2)
        return when {
            axis1 < axis2 -> {
                m1.copy(velocity = updateVelocity(m1.velocity, 1)) to m2.copy(velocity = updateVelocity(m2.velocity, -1))
            }
            axis1 > axis2 -> {
                m1.copy(velocity = updateVelocity(m1.velocity, -1)) to m2.copy(velocity = updateVelocity(m2.velocity, 1))
            }
            else -> {
                m1 to m2
            }
        }
    }

    data class JupiterSystem(val moons: List<Moon>) {
        private fun applyGravity(): JupiterSystem {
            val moonsByPosition = mutableMapOf<Vector, Moon>()
            moons.forEach { moonsByPosition[it.position] = it }
            (moons.indices).forEach { i ->
                (i + 1 until moons.size).forEach { j ->
                    applyGravity(moonsByPosition[moons[i].position]!!, moonsByPosition[moons[j].position]!!).toList().forEach { moonsByPosition[it.position] = it }
                }
            }
            return this.copy(moons = moonsByPosition.values.toList())
        }

        private fun applyGravitySingleAxis(axis: (Moon) -> Int, updateVelocity: (Vector, Int) -> Vector): JupiterSystem {
            val moonsByPosition = mutableMapOf<Vector, Moon>()
            moons.forEach { moonsByPosition[it.position] = it }
            (moons.indices).forEach { i ->
                (i + 1 until moons.size).forEach { j ->
                    applyGravity(moonsByPosition[moons[i].position]!! to moonsByPosition[moons[j].position]!!, axis, updateVelocity).toList().forEach { moonsByPosition[it.position] = it }
                }
            }
            return this.copy(moons = moonsByPosition.values.toList())
        }

        private fun applyVelocity(): JupiterSystem {
            return this.copy(moons = this.moons.map { it.applyVelocity() })
        }

        fun doTick(): JupiterSystem = this.applyGravity().applyVelocity()

        fun totalEnergy(): Int = moons.map { it.totalEnergy() }.sum()

        fun findCycle(axis: (Moon) -> Int, updateVelocity: (Vector, Int) -> Vector): Long {
            var tick = 0L
            var current = this
            do{
                current = current.applyGravitySingleAxis(axis, updateVelocity).applyVelocity()
                tick++
            }while(current != this)
            return tick
        }

        fun findCycle(): Long {
            val x = this.findCycle({ it.position.x }, { v, value -> v.copy(x = v.x + value) }).toBigInteger()
            val y = this.findCycle({ it.position.y }, { v, value -> v.copy(y = v.y + value) }).toBigInteger()
            val z = this.findCycle({ it.position.z }, { v, value -> v.copy(z = v.z + value) }).toBigInteger()
            val gcd = x.gcd(y.gcd(z))
            println("gcd $gcd")
            return ((x / gcd) * (y / gcd) * (z / gcd)).toLong()
        }

    }

    tailrec fun doTicks(jupiterSystem: JupiterSystem, n: Int): JupiterSystem {
        return if (n == 0) jupiterSystem
        else doTicks(jupiterSystem.doTick(), n - 1)
    }

    fun solve1(lines: Sequence<String>): Int {
        val vectors = lines.map { Vector.parse(it) }.toList()
        val system = JupiterSystem(vectors.map { Moon(it) })
        val afterTicks = doTicks(system, 1000)
        return afterTicks.totalEnergy()
    }

    fun solve2(lines: Sequence<String>): Long {
        val vectors = lines.map { Vector.parse(it) }.toList()
        val system = JupiterSystem(vectors.map { Moon(it) })
        return system.findCycle()
    }
}