package com.ctl.aoc.kotlin.utils

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

data class Matrix21(val x00: Int, val x10: Int) {

}

fun matrixMultiply(m: Matrix22, col: Matrix21): Matrix21 {
    return Matrix21(m.x00 * col.x00 + m.x01 * col.x10, m.x10 * col.x00 + m.x11 * col.x10)
}
