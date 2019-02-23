package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day19Test{

    val replacements = InputUtils.getLines(2015, 19)
    val molecule = "ORnPBPMgArCaCaCaSiThCaCaSiThCaCaPBSiRnFArRnFArCaCaSiThCaCaSiThCaCaCaCaCaCaSiRnFYFArSiRnMgArCaSiRnPTiTiBFYPBFArSiRnCaSiRnTiRnFArSiAlArPTiBPTiRnCaSiAlArCaPTiTiBPMgYFArPTiRnFArSiRnCaCaFArRnCaFArCaSiRnSiRnMgArFYCaSiRnMgArCaCaSiThPRnFArPBCaSiRnMgArCaCaSiThCaSiRnTiMgArFArSiThSiThCaCaSiRnMgArCaCaSiRnFArTiBPTiRnCaSiAlArCaPTiRnFArPBPBCaCaSiThCaPBSiThPRnFArSiThCaSiThCaSiThCaPTiBSiRnFYFArCaCaPRnFArPBCaCaPBSiRnTiRnFArCaPRnFArSiRnCaCaCaSiThCaRnCaFArYCaSiRnFArBCaCaCaSiThFArPBFArCaSiRnFArRnCaCaCaFArSiRnFArTiRnPMgArF"

    @Test
    internal fun testReplacement() {
        val r = Day19.Replacement.parse("H => HO")
        println(r)
        assertThat(r.replace("HOH"))
                .containsExactlyInAnyOrder("HOOH", "HOHO")
        val machine = Day19.ReplacementMachine(listOf(
                Day19.Replacement.parse("H => HO"),
                Day19.Replacement.parse("H => OH"),
                Day19.Replacement.parse("O => HH")
        ))

        assertThat(machine.replace("HOH"))
                .containsExactlyInAnyOrder("HOOH", "HOHO", "OHOH", "HHHH")
    }

    @Test
    internal fun solve1() {
        println(Day19.solve1(replacements, molecule))
    }

    @Test
    internal fun solve2Sample() {
        val replacements = sequenceOf("H => HO", "H => OH", "O => HH", "e => H", "e => O")
        assertEquals(3, Day19.solve22(replacements, "HOH"))
        assertEquals(6, Day19.solve22(replacements, "HOHOHO"))
    }

    @Test
    internal fun sovle2() {
        println(Day19.solve22(replacements, molecule))
    }
}