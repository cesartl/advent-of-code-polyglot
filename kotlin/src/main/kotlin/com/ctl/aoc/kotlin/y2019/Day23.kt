package com.ctl.aoc.kotlin.y2019

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Day23 {

    fun solve1(input: String): Long {
        val intCode = input.split(",").map { it.toLong() }.toLongArray()
        val n = 50
        val networkInterface = NetworkInterface(n)
        runNetwork(networkInterface, n, intCode)
        return networkInterface.packetBuffer(255).removeFirst().y
    }

    fun solve2(input: String): Long {
        val intCode = input.split(",").map { it.toLong() }.toLongArray()
        val n = 50
        val networkInterface = NetworkInterface(n, true)
        runNetwork(networkInterface, n, intCode)
        return networkInterface.lastZeroY ?: -1L
    }

    private fun runNetwork(networkInterface: NetworkInterface, n: Int, intCode: LongArray) {
        networkInterface.init(n)
        val threadPool = Executors.newFixedThreadPool(n)
        (0 until n).forEach { address ->
            val prgm = Day9.IntCodeState(intCode = intCode.copyOf(9999),
                    output = networkInterface.output(address),
                    input = networkInterface.input(address))
            threadPool.submit {
                Day9.run {
                    prgm.execute()
                }
            }
        }
        threadPool.shutdown()
        threadPool.awaitTermination(5, TimeUnit.SECONDS)
    }

    data class Packet(val origin: Int, val x: Long, val y: Long)

    class NetworkInterface(val size: Int, val natLogic: Boolean = false) {
        private val inputBuffers = mutableMapOf<Int, Deque<Long>>()
        private val outputBuffers = mutableMapOf<Int, Deque<Long>>()
        private val packetBuffers = mutableMapOf<Int, Deque<Packet>>()
        private val lock = Object()
        private val inactiveComputers : MutableSet<Int> = ConcurrentHashMap.newKeySet()
        var lastZeroY: Long? = null

        private fun inputBuffer(address: Int): Deque<Long> = inputBuffers.computeIfAbsent(address) { ArrayDeque() }
        private fun outputBuffer(address: Int): Deque<Long> = outputBuffers.computeIfAbsent(address) { ArrayDeque() }
        fun packetBuffer(address: Int): Deque<Packet> = packetBuffers.computeIfAbsent(address) { ArrayDeque() }

        fun init(n: Int) {
            (0 until n).forEach { i ->
                inputBuffer(i).addFirst(i.toLong())
            }
        }

        private fun checkNat() {
            synchronized(lock) {
                if (inactiveComputers.size == size && packetBuffer(255).isNotEmpty()) {
                    inactiveComputers.clear()
                    println("£££")
                    packetBuffer(0).addFirst(packetBuffer(255).peekFirst().copy(origin = 255))
                }
            }
        }

        fun input(address: Int): () -> Long = {
            val inputBuffer = inputBuffer(address)
            if (inputBuffer.isEmpty()) {
                receivePacket(address)
            }
            if (inputBuffer.isEmpty()) {
                inactiveComputers.add(address)
                if (natLogic) {
                    checkNat()
                }
                -1L
            } else {
                inactiveComputers.remove(address)
                inputBuffer.removeLast()
            }
        }

        public fun output(address: Int): (Long) -> Unit = { v ->
            val outputBuffer = outputBuffer(address)
            outputBuffer.addFirst(v)
            if (outputBuffer.size == 3) {
                sendPacket(address)
            }
        }

        private fun receivePacket(address: Int) {
            synchronized(lock) {
                val packetBuffer = packetBuffer(address)
                if (packetBuffer.isNotEmpty()) {
                    val packet = packetBuffer.removeLast()
                    println("$address receives $packet")
                    if (address == 0) {
                        if (packet.y == lastZeroY) {
                            println("******** $lastZeroY")
                        }
                        lastZeroY = packet.y
                    }
                    inputBuffer(address).addFirst(packet.x)
                    inputBuffer(address).addFirst(packet.y)
                }
            }
        }

        private fun sendPacket(address: Int) {
            synchronized(lock) {
                val outputBuffer = outputBuffer(address)
                val target = outputBuffer.removeLast().toInt()
                val x = outputBuffer.removeLast()
                val y = outputBuffer.removeLast()
                val packet = Packet(address, x, y)
                println("$address sends $packet to $target")
                packetBuffer(target).addFirst(packet)
            }
        }
    }
}