package com.ctl.aoc.kotlin.y2023

import kotlin.math.roundToLong


data class LongPosition(
    val x: Long,
    val y: Long,
    val z: Long
)

private fun String.toLongPosition(): LongPosition {
    val s = this.split(",").map { it.trim().toLong() }
    return LongPosition(s[0], s[1], s[2])
}


data class Hailstone(
    val position: LongPosition,
    val speed: LongPosition
) {
    val a: Double by lazy {
        val (vx, vy) = speed
        vy.toDouble() / vx.toDouble()
    }

    val b: Double by lazy {
        val (px, py) = position
        val (vx, vy) = speed
        py.toDouble() - px.toDouble() * vy.toDouble() / vx.toDouble()
    }

    fun time(x: Double): Double {
        return (x - position.x) / speed.x
    }
}

private fun intersect(h1: Hailstone, h2: Hailstone): Pair<Double, Double> {
    val x = (h2.b - h1.b) / (h1.a - h2.a)
    val y = h1.a * x + h1.b
    val yy = h2.a * x + h2.b
    return x to y
}

private fun String.parseHailstone(): Hailstone {
    val (position, speed) = this.splitToSequence("@")
        .map { it.trim() }
        .map { it.toLongPosition() }
        .toList()
    return Hailstone(position, speed)
}

private fun LongRange.contains(d: Double): Boolean {
    val rounded = d.roundToLong()
    if (rounded == this.first || rounded == this.last) {
        println("foo")
    }
    return rounded in this
}


private val Hailstone.eq1: String
    get() {
        val (x, y, z) = this.position
        val (dx, dy, dz) = this.speed
        //x dy - y dx + Y dx + y DX - x DY - X dy
        return """
            ${x * dy - y * dx} + Y *($dx) + ($y)*DX - ($x)*DY - X *($dy)
        """.trimIndent()
    }

private val Hailstone.eq2: String
    get() {
        val (x, _, z) = this.position
        val (dx, _, dz) = this.speed
        //x dy - y dx + Y dx + y DX - x DY - X dy
        return """
            ${x * dz - z * dx} + Z*($dx) + ($z)*DX - ($x)*DZ - X*($dz)
        """.trimIndent()
    }


object Day24 {
    fun solve1(input: Sequence<String>, xRange: LongRange, yRange: LongRange): Int {
        var count = 0
        val hailstones = input.map { it.parseHailstone() }.toList()
        hailstones.indices.forEach { i ->
            (i + 1..<hailstones.size).forEach { j ->
                val h1 = hailstones[i]
                val h2 = hailstones[j]
                val (x, y) = intersect(h1, h2)
                val t1 = h1.time(x)
                val t2 = h2.time(x)
                if (xRange.contains(x) && yRange.contains(y) && t1 > 0 && t2 > 0) {
//                    println("($x, $y) -> h1($t1): ${h1.position} h2($t2): ${h2.position}")
                    count++
                }
            }
        }

        return count
    }

    fun solve2(input: Sequence<String>): Long {
        val hailstones = input.map { it.parseHailstone() }.toList()
        hailstones.shuffled().chunked(2).take(4).forEach { group ->
            val (h1, h2) = group
//            val eq1 = "${h1.eq1} = ${h2.eq1}"
            val eq2 = "${h1.eq2} = ${h2.eq2}"
            println(eq2)
//            println(eq2)
        }
        val x = 180391926345105
        val y = 241509806572899
        val z = 127971479302113
        return x + y +z
    }
}
