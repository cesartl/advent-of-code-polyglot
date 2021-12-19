package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Test

internal class Position3dTest{

    @Test
    internal fun rotate24() {
        val p = Position3d(1, 2, 3)
        val all = p.rotations24().toSet()
        println(all.size)
        println(all)

        val all2 = allRotations().map { it(p) }.toSet()
        println(all2.size)
        println(all2)
    }
}