package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.sign

object Day13 {

    sealed class Tile {
        object Empty : Tile()
        object Wall : Tile()
        object Block : Tile()
        object Paddle : Tile()
        object Ball : Tile()

        companion object {
            fun fromCode(code: Long): Tile {
                return when (code) {
                    0L -> Empty
                    1L -> Wall
                    2L -> Block
                    3L -> Paddle
                    4L -> Ball
                    else -> throw IllegalArgumentException("$code")
                }
            }
        }
    }

    //TODO tile with point
    data class TileAtPoint(val tile: Tile, val point: Point)

    data class Point(val x: Long, val y: Long)

    fun solve1(intCode: LongArray): Int {
        val grid = mutableMapOf<Point, Tile>()
        val count = AtomicInteger(0)
        var x = 0L
        var y = 0L
        var tile: Tile = Tile.Empty
        val state = Day9.IntCodeState(intCode = intCode.copyOf(9999), output = {
            val i = count.getAndIncrement()
            when {
                i % 3 == 0 -> {
                    x = it
                }
                i % 3 == 1 -> {
                    y = it
                }
                else -> {
                    tile = Tile.fromCode(it)
                    grid[Point(x, y)] = tile
                }
            }
        })
        Day9.run {
            state.execute()
        }
        return grid.values.filterIsInstance(Tile.Block.javaClass).size
    }

    fun solve2(intCode: LongArray): Long {
        return runGame(intCode)
    }

    data class GameState(val grid: Map<Point, Tile>, val score: Long = 0) {
        fun rows(): List<List<TileAtPoint>> {
            val maxX = grid.keys.maxBy { it.x }?.x ?: 0
            val maxY = grid.keys.maxBy { it.y }?.y ?: 0
            val rows = mutableListOf<List<TileAtPoint>>()
            (0..maxY).forEach { y ->
                val row = mutableListOf<TileAtPoint>()
                (0..maxX).forEach { x ->
                    row.add(TileAtPoint((grid[Point(x, y)] ?: Tile.Empty), Point(x, y)))
                }
                rows.add(row)
            }
            return rows
        }
    }

    public fun runGame(intCode: LongArray, viewUpdate: (GameState) -> Unit = {}): Long {
        val grid = mutableMapOf<Point, Tile>()

        var ball: Point = Point(0, 0)
        var paddle: Point = Point(0, 0)

        val count = AtomicInteger(0)
        val shouldUpdateView = AtomicBoolean(false)
        var x = 0L
        var y = 0L
        var tile: Tile = Tile.Empty
        intCode[0] = 2
        var score = 0L
        val state = Day9.IntCodeState(intCode = intCode.copyOf(9999), output = {
            val i = count.getAndIncrement()
            when {
                i % 3 == 0 -> {
                    x = it
                }
                i % 3 == 1 -> {
                    y = it
                }
                else -> {
                    val p = Point(x, y)
                    if (p == Point(-1, 0)) {
                        score = it
                    } else {
                        tile = Tile.fromCode(it)
                        grid[p] = tile
                        when (tile) {
                            is Tile.Ball -> {
                                ball = p
                            }
                            is Tile.Paddle -> {
                                paddle = p
                            }
                        }
                        if (shouldUpdateView.get()) {
                            viewUpdate(GameState(grid, score))
                        }
                    }
                }
            }
        }, input = {
            shouldUpdateView.set(true)
            (ball.x - paddle.x).sign.toLong()
        })
        Day9.run {
            state.execute()
        }
        return score
    }
}