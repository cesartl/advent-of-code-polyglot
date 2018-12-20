package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.Graph
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

    fun computePaths(regex: String, start: Position): Graph<Position> {
        val graph = Graph<Position>()
        var currentIdx = 0

        val backtrack: Deque<List<Position>> = ArrayDeque()
        val backLog: Deque<Position> = ArrayDeque() //queue
        backLog.push(start)
        //SSE(EE|N)NN

        while (currentIdx < regex.length) {

            when {
                regex[currentIdx].isLetter() -> {
                    val o = Orientation.parse(regex[currentIdx])
                    var currentPosition: Position
                    var newPosition: Position
                    // we advanced all the backlog positions, taking from the end and adding to the top
                    for (i in 0 until backLog.size) {
                        currentPosition = backLog.removeLast()
                        newPosition = o.move(currentPosition)
                        backLog.addFirst(newPosition)
                        graph.addEdge(currentPosition, newPosition)
                    }
                }
                regex[currentIdx] == '(' -> {
                    backtrack.push(backLog.toList()) // add back track checkpoint for all backlog element
                }
                regex[currentIdx] == '|' -> {
                    // one option for the branch end
                    // we add the backtrack checkpoint to the end of the list
                    // we put backtrack positions at the end so we start from there
                    backtrack.peekFirst().forEach { backLog.addLast(it) }
                }
                regex[currentIdx] == ')' -> // it's the end of the branch, we just remove the backtracking point
                    backtrack.pop()
            }
            currentIdx++
        }
        return graph
    }

    fun solve1(regex: String): Int {

        return 0
    }
}