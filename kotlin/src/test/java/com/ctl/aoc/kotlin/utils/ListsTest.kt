package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Test

internal class ListsTest{

    @Test
    internal fun weaveTest() {
        val all =Lists.weave(listOf(1,2), listOf(3, 4))
        println(all)
    }

    @Test
    internal fun powerSet() {
        println(Lists.powerSet(listOf(1, 2, 3, 4)))
    }
}