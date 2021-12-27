package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.*
import kotlin.math.absoluteValue

object Day23 {

    enum class AmphiType {
        A, B, C, D;
    }

    fun AmphiType.x(): Int {
        return 2 + this.ordinal * 2
    }

    private val typePoints = AmphiType.values().associateWith { "1".padEnd(it.ordinal + 1, '0').toInt() }

    data class Env(
        val pods: Map<Position, AmphiType>, val maxY: Int = 2
    ) {

        val isDone: Boolean by lazy {
            pods.all { (pod, type) -> pod.y > 0 && pod.x == type.x() }
        }

        fun print() {
            (-1..2).forEach { y ->
                (-1..11).forEach { x ->
                    val p = Position(x, y)
                    val pod = pods[p]
                    if (pod != null) {
                        print(pod)
                    } else {
                        if (y == 0 && x in (0..10)) {
                            print('.')
                        } else if (y > 0 && (x == 2 || x == 4 || x == 6 || x == 8)) {
                            print('.')
                        } else {
                            print('#')
                        }
                    }
                }
                println()
            }
        }

        fun goIntoRoom(x: Int, type: AmphiType): Int? {
            val wrongPod = (1..maxY).any { y -> pods[Position(x, y)]?.let { pod -> pod != type } ?: false }
            if (wrongPod) {
                return null
            }
            var current = Position(x, 1)
            if (pods[current] != null) {
                return null
            }
            while (pods[current] == null) {
                if (current.y == maxY) {
                    return maxY
                }
                current = current.copy(y = current.y + 1)
            }
            return current.y - 1
        }

        fun podMoves(p: Position, type: AmphiType): Sequence<Position> {
            return sequence {
                if (p.y > 0) {
                    //inside a room
                    val up = Position(p.x, p.y - 1)
                    if (p.y == 1 || pods[up] == null) {
                        //can exit room
                        val tolerance = 3
                        //corridor to the right
                        var current = Position(p.x + 1, 0)
                        var countRight = 0
                        val maxCountRight = if (type.x() < p.x) tolerance else 10
                        while (pods[current] == null && current.x <= 10 && countRight < maxCountRight) {
                            //we are above the target room
                            if (current.x == type.x()) {
                                goIntoRoom(current.x, type)?.let { y ->
                                    yield(current.copy(y = y))
                                }
                            }
                            //go in corridor
                            if (current.x != 2 && current.x != 4 && current.x != 6 && current.x != 8) {
                                yield(current)
                            }

                            current = current.copy(x = current.x + 1)
                            countRight++
                        }
                        //corridor to the left
                        current = Position(p.x - 1, 0)
                        var countLeft = 0
                        val maxCountLeft = if (type.x() > p.x) tolerance else 10
                        while (pods[current] == null && current.x >= 0 && countLeft < maxCountLeft) {
                            //we are above the target room
                            if (current.x == type.x()) {
                                goIntoRoom(current.x, type)?.let { y ->
                                    yield(current.copy(y = y))
                                }
                            }

                            //go into corridor
                            if (current.x != 2 && current.x != 4 && current.x != 6 && current.x != 8) {
                                yield(current)
                            }

                            current = current.copy(x = current.x - 1)
                            countLeft++
                        }
                    }
                } else {
                    //in a corridor
                    val deltaX = type.x() - p.x
                    val range = if (deltaX > 0) (p.x + 1..type.x()) else (type.x() until p.x)
                    val pathClear = range.all { pods[Position(it, 0)] == null }
                    if (pathClear) {
                        goIntoRoom(type.x(), type)?.let { y ->
                            yield(Position(type.x(), y))
                        }
                    }
                }
            }
        }

        fun allMoves(): Sequence<Env> {
            val candidates = pods.asSequence().filterNot { (p, type) -> isAtRightPlace(p, type) }.flatMap { (p, type) ->
                podMoves(p, type).map { p to it }
            }

            return (candidates.find { it.second.y > 0 }?.let { best -> sequenceOf(best) } ?: candidates).map {
                movePod(
                    it.first,
                    it.second
                )
            }
        }

        fun isAtRightPlace(p: Position, type: AmphiType): Boolean {
            if (p.x != type.x()) {
                return false
            }
            if (p.y == maxY) {
                return true
            }
            return pods[Position(p.x, p.y + 1)]?.let { below ->
                below == type
            } ?: false
        }

        private fun movePod(from: Position, to: Position): Env {
            val mutablePods = pods.toMutableMap()
            val type = mutablePods.remove(from)!!
            mutablePods[to] = type
            return copy(pods = mutablePods)
        }


        override fun toString(): String {
            return "Env(amphipods=$pods, isDone=$isDone)"
        }

        companion object {
            fun build(lines: List<List<Char>>, depth: Int = 2): Env {
                val podLines = lines.map { line -> line.map { AmphiType.valueOf(it.toString()) } }
                val xStart = 2
                val yStart = 1
                val pods = podLines.flatMapIndexed { y: Int, pods: List<AmphiType> ->
                    pods.mapIndexed { x, pod -> Position(2 * x + xStart, y + yStart) to pod }
                }.toMap()
                return Env(pods, depth)
            }

            fun parse(input: Sequence<String>): Env {
                val lines = input.drop(2).map { line -> line.filter { it.isLetter() }.toList() }.take(2).toList()
                return build(lines, 2)
            }

            fun parse2(input: Sequence<String>): Env {
                val lines = input.drop(2).map { line -> line.filter { it.isLetter() }.toList() }.take(2).toList()
                val newLines = listOf(
                    lines[0], listOf('D', 'C', 'B', 'A'), listOf('D', 'B', 'A', 'C'), lines[1]
                )
                return build(newLines, 4)
            }
        }
    }

    fun distance(from: Env, to: Env): Int {
        val start = (from.pods.keys - to.pods.keys).first()
        val end = (to.pods.keys - from.pods.keys).first()
        val type = from.pods[start]!!
        assert(to.pods[end] == type)
        val ySteps = start.y + end.y
        val steps = (start.x - end.x).absoluteValue + ySteps
        return steps * typePoints[type]!!
    }

    fun Env.heuristic(): Int {
        return pods.asSequence().filter { e -> e.key.y > 0 }.sumOf { (pod, type) ->
            val xDistance = (type.x() - pod.x).absoluteValue
            xDistance * typePoints[type]!!
        }
    }

    fun bigPathing(start: Env): PathingResultInt<Env> {
        val constraint = CustomConstraintInt<Env> { current, _ -> !current.isDone }
        return Dijkstra.traverseInt(start,
            null,
            { it.allMoves() },
            { from, to -> distance(from, to) },
            listOf(constraint),
            heuristic = { it.heuristic() })
    }

    data class Room(
        val type: Char, val depth: Int, val current: Int, val corrupted: Boolean, val stack: Array<Char>
    ) {

        val heuristic: Int by lazy {
            if (isComplete()) {
                0
            } else {
                stack.slice(0..current).foldIndexed(0) { i, acc, c ->
                    if (c != type) {
                        acc + ((typeCoordinates[c]!! - typeCoordinates[type]!!).absoluteValue + depth - i) * points[type]!!
                    } else {
                        acc
                    }
                }
            }
        }


        fun isEmpty(): Boolean = current < 0

        fun isComplete(): Boolean = !corrupted && current == depth - 1

        fun first(): Char = stack[current]

        fun charAt(i: Int): Char? {
            return if (i <= current) {
                stack[i]
            } else {
                null
            }
        }

        fun firstSafe(): Char? {
            if (current == -1) {
                return null
            }
            return stack[current]
        }

        fun currentY(): Int = depth - current


        fun pop(): Room {
            if (current == -1) {
                error("Cannot pop empty room")
            }
            val newCurrent = current - 1
            val newCorrupted = (0..newCurrent).any { stack[it] != type }
            return this.copy(current = newCurrent, corrupted = newCorrupted)
        }

        fun popSafe(): Room? {
            if (current == -1) {
                return null
            }
            return pop()
        }

        fun add(char: Char, safe: Boolean = true): Room {
            if (safe && char != type) {
                error("Cannot add $char to $type room")
            }
            val newStack = stack.copyOf()
            val newCurrent = current + 1
            newStack[newCurrent] = char
            return this.copy(stack = newStack, current = newCurrent)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Room

            if (type != other.type) return false
            if (depth != other.depth) return false
            if (current != other.current) return false
            if (corrupted != other.corrupted) return false
            if (!stack.contentEquals(other.stack)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = type.hashCode()
            result = 31 * result + depth
            result = 31 * result + current
            result = 31 * result + corrupted.hashCode()
            result = 31 * result + stack.contentHashCode()
            return result
        }

        override fun toString(): String {
            return stack.slice(0..current).joinToString(separator = "|").padEnd(depth, '.')
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
                    Room(type = idxToChar[it]!!,
                        depth = depth,
                        current = -1,
                        corrupted = true,
                        stack = Array(depth) { '.' })
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
        val env = Env.parse(input)
        env.print()
        val result = timedMs { bigPathing(env) }
        println(result.first)
        println("${result.second.steps.size} states")
        return result.second.steps.asSequence().find { it.key.isDone }?.value ?: -1
    }

    fun solve1Bis(input: Sequence<String>): Int {
        val state = State.parse(input)
        val result = timedMs { bigPathingBis(state) }
        println(result.first)
        println("${result.second.steps.size} states")
        val found = result.second.steps.asSequence().find { it.key.isDone }!!
        return found?.value ?: -1
    }

    fun solve2(input: Sequence<String>): Int {
        val env = Env.parse2(input)
        val result = timedMs { bigPathing(env)}
        println("${result.second.steps.size} states")
        return result.second.steps.asSequence().find { it.key.isDone }?.value ?: -1
    }

    fun solve2Bis(input: Sequence<String>): Int {
        val state = State.parse2(input)
        val result = timedMs { bigPathingBis(state) }
        println(result.first)
        println("${result.second.steps.size} states")
        val found = result.second.steps.asSequence().find { it.key.isDone }!!
        return found?.value ?: -1
    }
}