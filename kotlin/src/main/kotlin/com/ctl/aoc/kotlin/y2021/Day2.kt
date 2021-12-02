package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position

object Day2 {

    sealed class Command {
        data class Forward(val n: Int) : Command()
        data class Down(val n: Int) : Command()
        data class Up(val n: Int) : Command()

        companion object {
            fun parse(s: String): Command {
                val parts = s.split(" ")
                return when {
                    s.startsWith("forward") -> {
                        Forward(parts[1].toInt())
                    }
                    s.startsWith("down") -> {
                        Down(parts[1].toInt())
                    }
                    s.startsWith("up") -> {
                        Up(parts[1].toInt())
                    }
                    else -> error(s)
                }
            }
        }
    }

    private fun Position.apply(command: Command): Position {
        return when (command) {
            is Command.Down -> this + Position(0, -1).scalar(command.n)
            is Command.Up -> this + Position(0, 1).scalar(command.n)
            is Command.Forward -> this + Position(1, 0).scalar(command.n)
        }
    }

    data class Submarine(val x: Long, val depth: Long, val aim: Long)

    private fun Submarine.apply(command: Command): Submarine {
        return when (command) {
            is Command.Down -> this.copy(aim = this.aim + command.n)
            is Command.Up -> this.copy(aim = this.aim - command.n)
            is Command.Forward -> this.copy(x = this.x + command.n, depth = this.depth + this.aim * command.n)
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val end = input.map { Command.parse(it) }
                .fold(Position(0, 0)) { p, command -> p.apply(command) }
        println(end)
        return (-end.y) * end.x
    }

    fun solve2(input: Sequence<String>): Long {
        val end = input.map { Command.parse(it) }
                .fold(Submarine(0, 0, 0)) { p, command -> p.apply(command) }
        println(end)
        return end.x * end.depth
    }
}