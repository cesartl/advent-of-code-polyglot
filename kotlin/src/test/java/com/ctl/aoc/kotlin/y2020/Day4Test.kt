package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day4Test {

    val puzzleInput = InputUtils.getLines(2020, 4)

    val example = """ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm

iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929

hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm

hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in""".split("\n").asSequence()

    val allValid = """pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
hcl:#623a2f

eyr:2029 ecl:blu cid:129 byr:1989
iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

hcl:#888785
hgt:164cm byr:2001 iyr:2015 cid:88
pid:545766238 ecl:hzl
eyr:2022

iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day4.solve1(example))
        println(Day4.solve1(puzzleInput))
    }

    @Test
    internal fun validate() {
        checkValid("byr", "2002")
        checkInvalid("byr", "2003")

        checkValid("hgt", "60in")
        checkValid("hgt", "190cm")
        checkInvalid("hgt", "190in")
        checkInvalid("hgt", "190")

        checkValid("hcl", "#123abc")
        checkInvalid("hcl", "#123abz")
        checkInvalid("hcl", "123abc")

        checkValid("ecl", "amb")
        checkInvalid("ecl", "aa")

        checkValid("pid", "093154719")
        checkInvalid("pid", "3556412378")

        checkValid("cid", "093154719")

        assert(Day4.solve2(allValid) == 4)
    }

    private fun checkValid(field: String, value: String) {
        assert(Day4.validate(field, value))
    }

    private fun checkInvalid(field: String, value: String) {
        assert(!Day4.validate(field, value))
    }

    @Test
    fun solve2() {
        println(Day4.solve2(example))
        println(Day4.solve2(puzzleInput))
    }
}