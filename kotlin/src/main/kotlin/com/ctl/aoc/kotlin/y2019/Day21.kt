package com.ctl.aoc.kotlin.y2019

object Day21 {

    data class SpringScript(val lines: List<String>) {
        fun asInput(): () -> Long {
            val asciis = mutableListOf<Int>()
            lines.forEach { line ->
                line.forEach { char -> asciis.add(char.toInt()) }
                asciis.add('\n'.toInt())
            }
            "WALK\n".forEach { asciis.add(it.toInt()) }
            val iterator = asciis.iterator()
            return {
                iterator.next().toLong()
            }
        }
    }

    class Output() {
        val lines = mutableListOf<String>()
        var dmg: Int? = null
        private var currentLine = StringBuilder()
        fun receiveOutput(v: Int) {
            if (v > 500) {
                dmg = v
            } else {
                if (v == '\n'.toInt()) {
                    lines.add(currentLine.toString())
                    currentLine = StringBuilder()
                } else {
                    currentLine.append(v.toChar())
                }
            }
        }

        fun print() {
            lines.forEach { println(it) }
        }

    }

    fun solve1(puzzleInput: String): Int {
        val intCode = puzzleInput.split(",").map { it.toLong() }.toLongArray()
        val springScript = SpringScript(
                """
//jump if !B!C
//NOT B J
//NOT C T
NOT B T
OR T J
// jump if !A
NOT A T
OR T J
NOT C T
OR T J
// jump if !BC
//
//
// CHECK D before jumping
AND D J
""".split("\n").drop(1).dropLast(1).filter { !it.contains("//") }
        )
        val output = Output()
        val intCodeState = Day9.IntCodeState(intCode = intCode.copyOf(9999), input = springScript.asInput(), output = { output.receiveOutput(it.toInt()) })
        Day9.run {
            intCodeState.execute()
        }
        output.print()
        return output.dmg?.toInt() ?: -1
    }
}