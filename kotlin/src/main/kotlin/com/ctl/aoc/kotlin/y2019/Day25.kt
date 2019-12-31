package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.Lists
import java.io.File
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors.newFixedThreadPool
import java.util.concurrent.TimeUnit

object Day25 {
    val puzzleInput = InputUtils.getString(2019, 25).split(',').map { it.toLong() }
            .toLongArray()

    val dump = InputUtils.getString("2019", "day25-checkpoint.txt").split(',').map { it.toLong() }
            .toLongArray()

    fun findPassword(intCode: LongArray) {
        //Pressure-Sensitive Floor
        val directions = mutableListOf(
                "north", "north", "west", "west", "south", "east", "north"
        )

        /*
            - astronaut ice cream
            - wreath
            - coin
            - dehydrated water
            - asterisk
            - monolith
            - astrolabe
            - mutex
         */
        val objects = listOf(
                "astronaut ice cream",
                "wreath",
                "coin",
                "dehydrated water",
                "asterisk",
                "monolith",
                "astrolabe",
                "mutex"
        )
        val allOptions = Lists.powerSet(objects)
//                .filter { (4..8).contains(it.size) }
                .filter { it.size > 1 }
                .asSequence()
                .map { selected ->
                    println("Doing $selected")
                    val inputQueue = ArrayBlockingQueue<Int>(10000)
                    val memory = intCode.copyOf(9999)
                    directions.joinToString(separator = "\n").forEach {
                        inputQueue.put(it.toInt())
                    }
                    inputQueue.put('\n'.toInt())
                    objects.joinToString(separator = "\n") { "drop $it" }
                            .forEach { inputQueue.put(it.toInt()) }
                    inputQueue.put('\n'.toInt())
                    selected.joinToString(separator = "\n") { "take $it" }
                            .forEach { inputQueue.put(it.toInt()) }
                    inputQueue.put('\n'.toInt())
                    val builder = StringBuilder()
                    val intCodeState = Day9.IntCodeState(intCode = memory,
                            output = {
                                //                                print(it.toInt().toChar())
                                builder.append(it.toInt().toChar())
                            }, input = {
                        val i = inputQueue.take().toLong()
//                        print(i.toInt().toChar())
                        i
                    })

                    val finalCommands = listOf("inv", "north")
                    finalCommands.joinToString(separator = "\n").forEach {
                        inputQueue.put(it.toInt())
                    }
                    inputQueue.put('\n'.toInt())
                    println("Submitting to pool")
                    val pool = newFixedThreadPool(1)
                    pool.submit {
                        Day9.run {
                            println("starting")
                            intCodeState.execute()
                            println("finished")
                        }
                    }
                    Thread.sleep(100)
                    pool.shutdownNow()
                    pool.awaitTermination(5, TimeUnit.SECONDS)
                    val output = builder.toString()
                    if (!output.contains("Pressure-Sensitive Floor")) {
                        throw IllegalArgumentException(output.takeLast(100))
                    }
                    output
                }

        println(allOptions.first { !it.contains("ejected") })
    }

    fun manual(intCode: LongArray) {

        val inputQueue = ArrayBlockingQueue<Int>(100)

        val memory = intCode.copyOf(9999)
        val intCodeState = Day9.IntCodeState(intCode = memory,
                output = {
                    print(it.toInt().toChar())
                }, input = {
            inputQueue.take().toLong()
        })

        val pool = newFixedThreadPool(1)

        val directions = mutableListOf(
                "north", "north", "west", "west", "south", "east", "north", "north"
        )

        directions.joinToString(separator = "\n").forEach {
            inputQueue.put(it.toInt())
        }
        pool.submit {
            Day9.run {
                intCodeState.execute()
                println("finished")
            }
        }

        val scanner = Scanner(System.`in`)
        var stop = false
        while (!stop) {
            val s = scanner.nextLine()
            if (s == "stop") {
                println("stopping")
                println(intCodeState.index)
                println(intCodeState.relativeBase)
                inputQueue.put(-1)
                stop = true
            } else if (s == "dump") {
                val file = File("day25-checkpoint.txt")
                println("Dumping state to ${file.absolutePath}")
                file.writeText(memory.joinToString(separator = ","))
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
        findPassword(dump)
    }
}