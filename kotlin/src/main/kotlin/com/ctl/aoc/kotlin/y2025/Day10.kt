package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.powerSet


private const val INFINITY = Int.MAX_VALUE.toLong()

object Day10 {

    data class Button(val wires: List<Int>) {
        val asInt: Int = wires.sumOf { 1 shl it }

        fun updateCounter(counter: List<Int>): List<Int> {
            val newCounter = counter.toMutableList()
            wires.forEach {
                newCounter[it] = newCounter[it] - 1
            }
            return newCounter
        }
    }

    data class MachineReq(
        val onLights: Int,
        val buttons: List<Button>,
        val joltages: List<Int>
    ) {
        val patterns: Map<Int, List<List<Button>>> by lazy {
            buttons.powerSet()
                .groupBy {
                    it.fold(0) { acc, i -> acc xor i.asInt }
                }
        }
    }

    private val lightsRegex = """\[([.#]+)\]""".toRegex()
    private val buttonsRegex = """\(([\d,]+)\)""".toRegex()
    private val joltagesRegex = """\{([\d,]+)\}""".toRegex()

    private fun parseReq(line: String): MachineReq {
        val lights = lightsRegex.find(line)?.groupValues[1]?.let { parseDots(it) } ?: error("")
        val buttons = buttonsRegex.findAll(line)
            .map { match -> match.groupValues[1].split(",").map { it.toInt() } }
            .map { Button(it) }
            .toList()
        val joltages = joltagesRegex.find(line)
            ?.groupValues[1]?.split(",")?.map { it.toInt() }
            ?: error("No joltages found")
        return MachineReq(lights, buttons, joltages)
    }

    fun parseDots(dotString: String): Int {
        return dotString.foldIndexed(0) { i, acc, c ->
            val diff = if (c == '#') {
                1 shl i
            } else {
                0
            }
            diff + acc
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val reqs = input.map { parseReq(it) }.toList()
        return reqs.sumOf {
            minButton(it)
        }
    }

    fun solve1Bis(input: Sequence<String>): Int {
        val reqs = input.map { parseReq(it) }.toList()
        return reqs.sumOf {
            it.patterns[it.onLights]?.minOf { buttons -> buttons.size } ?: error("")
        }
    }

    data class State(
        val lights: Int,
    )

    private fun minButton(machineReq: MachineReq): Int {
        val start = State(0)
        val result = Dijkstra.traverseIntPredicate(
            start = start,
            end = { it?.lights == machineReq.onLights },
            nodeGenerator = { state ->
                machineReq.buttons.asSequence()
                    .map { button ->
                        val newLights = state.lights xor button.asInt
                        State(newLights)
                    }
            },
            distance = { _, _ -> 1 }
        )
        val last = result.lastNode
        return result.steps[last!!]!!
    }

    fun solve2(input: Sequence<String>): Long {
        /*
            Solution from https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/?share_id=K35zS7tyoR9WcaGDt7IZm&utm_content=1&utm_medium=ios_app&utm_name=ioscss&utm_source=share&utm_term=1
            Originally implemented with Z3
         */
        return input
            .map { parseReq(it) }
            .map {
                val cache = mutableMapOf<List<Int>, Long>()
                val r = it.countMinButton(it.joltages, cache)
                println("${it.joltages} -> $r")
                r
            }
            .sum()
    }

    private fun MachineReq.countMinButton(counter: List<Int>, cache: MutableMap<List<Int>, Long>): Long {
        cache[counter]?.let {
            return it
        }
        val result = if (counter.all { it == 0 }) {
            0
        } else if (counter.any { it < 0 }) {
            INFINITY
        } else {
            val lightsPattern = counter.asSequence().map { if (it % 2 == 0) '.' else '#' }.joinToString("")
            val pattern = parseDots(lightsPattern)
            val patterns = this.patterns[pattern] ?: return INFINITY
            patterns.minOf { buttons ->
                val newCounter = buttons.fold(counter) { acc, button -> button.updateCounter(acc) }.map { it / 2 }
                buttons.size + 2 * countMinButton(newCounter, cache)
            }
        }
        cache[counter] = result
        return result
    }
}
