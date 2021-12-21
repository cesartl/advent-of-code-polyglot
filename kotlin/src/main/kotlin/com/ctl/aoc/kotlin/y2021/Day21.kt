package com.ctl.aoc.kotlin.y2021

object Day21 {

    class Die {
        var idx = 0L
        var count = 0L
        fun roll(): Long {
            val r = idx + 1
            idx = (idx + 1) % 100
            count++
            return r
        }

        fun roll3(): Long = roll() + roll() + roll()
    }

    data class Player(val positionIdx: Long, val score: Long = 0L) {
        fun move(offset: Long): Player {
            val newPos = (positionIdx + offset) % 10
            val newScore = score + newPos + 1
            return Player(newPos, newScore)
        }
    }

    data class GameState(val players: Map<Int, Player>, val die: Die, val currentPlayerIdx: Int) {
        fun isOver(): Boolean = players.any { it.value.score >= 1000 }

        fun next(): GameState {
            val player = players[currentPlayerIdx]!!
            val rolls = die.roll3()
            val newPlayer = player.move(rolls)
            return GameState(players = players + (currentPlayerIdx to newPlayer), die, (currentPlayerIdx + 1) % 2)
        }
    }

    fun solve1(input: Sequence<String>): Long {
        val lines = input.toList()
        val p1Pos = lines[0].last().toString().toLong()
        val p2Pos = lines[1].last().toString().toLong()
        val player1 = Player(p1Pos - 1)
        val player2 = Player(p2Pos - 1)
        val game = GameState(mapOf(0 to player1, 1 to player2), Die(), 0)
        val result = generateSequence(game) { it.next() }.find { it.isOver() }!!
        val loosing = result.players.minByOrNull { it.value.score }!!.value
        return loosing.score * result.die.count
    }

    data class UniverseCount(val p1: Long, val p2: Long) {
        operator fun plus(other: UniverseCount): UniverseCount {
            return UniverseCount(p1 + other.p1, p2 + other.p2)
        }

        companion object {
            fun p1Wins(): UniverseCount = UniverseCount(1L, 0L)
            fun p2Wins(): UniverseCount = UniverseCount(0L, 1L)
        }
    }

    infix fun Long.x(count: UniverseCount): UniverseCount {
        return UniverseCount(count.p1 * this, count.p2 * this)
    }


    fun playDirac(p1: Player, p2: Player, p1Start: Boolean = true): UniverseCount {
        if (p1.score >= 21) {
            return UniverseCount.p1Wins()
        }
        if (p2.score >= 21) {
            return UniverseCount.p2Wins()
        }
        val p = if (p1Start) p1 else p2
        val m3 = p.move(3)
        val m4 = p.move(4)
        val m5 = p.move(5)
        val m6 = p.move(6)
        val m7 = p.move(7)
        val m8 = p.move(8)
        val m9 = p.move(9)
        var total = UniverseCount(0, 0)
        if (p1Start) {
            total += 1L x playDirac(m3, p2, false)
            total += 3L x playDirac(m4, p2, false)
            total += 6L x playDirac(m5, p2, false)
            total += 7L x playDirac(m6, p2, false)
            total += 6L x playDirac(m7, p2, false)
            total += 3L x playDirac(m8, p2, false)
            total += 1L x playDirac(m9, p2, false)
        } else {
            total += 1L x playDirac(p1, m3, true)
            total += 3L x playDirac(p1, m4, true)
            total += 6L x playDirac(p1, m5, true)
            total += 7L x playDirac(p1, m6, true)
            total += 6L x playDirac(p1, m7, true)
            total += 3L x playDirac(p1, m8, true)
            total += 1L x playDirac(p1, m9, true)
        }
        return total
    }

    fun solve2(input: Sequence<String>): Long {
        val lines = input.toList()
        val p1Pos = lines[0].last().toString().toLong()
        val p2Pos = lines[1].last().toString().toLong()
        val player1 = Player(p1Pos - 1)
        val player2 = Player(p2Pos - 1)
        val result = playDirac(player1, player2)
        println(result)
        return listOf(result.p1, result.p2).maxOrNull()!!
    }
}