package com.ctl.aoc.kotlin.utils

import java.lang.IllegalArgumentException

data class Position(val x: Int, val y: Int)

sealed class Orientation {
    fun move(p: Position): Position {
        return when (this) {
            is N -> Position(p.x, p.y + 1)
            is E -> Position(p.x + 1, p.y)
            is S -> Position(p.x, p.y - 1)
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