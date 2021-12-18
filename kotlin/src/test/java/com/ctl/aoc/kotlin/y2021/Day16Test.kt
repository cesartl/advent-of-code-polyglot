package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2021.Day16.parse
import com.ctl.aoc.kotlin.y2021.Day16.toBinary
import com.ctl.aoc.kotlin.y2021.Day16.toKotlin
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
//        println(Day16.solve2("9C0141080250320F1802104A08"))
        println(Day16.solve2(puzzleInput))
    }

    fun String.toKotlin(): String{
        val packet = this.toBinary().parse()
        val kotlin = packet.toKotlin()
        return "println($kotlin)"
    }

    @Test
    fun toKotlin(){
        println("C200B40A82".toKotlin())
        println("9C0141080250320F1802104A08".toKotlin())
        println(puzzleInput.toKotlin())

    }

    @Test
    internal fun testRunKotlin() {
        println((62231L + (if((5L + 15L + 3L) == (14L + 15L + 6L)) 1L else 0L * 11060471L) + 470L + listOf((listOf(listOf((((listOf(listOf(listOf(listOf(listOf(((listOf(listOf((listOf(listOf((105057548040L)).maxOrNull()!!).minOrNull()!!)).maxOrNull()!!).minOrNull()!!))).maxOrNull()!!).maxOrNull()!!).minOrNull()!!).minOrNull()!!).maxOrNull()!!)))).maxOrNull()!!).minOrNull()!!)).maxOrNull()!! + 255024399L + (3317908289L * if(13854500L == 13854500L) 1L else 0L) + (549L + 410840L + 70L + 12L + 36731L) + (2086L * if(299544L > 2748L) 1L else 0L) + listOf(196724896L,2749L,5L,799029L).maxOrNull()!! + (108L * 112L * 109L * 62L * 52L) + (123L * 151L * 239L) + (96L * 125L) + listOf(15L,2838L,1744L).maxOrNull()!! + ((2L + 12L + 2L) * (14L + 9L + 6L) * (3L + 6L + 9L)) + (if(2669L > 44956L) 1L else 0L * 832109L) + (187255L) + (if((4L + 8L + 11L) > (11L + 4L + 11L)) 1L else 0L * 3671L) + (if(229L == 17L) 1L else 0L * 215L) + listOf(272L,20L,83577913L,347L,1739L).maxOrNull()!! + (if(68L < 68L) 1L else 0L * 2307L) + 9L + 14L + (119L) + (4L * if(2L < 237L) 1L else 0L) + listOf(15285809L).minOrNull()!! + (34266L * if(77092L > 77092L) 1L else 0L) + 11L + (if((12L + 12L + 14L) > (12L + 9L + 9L)) 1L else 0L * 13L) + ((6L * 2L * 6L) + (15L * 9L * 11L) + (13L * 15L * 2L)) + (if(16368768L < 84L) 1L else 0L * 8L) + 3911L + (2291L * if(666111L < 666111L) 1L else 0L) + (34L * 117L * 126L * 69L) + (151L + 1L + 42393L) + (58L + 133721L + 407L + 123L) + listOf(102L,4245376655368L).maxOrNull()!! + (1210908L * if(44621L < 2355497225458L) 1L else 0L) + listOf(518585L).maxOrNull()!! + (if(877192L > 877192L) 1L else 0L * 40132854L) + 35160L + listOf(10L,678L).minOrNull()!! + (9894041L * if((15L + 12L + 4L) < (9L + 14L + 3L)) 1L else 0L) + 32956L + (if(191365059161L > 92L) 1L else 0L * 1392L) + listOf(10495L,193L,3730L).minOrNull()!! + (if((8L + 13L + 7L) < (9L + 13L + 9L)) 1L else 0L * 101L) + listOf(6187L,2610L,25424913L,12385L).minOrNull()!! + (214670L * if(48507L > 151557L) 1L else 0L) + listOf(2576948864L,60047L,6L,90L,3131L).minOrNull()!! + (7L + 1441L) + (3828121L * if(238L < 3L) 1L else 0L) + 76L + (if(275L == 1704L) 1L else 0L * 93L)))
    }
}