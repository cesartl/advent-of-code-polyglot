package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.*
import kotlin.math.absoluteValue

object Day23 {

    data class Room(
        val type: Char, val depth: Int, val size: Int, val corrupted: Boolean, val stack: LinkedList<Char>
    ) {

        val heuristic: Int by lazy {
            if (isComplete()) {
                0
            } else {
                stack.foldLeftIndexed(0) { i, acc, c ->
                    if (c != type) {
                        acc + ((typeCoordinates[c]!! - typeCoordinates[type]!!).absoluteValue + depth - i) * points[type]!!
                    } else {
                        acc
                    }
                }
            }
        }


        fun isEmpty(): Boolean = size == 0

        fun isComplete(): Boolean = !corrupted && size == depth

        fun first(): Char = stack.first()

        fun charAt(i: Int): Char? {
            return if (i < size) {
                stack.drop(i).first()
            } else {
                null
            }
        }

        fun currentY(): Int = depth - size + 1

        fun pop(): Room {
            if (isEmpty()) {
                error("Cannot pop empty room")
            }
            val newStack = stack.drop(1)
            val newCorrupted = newStack.any { it != type }
            return this.copy(corrupted = newCorrupted, stack = newStack, size = size - 1)
        }

        fun add(char: Char, safe: Boolean = true): Room {
            if (safe && char != type) {
                error("Cannot add $char to $type room")
            }
            val newStack = stack.add(char)
            return this.copy(stack = newStack, size = size + 1)
        }

        override fun toString(): String {
            return stack.toString().padEnd(depth, '.')
        }

    }

    data class State(
        val corridor: Array<Char>,
        val rooms: Array<Room>,
        val lastEnergy: Int = 0,
        val intoRoom: Boolean = false
    ) {

        val isDone: Boolean by lazy {
            rooms.all { it.isComplete() }
        }

        fun print2() {
            repeat(corridor.count()) { print('#') }
            println()
            corridor.forEach { print(it) }
            println()
            repeat(corridor.count()) { x ->
                when (x) {
                    2 -> {
                        print(rooms[0].charAt(1) ?: '.')
                    }
                    4 -> {
                        print(rooms[1].charAt(1) ?: '.')
                    }
                    6 -> {
                        print(rooms[2].charAt(1) ?: '.')
                    }
                    8 -> {
                        print(rooms[3].charAt(1) ?: '.')
                    }
                    else -> {
                        print('#')
                    }
                }
            }
            println()
            repeat(corridor.count()) { x ->
                when (x) {
                    2 -> {
                        print(rooms[0].charAt(0) ?: '.')
                    }
                    4 -> {
                        print(rooms[1].charAt(0) ?: '.')
                    }
                    6 -> {
                        print(rooms[2].charAt(0) ?: '.')
                    }
                    8 -> {
                        print(rooms[3].charAt(0) ?: '.')
                    }
                    else -> {
                        print('#')
                    }
                }
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (!corridor.contentEquals(other.corridor)) return false
            if (!rooms.contentEquals(other.rooms)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = corridor.contentHashCode()
            result = 31 * result + rooms.contentHashCode()
            return result
        }

        companion object {
            fun build(lines: List<List<Char>>, depth: Int = 2): State {
                val corridor = Array(11) { '.' }
                val rooms = Array(4) {
                    Room(
                        type = idxToChar[it]!!,
                        depth = depth,
                        size = 0,
                        corrupted = true,
                        stack = Nil
                    )
                }
                lines.reversed().forEach { line ->
                    line.forEachIndexed { x, type ->
                        rooms[x] = rooms[x].add(type, false)
                    }
                }
                return State(corridor, rooms)
            }

            private val idxToChar = mapOf(0 to 'A', 1 to 'B', 2 to 'C', 3 to 'D')

            fun parse(input: Sequence<String>): State {
                val lines = input.drop(2).map { line -> line.filter { it.isLetter() }.toList() }.take(2).toList()
                return build(lines, 2)
            }

            fun parse2(input: Sequence<String>): State {
                val lines = input.drop(2).map { line -> line.filter { it.isLetter() }.toList() }.take(2).toList()
                val newLines = listOf(
                    lines[0], listOf('D', 'C', 'B', 'A'), listOf('D', 'B', 'A', 'C'), lines[1]
                )
                return build(newLines, 4)
            }

        }
    }

    private val typeCoordinates = mapOf('A' to 2, 'B' to 4, 'C' to 6, 'D' to 8)
    private val points = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)

    fun State.nextStates(): Sequence<State> {
        val next = sequence {
            rooms.forEachIndexed { i, room ->
                if (!room.isEmpty()) {
                    val type = room.first()
                    if (!room.isComplete() && room.corrupted) {
                        val tolerance = 6
                        val roomX = 2 * i + 2
                        var maxX = roomX + 1
                        while (maxX < 11 && corridor[maxX] == '.') {
                            maxX++
                        }
                        maxX--
                        maxX = maxX.coerceAtMost(roomX + tolerance)
                        var minX = roomX - 1
                        while (minX >= 0 && corridor[minX] == '.') {
                            minX--
                        }
                        minX++
                        minX = minX.coerceAtLeast(roomX - tolerance)
                        val targetRoomX = typeCoordinates[type]!!
                        val targetRoomIdx = (targetRoomX - 2) / 2
                        val targetRoom = rooms[targetRoomIdx]
                        val range = if (targetRoomX < roomX) {
                            (targetRoomX until roomX)
                        } else {
                            roomX + 1..targetRoomX
                        }
                        val pathClear = range.all { corridor[it] == '.' }
                        if (!targetRoom.corrupted && targetRoomIdx in (minX..maxX) && pathClear) {
                            //go into the target room
                            val newRooms = rooms.copyOf()
                            newRooms[i] = room.pop()
                            newRooms[targetRoomIdx] = targetRoom.add(room.first())
                            val ySteps = room.currentY() + targetRoom.currentY() - 1
                            val xSteps = (roomX - targetRoomX).absoluteValue
                            val energy = (xSteps + ySteps) * points[type]!!
                            yield(State(corridor = corridor, rooms = newRooms, lastEnergy = energy, intoRoom = true))
                        } else {
                            //go into the corridor
                            (minX..maxX).forEach { x ->
                                if (x < 2 || x > 8 || x % 2 == 1) {
                                    val newCorridor = corridor.copyOf()
                                    newCorridor[x] = type
                                    val newRooms = rooms.copyOf()
                                    newRooms[i] = room.pop()
                                    val ySteps = room.currentY()
                                    val xSteps = (x - roomX).absoluteValue
                                    val energy = (xSteps + ySteps) * points[type]!!
                                    yield(
                                        State(
                                            corridor = newCorridor,
                                            rooms = newRooms,
                                            lastEnergy = energy,
                                            intoRoom = false
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            corridor.forEachIndexed { x, type ->
                if (type != '.') {
                    val targetRoomX = typeCoordinates[type]!!
                    val targetRoomIdx = (targetRoomX - 2) / 2
                    val targetRoom = rooms[targetRoomIdx]
                    if (!targetRoom.corrupted) {
                        val range = if (targetRoomX < x) {
                            (targetRoomX until x)
                        } else {
                            x + 1..targetRoomX
                        }
                        //if corridor is clear
                        if (range.all { corridor[it] == '.' }) {
                            val newRooms = rooms.copyOf()
                            newRooms[targetRoomIdx] = targetRoom.add(type)
                            val newCorridor = corridor.copyOf()
                            newCorridor[x] = '.'
                            val ySteps = targetRoom.currentY() - 1
                            val xSteps = (x - targetRoomX).absoluteValue
                            val energy = (xSteps + ySteps) * points[type]!!
                            yield(State(corridor = newCorridor, rooms = newRooms, lastEnergy = energy, intoRoom = true))
                        }
                    }
                }
            }
        }
        return next.find { it.intoRoom }?.let { sequenceOf(it) } ?: next
    }


    fun State.heuristic(): Int {
        return rooms.sumOf { it.heuristic }
    }

    fun bigPathingBis(start: State): PathingResultInt<State> {
        return Dijkstra.traverseIntPredicate(
            start = start,
            end = { it?.isDone ?: false },
            nodeGenerator = { it.nextStates() },
            distance = { _, to -> to.lastEnergy },
            constraints = listOf(),
            heuristic = { it.heuristic() }
        )
    }


    fun solve1(input: Sequence<String>): Int {
        val state = State.parse(input)
        val result = timedMs { bigPathingBis(state) }
        println(result.first)
        println("${result.second.steps.size} states")
        val found = result.second.steps.asSequence().find { it.key.isDone }!!
        return found?.value ?: -1
    }

    fun solve2(input: Sequence<String>): Int {
        val state = State.parse2(input)
        val result = timedMs { bigPathingBis(state) }
        println(result.first)
        println("${result.second.steps.size} states")
        val found = result.second.steps.asSequence().find { it.key.isDone }!!
        return found?.value ?: -1
    }
}