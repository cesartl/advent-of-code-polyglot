package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SequencesTest{

    @Test
    internal fun testStartWith() {
        val s1 = sequence {
            for (i in 1..100){
                println("producing $i")
                yield(i)
            }
        }

        val s2 = sequenceOf(1, 2, 3)
        val s3 = sequenceOf(4, 5)
        assertEquals(true, s1.startWith(s2))
        println("**")
        assertEquals(false, s1.startWith(s3))
    }
}