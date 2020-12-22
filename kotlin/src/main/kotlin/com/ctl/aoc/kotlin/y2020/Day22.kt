package com.ctl.aoc.kotlin.y2020

import java.util.*

object Day22 {

    fun solve1(input: String): Int {
        var game = Game.parse(input)
        game.play()
        return game.score()
    }

    fun solve2(input: String): Int {
        var game = Game.parse(input)
        game.play2()
        return game.score()
    }

    data class Game(val player1: ArrayDeque<Int>, val player2: ArrayDeque<Int>) {

        fun isDone() = player1.isEmpty() || player2.isEmpty()

        fun play() {
            while (!isDone()) {
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
        }

        fun play2() {
            val previousRounds: MutableSet<Pair<List<Int>, List<Int>>> = mutableSetOf()
            while (!isDone()) {
                val p1List = player1.toList()
                val p2List = player2.toList()
                if (previousRounds.contains(p1List to p2List)) {
                    player2.clear()
                } else {
                    previousRounds.add(player1.toList() to player2.toList())
                    val p1 = player1.pop()
                    val p2 = player2.pop()

                    var p1Wins = p1 > p2
                    if (player1.size >= p1 && player2.size >= p2) {
                        val recursiveGame = Game(p1List.drop(1).take(p1).let { ArrayDeque(it) }, p2List.drop(1).take(p2).let { ArrayDeque(it) })
                        recursiveGame.play2()
                        p1Wins = recursiveGame.player2.isEmpty()
                    }

                    if (p1Wins) {
                        player1.addLast(p1)
                        player1.addLast(p2)
                    } else {
                        player2.addLast(p2)
                        player2.addLast(p1)
                    }
                }
            }
        }

        fun score() = (player1.toList().reversed() + player2.toList().reversed())
                .mapIndexed { index, i -> (index + 1) * i }.sum()

        companion object {
            fun parse(input: String): Game {
                val parts = input.split("\n\n")
                val p1 = parts[0].split("\n").drop(1).map { it.toInt() }.toList()
                val p2 = parts[1].split("\n").drop(1).map { it.toInt() }.toList()
                return Game(ArrayDeque(p1), ArrayDeque(p2))
            }
        }

    }
}