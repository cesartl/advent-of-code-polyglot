package com.ctl.aoc.kotlin.utils

import java.math.BigInteger

data class Position(val x: Int, val y: Int) {
    fun adjacent(): Sequence<Position> = sequenceOf(N, S, E, W).map { it.move(this) }
    fun distance(other: Position): Int = Math.abs(other.x - x) + Math.abs(other.y - y)
    override fun toString(): String {
        return "(x=$x, y=$y)"
    }

    operator fun plus(other: Position): Position {
        return Position(x = x + other.x, y = y + other.y)
    }

    operator fun minus(other: Position): Position {
        return Position(x = x - other.x, y = y - other.y)
    }

    fun scalar(ratio: Int): Position {
        return copy(x = x * ratio, y = y * ratio)
    }

    fun normalise(): Position {
        val bigX = BigInteger.valueOf(x.toLong()).abs()
        val bigY = BigInteger.valueOf(y.toLong()).abs()
        val gcd = bigX.gcd(bigY).toInt()
        return when {
            gcd != 0 -> {
                copy(x = x / gcd, y = y / gcd)
            }
            this.x == 0 -> {
                copy(y = 1)
            }
            else -> {
                copy(x = 1)
            }
        }
    }

    fun rotate90(): Position = rotate(Matrix22.rotate90)
    fun rotate180(): Position = rotate(Matrix22.rotate180)
    fun rotate270(): Position = rotate(Matrix22.rotate270)

    private fun rotate(matrix22: Matrix22): Position = (matrix22 x this).let { (x, y) -> Position(x, y) }


    companion object {
        fun parse(string: String): Position {
            val split = string.split(",")
            return Position(split[0].toInt(), split[1].toInt())
        }
    }
}

sealed class Orientation {
    fun move(p: Position, amount: Int = 1): Position {
        return when (this) {
            is N -> Position(p.x, p.y - amount)
            is E -> Position(p.x + amount, p.y)
            is S -> Position(p.x, p.y + amount)
            is W -> Position(p.x - amount, p.y)
        }
    }

    fun moveTrigo(p: Position, amount: Int = 1): Position {
        return when (this) {
            is N -> Position(p.x, p.y + amount)
            is E -> Position(p.x + amount, p.y)
            is S -> Position(p.x, p.y - amount)
            is W -> Position(p.x - amount, p.y)
        }
    }

    fun rotate(degrees: Int): Orientation {
        val d = (360 + degrees) % 360
        return when (this) {
            E -> when (d) {
                0 -> E
                90 -> N
                180 -> W
                270 -> S
                else -> throw Error("degree: $degrees ($d)")
            }
            W -> when (d) {
                0 -> W
                90 -> S
                180 -> E
                270 -> N
                else -> throw Error("degree: $degrees ($d)")
            }
            S -> when (d) {
                0 -> S
                90 -> E
                180 -> N
                270 -> W
                else -> throw Error("degree: $degrees ($d)")
            }
            N -> when (d) {
                0 -> N
                90 -> W
                180 -> S
                270 -> E
                else -> throw Error("degree: $degrees ($d)")
            }
        }
    }

    companion object {
        fun parse(char: Char): Orientation {
            return when (char) {
                'E' -> E
                'W' -> W
                'S' -> S
                'N' -> N
                else -> throw IllegalArgumentException(char.toString())
            }
        }
    }
}

object E : Orientation()
object W : Orientation()
object S : Orientation()
object N : Orientation()