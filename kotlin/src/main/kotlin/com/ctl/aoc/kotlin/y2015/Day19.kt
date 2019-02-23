package com.ctl.aoc.kotlin.y2015

import java.util.*

object Day19 {
    data class Replacement(val from: String, val to: String) {

        val fromRegex = from.toRegex()

        fun replace(molecule: String): Set<String> {
            val result = mutableSetOf<String>()
            var match = fromRegex.find(molecule)
            while (match != null) {
                val range = match.range
                result.add(molecule.substring(0, range.start) + to + molecule.substring(range.endInclusive + 1))
                match = match.next()
            }
//            println("${fromRegex.pattern} => $to -> $result")
            return result
        }

        fun reverse(): Replacement = Replacement(to, from)

        companion object {
            val regex = """([\w]+) => ([\w]+)""".toRegex()
            fun parse(s: String): Replacement {
                return regex.matchEntire(s)?.groupValues?.let { Replacement(it[1], it[2]) }
                        ?: throw IllegalArgumentException(s)
            }
        }
    }

    data class ReplacementMachine(val replacement: List<Replacement>) {
        fun replace(molecule: String): Set<String> {
            return replacement.flatMap { it.replace(molecule) }.toSet()
        }
    }

    fun solve1(replacements: Sequence<String>, molecule: String): Int {
        val machine = Day19.ReplacementMachine(replacements.map { Replacement.parse(it) }.toList())
        val all = machine.replace(molecule)
        return all.size
    }


    fun solve2(replacements: Sequence<String>, molecule: String): Int {
        val machine = Day19.ReplacementMachine(replacements.map { Replacement.parse(it).reverse() }.toList())

        val seen = mutableSetOf<String>()
        var current = setOf(molecule)
        var step = 0
        while (!current.contains("e")) {
            current = current.flatMap { machine.replace(it).filter { n -> n.length <= it.length } }.filter { !seen.contains(it) }.toSet()
            seen.addAll(current)
            step++
            println("current: ${current.size} seen: ${seen.size} step: $step")
        }
        return step
    }

    data class State(val molecule: String, val step: Int = 0)

    fun solve22(replacements: Sequence<String>, molecule: String): Int {

        val machine = Day19.ReplacementMachine(replacements.map { Replacement.parse(it).reverse() }.toList())

        val dequeue = ArrayDeque<State>()
        molecule
        dequeue.add(State(molecule))
        val seen = mutableSetOf<String>()
        var current = dequeue.peek()
        var i = 0
        while (current.molecule != "e" && !dequeue.isEmpty()) {
            i++
            current = dequeue.removeFirst()
            seen.add(current.molecule)
            if (i % 1000 == 0) {
                println("Current $current seen ${seen.size}")
            }
            machine.replace(current.molecule).filter { !seen.contains(it) }.sortedBy { -it.length }.map { State(it, current.step + 1) }.forEach { dequeue.push(it) }
        }
        println("i $i")
        return current.step
    }

}