package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2021.Day16.parse
import com.ctl.aoc.kotlin.y2021.Day16.toBinary
import com.ctl.aoc.kotlin.y2021.Day16.versionSum
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test {

    val puzzleInput = InputUtils.downloadAndGetString(2021, 16)

    @Test
    internal fun testBinary() {
        assertEquals("110100101111111000101000", "D2FE28".toBinary())
        assertEquals("00111000000000000110111101000101001010010001001000000000", "38006F45291200".toBinary())
    }

    @Test
    internal fun testPacket() {
        println("EE00D40C823060".toBinary().parse())
        assertEquals(16, "8A004A801A8002F478".toBinary().parse().versionSum())
        assertEquals(31, "A0016C880162017C3686B18A3D4780".toBinary().parse().versionSum())
    }

    @Test
    fun solve1() {
        println(Day16.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day16.solve2("9C0141080250320F1802104A08"))
        println(Day16.solve2(puzzleInput))
    }


}