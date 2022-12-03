package com.ctl.aoc.kotlin.y2022

object Day2 {

    enum class RPS {
        Rock, Paper, Scissors
    }

    fun RPS.score(): Int {
        return when (this) {
            RPS.Rock -> 1
            RPS.Paper -> 2
            RPS.Scissors -> 3
        }
    }

    fun RPS.findMe(outcome: Outcome): RPS {
        return RPS.values().first { it.compete(this) == outcome }
    }

    enum class Outcome {
        Win, Loose, Draw
    }

    fun Outcome.score(): Int = when (this) {
        Outcome.Win -> 6
        Outcome.Draw -> 3
        Outcome.Loose -> 0
    }

    fun RPS.compete(other: RPS): Outcome {
        return if (this == other) {
            Outcome.Draw
        } else if (this == RPS.Rock) {
            if (other == RPS.Paper) {
                Outcome.Loose
            } else {
                Outcome.Win
            }
        } else if (this == RPS.Paper) {
            if (other == RPS.Scissors) {
                Outcome.Loose
            } else {
                Outcome.Win
            }
            //Scissors
        } else {
            assert(this == RPS.Scissors)
            if (other == RPS.Rock) {
                Outcome.Loose
            } else {
                Outcome.Win
            }
        }
    }

    data class Round(val other: RPS, val me: RPS) {
        val score: Int by lazy {
            me.score() + me.compete(other).score()
        }
    }

    fun String.toRPS(): RPS {
        return when (this) {
            "A" -> RPS.Rock
            "B" -> RPS.Paper
            "C" -> RPS.Scissors
            "X" -> RPS.Rock
            "Y" -> RPS.Paper
            "Z" -> RPS.Scissors
            else -> error(this)
        }
    }

    fun String.toOutcome(): Outcome {
        return when (this) {
            "X" -> Outcome.Loose
            "Y" -> Outcome.Draw
            "Z" -> Outcome.Win
            else -> error(this)
        }
    }

    fun solve1(input: Sequence<String>): Int {
        return input.map { line ->
            val split = line.split(" ")
            Round(split[0].toRPS(), split[1].toRPS())
        }
            .map { it.score }
            .sum()
    }

    fun solve2(input: Sequence<String>): Int {
        return input.map { line ->
            val split = line.split(" ")
            split[0].toRPS() to split[1].toOutcome()
        }.map { (other, outcome) ->
            Round(other, other.findMe(outcome))
        }.map { it.score }
            .sum()
    }
}
