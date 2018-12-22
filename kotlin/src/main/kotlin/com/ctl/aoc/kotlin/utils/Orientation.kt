package com.ctl.aoc.kotlin.utils

data class Position(val x: Int, val y: Int) {
    fun adjacents(): Sequence<Position> = sequenceOf(N, S, E, W).map { it.move(this) }
    fun distance(other: Position) : Int = Math.abs(other.x - x) + Math.abs(other.y - y)
}

sealed class Orientation {
    fun move(p: Position): Position {
        return when (this) {
            is N -> Position(p.x, p.y - 1)
            is E -> Position(p.x + 1, p.y)
            is S -> Position(p.x, p.y + 1)
            is W -> Position(p.x - 1, p.y)
        }
    }

    companion object {
        fun parse(char: Char): Orientation {
            return when (char) {
                'E' -> E
                'W' -> W
                'S' -> S
                'N' -> N
                else -> throw IllegalArgumentException(char.toString())
            }
        }
    }
}

object E : Orientation()
object W : Orientation()
object S : Orientation()
object N : Orientation()