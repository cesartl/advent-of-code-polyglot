package com.ctl.aoc.kotlin.y2019

import java.lang.IllegalArgumentException

object Day2 {
    fun solve1(program: IntArray): Int {
        program[1] = 12
        program[2] = 2
        executeProgram(program)
        return program[0]
    }

    fun solve2(program: IntArray): Int {
//        println("*** ${250661 * 2}")
//        (0..10).forEach {
//            println(executeProgram(program.clone(), 1, it))
//        }

        var left = 0
        var right = 99
        val target = 19690720
        while (left <= right) {
            val mid = (left + right) / 2;
            val result = executeProgram(program.clone(), mid, 0)
            println("mid $mid, left $left, right $right, result $result")

            if (result >= target) {
                right = mid - 1
            } else if (result < target) {
                left = mid + 1
            } else {
                break
            }
        }
        val noun = left - 1
        val tmp = executeProgram(program.clone(), noun, 0)
        val diff = (target - tmp)

        val verb = diff
        println("diff $diff")

        println("noun $noun, verb $verb, result ${executeProgram(program.clone(), noun, verb)}")
        return 100 * noun + verb
    }

    fun executeProgram(program: IntArray, noun: Int, verb: Int): Int {
        program[1] = noun
        program[2] = verb
        executeProgram(program)
        return program[0]
    }

    fun executeProgram(program: IntArray): IntArray {
        var i = 0
        while (true) {
            if (program[i] == 1) {
                program[program[i + 3]] = program[program[i + 1]] + program[program[i + 2]]
            } else if (program[i] == 2) {
                program[program[i + 3]] = program[program[i + 1]] * program[program[i + 2]]
            } else if (program[i] == 99) {
                break
            } else {
                throw IllegalArgumentException("Illegal opp code ${program[i]}")
            }
            i += 4
        }
        return program
    }
}