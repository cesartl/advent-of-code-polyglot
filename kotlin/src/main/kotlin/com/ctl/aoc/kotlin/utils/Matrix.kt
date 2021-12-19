package com.ctl.aoc.kotlin.utils

import kotlin.math.cos
import kotlin.math.sin

data class Matrix22(
        val x00: Int,
        val x01: Int,
        val x10: Int,
        val x11: Int
) {
    companion object {
        val identity = Matrix22(1, 0, 0, 1)
        val rotate90 = Matrix22(0, -1, 1, 0)
        val rotate180 = Matrix22(-1, 0, 0, -1)
        val rotate270 = Matrix22(0, 1, -1, 0)
    }

    infix fun x(col: Matrix21) = matrixMultiply(this, col)
    infix fun x(p: Position) = matrixMultiply(this, Matrix21(p.x, p.y))
}

data class Matrix33(
        val x00: Int, val x01: Int, val x02: Int,
        val x10: Int, val x11: Int, val x12: Int,
        val x20: Int, val x21: Int, val x22: Int) {
    infix fun x(col: Matrix31) = matrixMultiply(this, col)
    infix fun x(p: Position3d) = matrixMultiply(this, Matrix31(p.x, p.y, p.z))
    override fun toString(): String {
        return """$x00 $x01 $x02
            |$x10 $x11 $x12
            |$x20 $x21 $x22
        """.trimMargin()
    }


    companion object {
        val identity = Matrix33(1, 0, 0, 0, 1, 0, 0, 0, 1)

        fun rx(angle: Double) = Matrix33(
                1, 0, 0,
                0, cos(angle).toInt(), -sin(angle).toInt(),
                0, sin(angle).toInt(), cos(angle).toInt()
        )

        fun ry(angle: Double) = Matrix33(
                cos(angle).toInt(), 0, sin(angle).toInt(),
                0, 1, 0,
                -sin(angle).toInt(), 0, cos(angle).toInt()
        )

        fun rz(angle: Double) = Matrix33(
                cos(angle).toInt(), -sin(angle).toInt(), 0,
                sin(angle).toInt(), cos(angle).toInt(), 0,
                0, 0, 1
        )
    }
}

data class Matrix21(val x00: Int, val x10: Int) {

}

data class Matrix31(val x00: Int, val x10: Int, val x20: Int)

fun matrixMultiply(m: Matrix22, col: Matrix21): Matrix21 {
    return Matrix21(m.x00 * col.x00 + m.x01 * col.x10, m.x10 * col.x00 + m.x11 * col.x10)
}

fun matrixMultiply(m: Matrix33, col: Matrix31): Matrix31 {
    val (a, b, c) = col
    return Matrix31(
            a * m.x00 + b * m.x01 + c * m.x02,
            a * m.x10 + b * m.x11 + c * m.x12,
            a * m.x20 + b * m.x21 + c * m.x22
    )
}


