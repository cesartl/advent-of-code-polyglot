package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.Orientation
import com.ctl.aoc.kotlin.utils.Position
import java.util.*

object Day20 {

    fun getPaths(regex: String, prefix: String): Sequence<String> {
//        println()
//        println("regex $regex prefix: $prefix")
//        println("prefix: $prefix")
        if (regex.isEmpty() || regex.first() == '$') {
            return sequenceOf(prefix)
        }
        var i = 0
        while (i < regex.length && regex[i].isLetter()) {
            i++
        }
        if (i == regex.length) {
            return sequenceOf(prefix + regex)
        }
        if (regex[i] == '$') {
            return sequenceOf(prefix + regex.substring(0, i))
        }
        if (regex[i] != '(') throw IllegalArgumentException(regex[i].toString())

        //counting | and (
        var leftBracket = 1
        var pipeIndices = mutableListOf<Int>()
        var j = i + 1
        while (leftBracket > 0) {
            if (regex[j] == '(') leftBracket += 1
            else if (regex[j] == ')') leftBracket -= 1
            else if (regex[j] == '|' && leftBracket == 1) pipeIndices.add(j)
            j++
        }
        pipeIndices.add(j - 1)
        var branches = pipeIndices.fold(i + 1 to listOf<String>()) { (start, acc), idx ->
            idx + 1 to acc + regex.substring(start, idx)
        }.second
        val newPrefix = prefix + regex.substring(0 until i)
//        println("branches: $branches")
        return sequence {
            branches.forEach { branches ->
                getPaths(branches, "").forEach { branch ->
                    getPaths(regex.substring(j), newPrefix + branch).forEach { yield(it) }
                }
            }
        }
    }

    fun computePaths(regex: String, start: Position): Map<Position, Int> {
        var currentIdx = 0

        val backLog: Deque<Position> = ArrayDeque() //queue
        var currentPosition = start

        val distance = mutableMapOf<Position, Int>()
        distance[start] = 0

        while (currentIdx < regex.length) {
            when {
                regex[currentIdx].isLetter() -> {
                    val o = Orientation.parse(regex[currentIdx])
                    var newPosition: Position = o.move(currentPosition)

                    val d = Math.min(distance[currentPosition]!! + 1, distance[newPosition] ?: Int.MAX_VALUE)
                    distance[newPosition] = d
                    currentPosition = newPosition
                }
                regex[currentIdx] == '(' -> {
                    backLog.push(currentPosition)
                }

                regex[currentIdx] == ')' -> {
                    currentPosition = backLog.removeFirst()
                }
                regex[currentIdx] == '|' -> {
                    currentPosition = backLog.peekFirst()
                }

            }
            currentIdx++
        }
        return distance
    }

    fun solve1(regex: String): Int {
        val start = Position(0, 0)
        val distances = computePaths(regex, start)
        return distances.values.max() ?: 0
    }

    fun solve2(regex: String): Int {
        val start = Position(0, 0)
        val distances = computePaths(regex, start)
        return distances.values.count { it >= 1000 }
    }
}