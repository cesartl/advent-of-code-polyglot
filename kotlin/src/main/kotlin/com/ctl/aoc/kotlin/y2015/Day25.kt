package com.ctl.aoc.kotlin.y2015

import java.math.BigInteger

object Day25 {
    fun solve1(row: Int, column: Int): BigInteger {
        val index = getIndex(row, column)
        return codeSequence().drop(index - 1).first()
    }

    fun codeSequence(): Sequence<BigInteger> = generateSequence(BigInteger.valueOf(20151125L)) { n ->
        (n * BigInteger.valueOf(252533L)) % BigInteger.valueOf(33554393L)
    }

    fun getIndex(row: Int, column: Int): Int {
        val x = column * (1 + column) / 2
        var y = (row - 1) * (column + row - 2 + column) / 2
        return x + y
    }
}