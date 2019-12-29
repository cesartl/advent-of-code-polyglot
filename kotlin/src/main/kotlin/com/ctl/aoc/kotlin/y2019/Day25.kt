package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors.newFixedThreadPool
import java.util.concurrent.TimeUnit

object Day25 {
    val puzzleInput = InputUtils.getString(2019, 25).split(',').map { it.toLong() }
            .toLongArray()

    fun solve1(intCode: LongArray) {

        val inputQueue = ArrayBlockingQueue<Int>(100)

        val intCodeState = Day9.IntCodeState(intCode = intCode.copyOf(9999),
                output = {
                    print(it.toInt().toChar())
                }, input = {
            inputQueue.take().toLong()
        })

        val pool = newFixedThreadPool(1)

        pool.submit {
            Day9.run {
                intCodeState.execute()
            }
        }

        val scanner = Scanner(System.`in`)
        var stop = false
        while (!stop) {
            val s = scanner.nextLine()
            if (s == "stop") {
                println("stopping")
                stop = true
            } else {
                s.forEach { inputQueue.put(it.toInt()) }
                inputQueue.put('\n'.toInt())
            }
        }
        pool.shutdownNow()
        pool.awaitTermination(5, TimeUnit.SECONDS)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        solve1(puzzleInput)
    }
}