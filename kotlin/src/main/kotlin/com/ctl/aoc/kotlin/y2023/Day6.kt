package com.ctl.aoc.kotlin.y2023

data class Race(
    val time: Int,
    val distance: Long
)

private fun Race.holdButton(duration: Int): Long {
    return duration * (time - duration).toLong()
}

private fun Race.options(): Sequence<Long> = sequence {
    repeat(time) {
        yield(holdButton(it))
    }
}

private fun parseRaces(input: Sequence<String>): List<Race> {
    val l = input.filter { it.isNotEmpty() }.toList()
    val regex = """\s+""".toRegex()
    val times = l[0].splitToSequence(regex).filter { it.isNotEmpty() }.drop(1).map { it.toInt() }
    val distances = l[1].splitToSequence(regex).filter { it.isNotEmpty() }.drop(1).map { it.toLong() }
    return times.zip(distances)
        .map { (time, distance) -> Race(time, distance) }
        .toList()
}

private fun parseRace(input: Sequence<String>): Race {
    val l = input.filter { it.isNotEmpty() }.toList()
    val regex = """\s+""".toRegex()
    val time = l[0].splitToSequence(regex).filter { it.isNotEmpty() }.drop(1).joinToString(separator = "").toInt()
    val distance = l[1].splitToSequence(regex).filter { it.isNotEmpty() }.drop(1).joinToString(separator = "").toLong()
    return Race(time, distance)
}

object Day6 {
    fun solve1(input: Sequence<String>): Long {
        val races = parseRaces(input)
        return races.asSequence()
            .map { race -> race.options().filter { it > race.distance }.count().toLong() }
            .fold(1L) { acc, l -> acc * l }
    }

    fun solve2(input: Sequence<String>): Int {
        val race = parseRace(input)
        println(race)
        return race
            .options()
            .dropWhile { it < race.distance }
            .takeWhile { it > race.distance }
            .count()
    }
}
