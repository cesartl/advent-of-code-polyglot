package com.ctl.aoc.kotlin.y2015

object Day14 {
    data class Reindeer(val name: String, val flySpeed: Int, val flyTime: Int, val rest: Int) {
        companion object {
            val regex = """([\w]+) can fly ([\d]+) km\/s for ([\d]+) seconds, but then must rest for ([\d]+) seconds\.""".toRegex()
            fun parse(s: String): Reindeer {
                val m = regex.matchEntire(s)
                if (m != null) {
                    return Reindeer(m.groupValues[1], m.groupValues[2].toInt(), m.groupValues[3].toInt(), m.groupValues[4].toInt())
                }
                throw IllegalArgumentException(s)
            }
        }

        fun distanceAfterT(seconds: Int): Int {
            var d = 0
            var t = 0
            var isResting = false
            while (t <= seconds) {
                if (isResting) {
                    t += rest
                    isResting = false
                } else {
                    val totalFlight = Math.min(flyTime, seconds - t)
                    d += totalFlight * flySpeed
                    t += totalFlight
                    isResting = true
                }
            }
            return d
        }
    }

    data class ReindeerState(val reindeer: Reindeer, val isResting: Boolean = false, val distanceTravelled: Int = 0, val lastCheckpoint: Int = 0, val points: Int = 0) {

        private val nextCheckpoint = (if (isResting) reindeer.rest else reindeer.flyTime) + lastCheckpoint

        fun move(time: Int): ReindeerState {
            var newDistanceTravelled = distanceTravelled
            if (!isResting) {
                newDistanceTravelled += reindeer.flySpeed
            }
            return if (time >= nextCheckpoint) {
                this.copy(isResting = !isResting, lastCheckpoint = nextCheckpoint, distanceTravelled = newDistanceTravelled)
            } else {
                this.copy(distanceTravelled = newDistanceTravelled)
            }
        }

        fun givePoint(): ReindeerState = this.copy(points = this.points + 1)
    }

    fun solve1(lines: Sequence<String>, seconds: Int): Int {
        return lines.map { Reindeer.parse(it) }.map { it to it.distanceAfterT(seconds) }.maxByOrNull { it.second }?.second
                ?: 0
    }

    fun solve2(lines: Sequence<String>, seconds: Int): Int {
        var states = lines.map { Reindeer.parse(it) }.map { ReindeerState(it) }.toList()

        var t = 1
        while (t <= seconds) {
            states = states.map { it.move(t) }
            val best = states.maxByOrNull { it.distanceTravelled }?.distanceTravelled!!
            val (first, other) = states.partition { it.distanceTravelled == best }
            states = first.map { it.givePoint() } + other
            t++
        }
        return states.maxByOrNull { it.points }?.points ?: 0
    }
}