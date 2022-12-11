package com.ctl.aoc.kotlin.y2022

import tornadofx.*

private typealias Worry = Long

object Day11 {

    data class Monkey(
        val id: Int,
        val items: MutableList<Worry>,
        val operation: (Worry) -> Worry,
        val test: Worry,
        val throwIfTrue: Int,
        val throwIfFalse: Int,
        var inspectCount: Worry
    )

    private fun String.toMonkey(): Monkey {
        val lines = this.split("\n")
        val id = lines[0].dropLast(1).takeLast(1).toInt()
        val items = lines[1].split(":")[1].trim()
            .split(",").map { it.trim().toLong() }

        val opString = lines[2].split("=")[1].trim().split(" ")

        val operation: (Worry) -> Worry = {
            val b = if (opString[2].isInt()) {
                opString[2].toLong()
            } else {
                it
            }
            when (opString[1]) {
                "*" -> it * b
                "+" -> it + b
                else -> error(opString)
            }
        }
        val test = lines[3].trim().split(" ").last().toLong()
        val throwIfTrue = lines[4].takeLast(1).toInt()
        val throwIfFalse = lines[5].takeLast(1).toInt()
        return Monkey(
            id = id,
            items = items.toMutableList(),
            operation = operation,
            test = test,
            throwIfTrue = throwIfTrue,
            throwIfFalse = throwIfFalse,
            inspectCount = 0L
        )
    }

    data class State(
        val monkeys: List<Monkey>
    ) {

        private val N = monkeys.map { it.test }.fold(1L) { acc, i -> acc * i }

        private val byId = monkeys.associateBy { it.id }

        fun next() {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    monkey.inspectCount++
                    val newItem = monkey.operation(item) / 3L
                    if (newItem % monkey.test == 0L) {
                        byId[monkey.throwIfTrue]!!.items.add(newItem)
                    } else {
                        byId[monkey.throwIfFalse]!!.items.add(newItem)
                    }
                }
            }
        }

        fun next2() {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    monkey.inspectCount++
                    val newItem = monkey.operation(item) % N
                    if (newItem % monkey.test == 0L) {
                        byId[monkey.throwIfTrue]!!.items.add(newItem)
                    } else {
                        byId[monkey.throwIfFalse]!!.items.add(newItem)
                    }
                }
            }
        }
    }

    fun solve1(input: String): Worry {
        val monkeys = input.splitToSequence("\n\n")
            .map { it.toMonkey() }
            .toList()
        val state = State(monkeys)
        repeat(20) {
            state.next()
        }
        return state.monkeys
            .map { it.inspectCount }
            .sortedDescending()
            .take(2)
            .fold(1L) { acc, i -> acc * i }
    }

    fun solve2(input: String): Worry {
        val monkeys = input
            .splitToSequence("\n\n")
            .map { it.toMonkey() }
            .toList()
        val state = State(monkeys)
        repeat(10_000) {
            state.next2()
        }
        return state.monkeys
            .map { it.inspectCount }
            .sortedDescending()
            .take(2)
            .fold(1L) { acc, i -> acc * i }
    }
}
