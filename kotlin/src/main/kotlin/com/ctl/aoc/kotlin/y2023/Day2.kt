package com.ctl.aoc.kotlin.y2023

data class Game(
    val id: Int,
    val trials: List<Cubes>
)

data class Cubes(
    val red: Int,
    val green: Int,
    val blue: Int
)

val gameRegex = """Game (\d+): """.toRegex()

private fun String.parseGame(): Game {
    val idMatch = gameRegex.find(this) ?: error("Couldn't find game ID")
    val id = idMatch.groupValues[1].toInt()
    val trials = this
        .substring(idMatch.range.last)
        .splitToSequence(";")
        .map { it.trim() }
        .map { it.parseTrial() }
        .toList()
    return Game(id, trials)
}

val redRegex = """(\d+) red""".toRegex()
val greenRegex = """(\d+) green""".toRegex()
val blueRegex = """(\d+) blue""".toRegex()

private fun String.parseTrial(): Cubes {
    val red = redRegex.find(this)?.groupValues?.get(1)?.toInt() ?: 0
    val green = greenRegex.find(this)?.groupValues?.get(1)?.toInt() ?: 0
    val blue = blueRegex.find(this)?.groupValues?.get(1)?.toInt() ?: 0
    return Cubes(red = red, green = green, blue = blue)
}

fun Game.atMost(cubes: Cubes): Boolean {
    return trials.all { (red, green, blue) ->
        red <= cubes.red && green <= cubes.green && blue <= cubes.blue
    }
}

fun Game.atLeast(): Cubes {
    return this.trials
        .fold(Cubes(0, 0, 0)) { acc, cubes ->
            Cubes(
                red = acc.red.coerceAtLeast(cubes.red),
                green = acc.green.coerceAtLeast(cubes.green),
                blue = acc.blue.coerceAtLeast(cubes.blue)
            )
        }
}

fun Cubes.power(): Int {
    return red * green * blue
}


object Day2 {
    fun solve1(input: Sequence<String>): Int {
        val limit = Cubes(12, 13, 14)
        return input
            .map { it.parseGame() }
            .filter { it.atMost(limit) }
            .map { it.id }
            .sum()
    }

    fun solve2(input: Sequence<String>): Int {
        return input
            .map { it.parseGame() }
            .map { it.atLeast() }
            .map { it.power() }
            .sum()
    }
}
