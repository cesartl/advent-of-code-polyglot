package com.ctl.aoc.kotlin.y2020

import java.util.concurrent.ConcurrentHashMap

object Day22 {

    fun solve1(input: String): Int {
        var game = Game.parse(input)
        while (!game.isDone()) {
            game = game.next()
        }
        return (game.player1.toList().reversed() + game.player2.toList().reversed())
                .mapIndexed { index, i -> (index + 1) * i }.sum()
    }

    fun solve2(input: String): Int {
        cache.clear()
        var game = Game.parse(input)
        while (!game.isDone()) {
            game = game.recursiveCombat()
        }
        return (game.player1.toList().reversed() + game.player2.toList().reversed())
                .mapIndexed { index, i -> (index + 1) * i }.sum()
    }


    val cache = ConcurrentHashMap<Game, Game>()


    fun Game.recursiveCombat(): Game {
        if (isDone()) {
            return this
        }
        if (previousRounds.contains(this)) {
//            println("Won")
            return copy(player2 = listOf())
        }
        val p1 = player1.first()
        val p2 = player2.first()
        return if ((player1.size - 1) >= p1 && (player2.size - 1) >= p2) {
            var recursiveGame = Game(player1 = player1.drop(1).take(p1), player2 = player2.drop(1).take(p2))
            while (!recursiveGame.isDone()) {
                recursiveGame = recursiveGame.recursiveCombat()
            }

//            recursiveGame = cache.computeIfAbsent(recursiveGame) {
//                var current = it
//                while (!current.isDone()) {
//                    current = current.recursiveCombat()
//                }
//                current
//            }
            when {
                recursiveGame.player2.isEmpty() -> {
                    copy(player1 = player1.drop(1) + listOf(p1, p2), player2 = player2.drop(1), previousRounds = previousRounds + this)
                }
                recursiveGame.player1.isEmpty() -> {
                    copy(player1 = player1.drop(1), player2 = player2.drop(1) + listOf(p2, p1), previousRounds = previousRounds + this)
                }
                else -> {
                    error("bad recursive game")
                }
            }
        } else {
            if (p1 > p2) {
                copy(player1 = player1.drop(1) + listOf(p1, p2), player2 = player2.drop(1), previousRounds = previousRounds + this)
            } else {
                copy(player1 = player1.drop(1), player2 = player2.drop(1) + listOf(p2, p1), previousRounds = previousRounds + this)
            }
        }


    }


    data class Game(val player1: List<Int>, val player2: List<Int>, val previousRounds: Set<Game> = setOf()) {

        fun isDone() = player1.isEmpty() || player2.isEmpty()

        fun next(): Game {
            val p1 = player1.first()
            val p2 = player2.first()
            return if (p1 > p2) {
                copy(player1 = player1.drop(1) + listOf(p1, p2), player2 = player2.drop(1))
            } else {
                copy(player1 = player1.drop(1), player2 = player2.drop(1) + listOf(p2, p1))
            }
        }

        fun play2(previousGames: Set<Game>): Game {
            TODO()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Game

            if (player1 != other.player1) return false
            if (player2 != other.player2) return false

            return true
        }

        override fun hashCode(): Int {
            var result = player1.hashCode()
            result = 31 * result + player2.hashCode()
            return result
        }

        companion object {
            fun parse(input: String): Game {
                val parts = input.split("\n\n")
                val p1 = parts[0].split("\n").drop(1).map { it.toInt() }.toList()
                val p2 = parts[1].split("\n").drop(1).map { it.toInt() }.toList()
                return Game(p1, p2)
            }
        }

    }
}