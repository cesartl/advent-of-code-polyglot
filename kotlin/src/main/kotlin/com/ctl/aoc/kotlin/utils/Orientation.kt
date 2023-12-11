package com.ctl.aoc.kotlin.utils

import java.math.BigInteger
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign

data class Position(val x: Int, val y: Int) {
    fun adjacent(): Sequence<Position> = sequenceOf(N, S, E, W).map { it.move(this) }
    fun neighbours(): Sequence<Position> = sequence {
        yield(Position(x, y - 1))
        yield(Position(x + 1, y - 1))
        yield(Position(x + 1, y))
        yield(Position(x + 1, y + 1))
        yield(Position(x, y + 1))
        yield(Position(x - 1, y + 1))
        yield(Position(x - 1, y))
        yield(Position(x - 1, y - 1))
    }

    fun isTouching(other: Position): Boolean {
        val (dX, dY) = this - other
        return abs(dX) <= 1 && abs(dY) <= 1
    }

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

    fun sign(): Position = this.copy(x = this.x.sign, y = this.y.sign)

    fun rotate90(): Position = rotate(Matrix22.rotate90)
    fun rotate180(): Position = rotate(Matrix22.rotate180)
    fun rotate270(): Position = rotate(Matrix22.rotate270)

    private fun rotate(matrix22: Matrix22): Position = (matrix22 x this).let { (x, y) -> Position(x, y) }

    fun rotate4(): Sequence<Position> {
        val p = this
        return sequence {
            yield(p)
            yield(p.rotate90())
            yield(p.rotate180())
            yield(p.rotate270())
        }
    }

    fun sameRow(other: Position): Boolean {
        return this.y == other.y
    }

    fun sameColumn(other: Position): Boolean {
        return this.x == other.x
    }

    companion object {
        fun parse(string: String): Position {
            val split = string.split(",")
            return Position(split[0].toInt(), split[1].toInt())
        }
    }
}

fun String.toPosition3d(): Position3d {
    val s = this.split(",").map { it.toInt() }
    return Position3d(s[0], s[1], s[2])
}

data class Position3d(val x: Int, val y: Int, val z: Int) {


    operator fun plus(other: Position3d): Position3d {
        return Position3d(x = x + other.x, y = y + other.y, z = z + other.z)
    }

    operator fun minus(other: Position3d): Position3d {
        return Position3d(x = x - other.x, y = y - other.y, z = z - other.z)
    }

    fun scalar(ratio: Int): Position3d {
        return copy(x = x * ratio, y = y * ratio, z = z * ratio)
    }

    fun divide(ratio: Int): Position3d {
        return copy(x = x / ratio, y = y / ratio, z = z / ratio)
    }

    fun distance(other: Position3d): Int = Math.abs(other.x - x) + Math.abs(other.y - y) + Math.abs(other.z - z)


    private fun rotate(matrix33: Matrix33): Position3d = (matrix33 x this).let { (x, y, z) -> Position3d(x, y, z) }

    fun rotateX90(): Position3d = rotate(Matrix33.rx(PI / 2))
    fun rotateX180(): Position3d = rotate(Matrix33.rx(PI))
    fun rotateX270(): Position3d = rotate(Matrix33.rx(-PI / 2))

    fun rotateY90(): Position3d = rotate(Matrix33.ry(PI / 2))
    fun rotateY180(): Position3d = rotate(Matrix33.ry(PI))
    fun rotateY270(): Position3d = rotate(Matrix33.ry(-PI / 2))

    fun rotateZ90(): Position3d = rotate(Matrix33.rz(PI / 2))
    fun rotateZ180(): Position3d = rotate(Matrix33.rz(PI))
    fun rotateZ270(): Position3d = rotate(Matrix33.rz(-PI / 2))
    override fun toString(): String {
        return "[$x,$y,$z]"
    }
}

fun allRotations(): Sequence<(Position3d) -> Position3d> = sequence {
    //x
    yield() { p: Position3d -> p }
    yield() { p: Position3d -> p.rotateX90() }
    yield() { p: Position3d -> p.rotateX180() }
    yield() { p: Position3d -> p.rotateX270() }

    //-x
    val xx: (Position3d) -> Position3d = { it.rotateY180() }

    yield() { p: Position3d -> xx(p) }
    yield() { p: Position3d -> xx(p).rotateX90() }
    yield() { p: Position3d -> xx(p).rotateX180() }
    yield() { p: Position3d -> xx(p).rotateX270() }

    //y

    val y: (Position3d) -> Position3d = { it.rotateZ90() }
    yield() { p: Position3d -> y(p) }
    yield() { p: Position3d -> y(p).rotateX90() }
    yield() { p: Position3d -> y(p).rotateX180() }
    yield() { p: Position3d -> y(p).rotateX270() }

    //-y

    val yy: (Position3d) -> Position3d = { y(it).rotateY180() }
    yield() { p: Position3d -> yy(p) }
    yield() { p: Position3d -> yy(p).rotateX90() }
    yield() { p: Position3d -> yy(p).rotateX180() }
    yield() { p: Position3d -> yy(p).rotateX270() }

    // z
    val z: (Position3d) -> Position3d = { it.rotateY90() }
    yield() { p: Position3d -> z(p) }
    yield() { p: Position3d -> z(p).rotateX90() }
    yield() { p: Position3d -> z(p).rotateX180() }
    yield() { p: Position3d -> z(p).rotateX270() }

    //-z
    val zz: (Position3d) -> Position3d = { z(it).rotateY180() }
    yield() { p: Position3d -> zz(p) }
    yield() { p: Position3d -> zz(p).rotateX90() }
    yield() { p: Position3d -> zz(p).rotateX180() }
    yield() { p: Position3d -> zz(p).rotateX270() }
}

fun Position3d.rotations24(): Sequence<Position3d> {
    val p = this
    return sequence {
        //x
        yield(p)
        yield(p.rotateX90())
        yield(p.rotateX180())
        yield(p.rotateX270())

        //-x
        val xx = p.rotateY180()
        yield(xx)
        yield(xx.rotateX90())
        yield(xx.rotateX180())
        yield(xx.rotateX270())

        //y

        val y = p.rotateZ90()
        yield(y)
        yield(y.rotateX90())
        yield(y.rotateX180())
        yield(y.rotateX270())

        //-y

        val yy = y.rotateY180()
        yield(yy)
        yield(yy.rotateX90())
        yield(yy.rotateX180())
        yield(yy.rotateX270())

        // z
        val z = p.rotateY90()
        yield(z)
        yield(z.rotateX90())
        yield(z.rotateX180())
        yield(z.rotateX270())

        val zz = z.rotateY180()
        yield(zz)
        yield(zz.rotateX90())
        yield(zz.rotateX180())
        yield(zz.rotateX270())
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

    fun opposite(): Orientation = when (this) {
        E -> W
        N -> S
        S -> N
        W -> E
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

        fun parse2(char: Char): Orientation {
            return when (char) {
                '>' -> E
                '<' -> W
                'v' -> S
                '^' -> N
                else -> throw IllegalArgumentException(char.toString())
            }
        }
    }
}

data object E : Orientation()
data object W : Orientation()
data object S : Orientation()
data object N : Orientation()
