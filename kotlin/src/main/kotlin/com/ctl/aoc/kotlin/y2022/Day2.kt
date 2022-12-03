package com.ctl.aoc.kotlin.y2022


object Day2 {

    enum class RPS : Comparator<RPS> {
        Rock, Paper, Scissors;

        override fun compare(o1: RPS, o2: RPS): Int {
            return (o1.ordinal - o2.ordinal) % RPS.values().size
        }

        val score: Int = ordinal + 1

        fun compete(other: RPS): Outcome {
            return if (this > other) {
                Outcome.Win
            } else if (this < other) {
                Outcome.Loose
            } else {
                Outcome.Draw
            }
        }
    }

    private fun RPS.advance(n: Int): RPS {
        return RPS.values()[(this.ordinal + n) % RPS.values().size]
    }

    enum class Outcome(val offset: Int) {
        Win(-1), Loose(1), Draw(0);

        val score: Int = 3 * ordinal
    }

    data class Round(val other: RPS, val me: RPS) {
        val score: Int by lazy {
            me.score + me.compete(other).score
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
        return input
            .map { line ->
                val split = line.split(" ")
                split[0].toRPS() to split[1].toOutcome()
            }
            .map { (other, outcome) ->
                Round(other, other.advance(outcome.offset))
            }
            .map { it.score }
            .sum()
    }
}
