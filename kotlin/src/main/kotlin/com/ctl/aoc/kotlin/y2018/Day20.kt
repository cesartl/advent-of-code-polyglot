package com.ctl.aoc.kotlin.y2018

import java.lang.IllegalArgumentException

object Day20 {

    fun getPaths(regex: String, prefix: String): Sequence<String> {
//        println()
//        println("regex $regex prefix: $prefix")
        if (regex.isEmpty() || regex.first() == '$') {
            return sequenceOf(prefix)
        }
        var i = 0
        while (i < regex.length && regex[i].isLetter()) {
            i++
        }
        if(i == regex.length){
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

    fun solve1(regex: String): Int {

        return 0
    }
}