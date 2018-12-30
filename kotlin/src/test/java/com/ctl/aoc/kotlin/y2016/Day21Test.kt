package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2016.Day21.Scrambler.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {

    val input = InputUtils.getLines(2016, 21)

    @Test
    internal fun testScrambler() {
        assertEquals("ebcda", SwapPosition(4, 0).scramble("abcde"))
        assertEquals("edcba", SwapLetter('d', 'b').scramble("ebcda"))
        assertEquals("dabc", Rotate(1).scramble("abcd"))
        assertEquals("bcda", Rotate(-1).scramble("abcd"))
        assertEquals("ecabd", RotateLetter('b').scramble("abdec"))
        assertEquals("adcbe", Reverse(1, 3).scramble("abcde"))
        assertEquals("bdeac", Move(1, 4).scramble("bcdea"))
        assertEquals("abdec", Move(3, 0).scramble("bdeac"))
    }

    @Test
    internal fun solve1() {
        println(Day21.solve1(input, "abcdefgh"))
    }

    @Test
    internal fun solve2() {
        assertEquals("abcdefgh", Day21.solve2(input, "ghfacdbe"))
        println(Day21.solve2(input, "fbgdceah"))
    }

    @Test
    internal fun RotateLetter() {
        println(RotateLetter('0').scramble("01234567"))
        println(RotateLetter('1').scramble("01234567"))
        println(RotateLetter('2').scramble("01234567"))
        println(RotateLetter('3').scramble("01234567"))
        println(RotateLetter('4').scramble("01234567"))
        println(RotateLetter('5').scramble("01234567"))
        println(RotateLetter('6').scramble("01234567"))
        println(RotateLetter('7').scramble("01234567"))

        roundTrip('a', "abcdefgh")
        roundTrip('b', "abcdefgh")
        roundTrip('c', "abcdefgh")
        roundTrip('d', "abcdefgh")
        roundTrip('e', "abcdefgh")
        roundTrip('f', "abcdefgh")
        roundTrip('g', "abcdefgh")
        roundTrip('h', "abcdefgh")
    }


    private fun roundTrip(letter: Char, s: String){
        val r = RotateLetter(letter)
        assertEquals(s, r.reverse(r.scramble(s)))
    }
}