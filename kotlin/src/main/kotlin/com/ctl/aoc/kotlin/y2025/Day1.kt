package com.ctl.aoc.kotlin.y2025

object Day1 {
    fun solve1(input: Sequence<String>): Int {
        val n = 100
        val positions = input.runningFold(50) { dialPosition, rotation ->
            val sign = if (rotation.startsWith("L")) {
                -1
            } else {
                1
            }
            val count = rotation.drop(1).toInt() % n
            (dialPosition + sign * count + n) % n
        }
        return positions.count { it == 0 }
    }

    fun solve2(input: Sequence<String>): Int {
        var current = 50
        val n = 100
        val positions = sequence {
            input.forEach { rotation ->
                val sign = when (val direction = rotation[0]) {
                    'L' -> -1
                    'R' -> 1
                    else -> error("Unknown direction $direction")
                }
                val count = rotation.drop(1).toInt()
                repeat(count) {
                    current = (current + sign + n) % n
                    yield(current)
                }
            }
        }
        return positions.count { it == 0 }
    }

    fun solve2Bis(input: Sequence<String>): Int {
        val n = 100
        val (_, zeroCount) = input.fold(50 to 0) { (dialPosition, zeroCount), rotation ->
            val direction = rotation[0]
            val sign = when (direction) {
                'L' -> -1
                'R' -> 1
                else -> error("Unknown direction $direction")
            }
            val count = rotation.drop(1).toInt()
            val k0 = when (direction) {
                'R' -> {
                    (n - dialPosition) % n
                }

                'L' -> {
                    dialPosition % n
                }

                else -> error("Unknown direction $direction")
            }

            val newZeroCount = if (dialPosition == 0) {
                count / n
            } else if (count < k0) {
                0
            } else {
                1 + (count - k0) / n
            }

            val newPosition = dialPosition + sign * (count % n)
            (newPosition + n) % n to zeroCount + newZeroCount
        }
        return zeroCount
    }
}
