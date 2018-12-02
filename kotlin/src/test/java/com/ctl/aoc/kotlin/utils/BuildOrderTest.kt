package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BuildOrderTest {

    @Test
    internal fun testBuildOrder() {
        val order = BuildOrder.buildOrder(
                listOf("a", "b", "c", "d", "e", "f"),
                listOf("a" to "d", "f" to "b", "b" to "d", "f" to "a", "d" to "c")
        )
        println(order)
    }
}