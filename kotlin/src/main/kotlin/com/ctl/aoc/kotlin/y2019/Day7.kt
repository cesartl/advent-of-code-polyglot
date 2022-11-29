package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.Lists
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.List
import kotlin.collections.copyOf
import kotlin.collections.first
import kotlin.collections.fold
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.indices
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toList

object Day7 {


    fun solve1(intCode: IntArray): Int {
        return Lists.permutations(listOf(0, 1, 2, 3, 4))
            .map { phases -> runWithPhases(intCode, phases) }
            .maxOrNull() ?: 0
    }

    fun solve2(intCode: IntArray): Int {
        return Lists.permutations((5..9).toList())
            .map { phases -> runWithFeedbackLoop(intCode, phases) }
            .maxOrNull() ?: 0
    }

    fun solve2Channel(intCode: LongArray): Long {
        return Lists.permutations((5..9).toList())
            .map { phases -> runBlocking { runWithFeedbackLoopChannel(intCode, phases) } }
            .maxOrNull() ?: 0
    }

    fun runWithPhases(intCode: IntArray, phases: List<Int>): Int {
        return phases.fold(0) { input, phase -> runWithPhase(intCode, phase, input) }
    }

    fun runWithPhase(intCode: IntArray, phase: Int, input: Int): Int {
        val output = mutableListOf<Int>()
        val inputs = LinkedList(listOf(phase, input))
        val state =
            Day5.IntCodeState(intCode = intCode.copyOf(), input = { inputs.remove() }, output = { output.add(it) })
        Day5.run {
            state.execute()
        }
        require(output.size == 1) { "Could not run phase $phase" }
        return output.first()
    }

    private fun amplifier(intCode: LongArray, input: ReceiveChannel<Long>, output: SendChannel<Long>): IntCodeVM {
        return IntCodeVM(intCode = intCode.copyOf(),
            input = { input.receive() },
            output = { output.send(it) })
    }

    suspend fun runWithFeedbackLoopChannel(intCode: LongArray, phases: List<Int>): Long {
        val channels = mutableMapOf<Int, Channel<Long>>()

        phases.forEachIndexed { index, phase ->
            val channel = Channel<Long>(2)
            channels[index] = channel
            channel.send(phase.toLong())
        }

        val amplifiers = List(phases.size) { index ->
            val input = channels[index] ?: error("Missing channel")
            val output = channels[index.next(phases.size)] ?: error("Missing channel")
            amplifier(intCode, input, output)
        }

        channels[0]!!.send(0)

        runBlocking {
            amplifiers.forEach {
                launch {
                    it.execute()
                }
            }
        }
        return channels[0]!!.receive()
    }

    fun runWithFeedbackLoop(intCode: IntArray, phases: List<Int>): Int {
        val inputOutputs = phases.map { InputOutput(it) }
        inputOutputs[0].writeOutput(0)
        val amps: List<Day5.IntCodeState> = (phases.indices).map { ampIdx ->
            Day5.IntCodeState(intCode = intCode.copyOf(),
                input = { inputOutputs[ampIdx].getInput() },
                output = { inputOutputs[(ampIdx + 1) % phases.size].writeOutput(it) })
        }

        val threadCounter = AtomicInteger()
        val threadFactory: ThreadFactory = ThreadFactory {
            Thread(it, "Amplifier-${threadCounter.getAndIncrement()}")
        }
        val threadPool = Executors.newFixedThreadPool(phases.size, threadFactory)

        amps.forEachIndexed { index, amp ->
            threadPool.execute {
                Day5.run {
                    amp.execute()
//                    println("amp $index has finished")
                }
            }
        }

        threadPool.shutdown()
        threadPool.awaitTermination(10, TimeUnit.SECONDS)
        return inputOutputs[0].getInput()
    }

    class InputOutput() {
        constructor(initialValue: Int) : this() {
            queue.put(initialValue)
        }

        private val queue: BlockingQueue<Int> = ArrayBlockingQueue(10)

        fun getInput(): Int {
            val input = queue.take()
//            println("Thread ${Thread.currentThread().name} obtaining value $input")
            return input
        }

        fun writeOutput(output: Int) {
//            println("Thread ${Thread.currentThread().name} producing value $output")
            queue.put(output)
        }
    }
}

private fun Int.next(m: Int): Int {
    return (this + 1) % m
}
