package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class IntSetTest {

    @Test
    internal fun testIntset() {
        val intSetMapper = IntSetMapper<String>()
        intSetMapper
                .add("a")
                .add("b")
                .add("c")

        intSetMapper.run {
            val set = IntSet()
            assertFalse(set.contains("a"))
            assertFalse(set.add("b").contains("a"))
            assertTrue(set.add("a").add("b").contains("a"))
            assertFalse(set.add("a").add("b").remove("a").contains("a"))

            val set2 = set.add("a").add("b").add("b").add("c")
            assertTrue(set2.contains("a"))
            assertTrue(set2.contains("b"))
            assertTrue(set2.contains("c"))
        }
    }
}