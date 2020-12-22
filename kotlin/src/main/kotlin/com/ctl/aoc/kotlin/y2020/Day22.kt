package com.ctl.aoc.kotlin.y2020

import java.util.*

object Day22 {

    fun solve1(input: String): Int {
        val game = Game.parse(input)
        while (!game.isDone()) {
            game.next()
        }
        return (game.player1.toList().reversed() + game.player2.toList().reversed())
                .mapIndexed { index, i -> (index + 1) * i }.sum()
    }

    fun solve2(input: String): Int {
        TODO()
    }

    data class Game(val player1: Deque<Int>, val player2: Deque<Int>) {

        fun isDone() = player1.isEmpty() || player2.isEmpty()

        fun next() {
            val p1 = player1.pop()
            val p2 = player2.pop()
            if (p1 > p2) {
                player1.addLast(p1)
                player1.addLast(p2)
            } else {
                player2.addLast(p2)
                player2.addLast(p1)
            }
        }

        companion object {
            fun parse(input: String): Game {
                val parts = input.split("\n\n")
                val p1 = parts[0].split("\n").drop(1).map { it.toInt() }.toList()
                val p2 = parts[1].split("\n").drop(1).map { it.toInt() }.toList()

                val player1 = ArrayDeque<Int>(p1.size)
                p1.forEach { player1.addLast(it) }

                val player2 = ArrayDeque<Int>(p2.size)
                p2.forEach { player2.addLast(it) }

                return Game(player1, player2)
            }
        }
    }
}