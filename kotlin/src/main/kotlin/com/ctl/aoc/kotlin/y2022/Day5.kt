package com.ctl.aoc.kotlin.y2022

object Day5 {

    private val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()

    data class Move(val n: Int, val from: Int, val to: Int)

    private fun String.toMove(): Move = regex.matchEntire(this)?.let {
        val n = it.groupValues[1].toInt()
        val from = it.groupValues[2].toInt()
        val to = it.groupValues[3].toInt()
        Move(n, from, to)
    } ?: error(this)

    data class Crates(
        val stacks: List<ArrayDeque<Char>> = (0..8).map { ArrayDeque() }
    ) {
        fun apply(move: Move) {
            repeat(move.n) {
                val c = stacks[move.from - 1].removeFirst()
                stacks[move.to - 1].addFirst(c)
            }
        }

        fun apply2(move: Move) {
            val buffer = ArrayDeque<Char>()
            repeat(move.n) {
                val c = stacks[move.from - 1].removeFirst()
                buffer.addFirst(c)
            }
            buffer.forEach { c ->
                stacks[move.to - 1].addFirst(c)
            }
        }

        fun topCrates(): String {
            return stacks.map { it.first() }.joinToString("")
        }
    }

    fun solve1(input: Sequence<String>): String {
        val (crates, moves) = parseInput(input)
        moves.forEach {
            crates.apply(it)
        }
        return crates.topCrates()
    }

    fun solve2(input: Sequence<String>): String {
        val (crates, moves) = parseInput(input)
        moves.forEach {
            crates.apply2(it)
        }
        return crates.topCrates()
    }

    private fun parseInput(input: Sequence<String>): Pair<Crates, List<Move>> {
        val crates = Crates()
        val splitIndex = input.indexOf("")
        println("Split index: $splitIndex")
        input.take(splitIndex - 1)
            .map { line -> line.chunked(4).map { it.trim() } }
            .forEach { elements ->
                elements.forEachIndexed { crateNum, s ->
                    if (s.isNotEmpty()) {
                        val stack = crates.stacks[crateNum]
                        stack.addLast(s[1])
                    }
                }
            }
        val moves = input.drop(splitIndex + 1).map { it.toMove() }.toList()
        return crates to moves
    }
}
