package com.ctl.aoc.kotlin.`fun`

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RecursionTest{
    @Test
    internal fun testMultiplication() {
        assertEquals(6, Recursion.multiply(3, 2))
        assertEquals(200, Recursion.multiply(100, 2))
        assertEquals(63, Recursion.multiply(9, 7))

        val start = System.currentTimeMillis()
        for (i in 0 until 1000000){
            Recursion.multiply(12300, 2123)
//            Recursion.multiply(2123, 12300)
//            2123 * 12300
        }
        val end = System.currentTimeMillis()
        println((end - start))
    }

    @Test
    internal fun parens() {
        println(Recursion.parens(1))
    }
}