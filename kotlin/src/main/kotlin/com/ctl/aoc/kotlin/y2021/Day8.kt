package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.allMappings

object Day8 {

    data class Digit(val wires: Set<Char>) {
        val size = wires.size
        fun matches(expectedCount: Int, wiringPlan: Map<Int, Digit>, overlaps: Map<Int, Int>): Boolean {
            return wires.size == expectedCount
                    && overlaps.all { (digit, overlap) ->
                wires.intersect(wiringPlan[digit]?.wires ?: error("No wiring plan for $digit")).size == overlap
            }
        }
    }

    data class Line2(val digits: List<Digit>, val displays: List<Digit>) {

        fun deriveWirePlan(): Map<Int, Digit> {
            val wirePlan = mutableMapOf<Int, Digit>()
            wirePlan[1] = digits.find { it.size == 2 }!!
            wirePlan[4] = digits.find { it.size == 4 }!!
            wirePlan[7] = digits.find { it.size == 3 }!!
            wirePlan[8] = digits.find { it.size == 7 }!!

            wirePlan[2] = digits.find { it.matches(5, wirePlan, mapOf(1 to 1, 4 to 2)) }!!
            wirePlan[3] = digits.find { it.matches(5, wirePlan, mapOf(1 to 2, 4 to 3)) }!!
            wirePlan[5] = digits.find { it.matches(5, wirePlan, mapOf(1 to 1, 4 to 3)) }!!

            wirePlan[0] = digits.find { it.matches(6, wirePlan, mapOf(1 to 2, 4 to 3)) }!!
            wirePlan[6] = digits.find { it.matches(6, wirePlan, mapOf(1 to 1, 4 to 3)) }!!
            wirePlan[9] = digits.find { it.matches(6, wirePlan, mapOf(1 to 2, 4 to 4)) }!!
            return wirePlan
        }

        fun applyPlan(wiringPlan: Map<Int, Digit>): Long {
            val reversePlan = wiringPlan.toList().associate { (i, digit) -> digit to i }
            return displays.joinToString(separator = "") {
                reversePlan[it]?.toString() ?: error("No wiring plan for $it")
            }.toLong()
        }

        companion object {
            fun parse(s: String): Line2 {
                val parts = s.split(" | ")
                val digits = parts[0].split(" ").map { Digit(it.toSet()) }
                val displays = parts[1].split(" ").map { Digit(it.toSet()) }
                return Line2(digits, displays)
            }
        }
    }

    data class Line(val entries: List<String>, val outputs: List<String>) {

        private val wirings: Set<Set<Char>> = entries.map { it.toSet() }.toSet()

        fun findMapping(): Map<Char, Char> {
            return allMappings
                    .filter { isCompatible(it) }
                    .first()
        }

        private fun isCompatible(mapping: Map<Char, Char>): Boolean {
            val test: Set<Set<Char>> = wirings.map { wire ->
                wire.map { mapping[it] ?: error("No mapping for $it in $mapping") }.toSet()
            }.toSet()
            return test == canonical
        }

        fun outputForMapping(mapping: Map<Char, Char>): Long {
            return outputs.joinToString("") { out ->
                reverseMapping[out.map { mapping[it]!! }.toSet()]?.toString()!!
            }.toLong()
        }

        companion object {
            fun parse(s: String): Line {
                val parts = s.split(" | ")
                val entries = parts[0].split(" ")
                val outputs = parts[1].split(" ")
                return Line(entries, outputs)
            }

            val digitsMapping = mapOf(
                    0 to "abcefg",
                    1 to "cf",
                    2 to "acdeg",
                    3 to "acdfg",
                    4 to "bcdf",
                    5 to "abdfg",
                    6 to "abdefg",
                    7 to "acf",
                    8 to "abcdefg",
                    9 to "abcdfg"
            )

            val reverseMapping: Map<Set<Char>, Int> = digitsMapping.toList().associate { (x, y) -> y.toSet() to x }

            val canonical = digitsMapping.values.map { it.toSet() }.toSet()

            val allMappings = "abcdefg".toList().allMappings()
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val easy = setOf(2, 4, 3, 7)
        return input.map { Line.parse(it) }
                .flatMap { it.outputs.asSequence() }
                .filter { easy.contains(it.length) }
                .count()
    }

    fun solve2(input: Sequence<String>): Long {
        return input.map { Line.parse(it) }
                .map { line ->
                    val mapping = line.findMapping()
                    line.outputForMapping(mapping)
                }.sum()
    }

    fun solve2Bis(input: Sequence<String>): Long {
        return input.map { Line2.parse(it) }
                .map { line ->
                    val plan = line.deriveWirePlan()
                    line.applyPlan(plan)
                }.sum()
    }
}