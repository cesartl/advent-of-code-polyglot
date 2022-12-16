package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Queue
import com.ctl.aoc.kotlin.utils.traversal


private val regex = """Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? ([\w ,]+)""".toRegex()
private typealias Valve = String

object Day16 {

    data class Spec(
        val from: Valve,
        val flowRate: Int,
        val to: List<Valve>
    )

    private fun String.toSpec(): Spec {
        return regex.matchEntire(this)
            ?.let { it.groupValues }
            ?.let { values ->
                val from = values[1]
                val flowRate = values[2].toInt()
                val to = values[3].split(",").map { it.trim() }
                Spec(from, flowRate, to)
            }
            ?: error(this)
    }

    data class State(
        val specs: Map<Valve, Spec>,
        val current: Valve,
        val clock: Int,
        val totalPressureRelease: Int,
        val openValves: Set<Valve>
    ) {
        fun next(): Sequence<State> {
            val spec = specs[current]!!
            return sequence {
                val currentSpec = specs[current]!!
                if (!openValves.contains(current) && currentSpec.flowRate > 0) {
                    val newClock = clock - 1
                    if (newClock >= 0) {
                        yield(
                            copy(
                                clock = newClock,
                                totalPressureRelease = totalPressureRelease + newClock * currentSpec.flowRate,
                                openValves = openValves + current
                            )
                        )
                    }
                }
                spec.to.forEach { next ->
                    val newClock = clock - 1
                    if (newClock >= 0) {
                        yield(
                            copy(
                                current = next,
                                clock = newClock,
                                totalPressureRelease = totalPressureRelease,
                                openValves = openValves
                            )
                        )
                    }
                }
            }
        }
    }

    data class State2(
        val specs: Map<Valve, Spec>,
        val me: Valve,
        val elephant: Valve,
        val clock: Int,
        val totalPressureRelease: Int,
        val openValves: Set<Valve>,
        val usefulValves: Int = specs.values.count { it.flowRate > 0 }
    ) {
        fun next(): Sequence<State2> {
            if (openValves.size == usefulValves) {
                return sequenceOf(copy(clock = 0))
            }
            if(clock ==0){
                return sequenceOf()
            }
            val meState = State(specs, me, clock, totalPressureRelease, openValves)
            val elephantState = State(specs, elephant, clock, totalPressureRelease, openValves)
            val elephantNextStates = elephantState.next().toList()
            return sequence {
                meState.next().forEach { meNext ->
                    elephantNextStates.forEach { elephantNext ->
                        if (meNext.current != elephantNext.current) {
                            yield(merge(meNext, elephantNext))
                        }
                    }
                }
            }
        }

        fun merge(meNext: State, elephantNext: State): State2 {
            val meRelease = meNext.totalPressureRelease - totalPressureRelease
            val elephantRelease = elephantNext.totalPressureRelease - totalPressureRelease
            return copy(
                me = meNext.current,
                elephant = elephantNext.current,
                totalPressureRelease = totalPressureRelease + meRelease + elephantRelease,
                openValves = meNext.openValves + elephantNext.openValves,
                clock = clock - 1
            )
        }
    }

    fun buildState(input: Sequence<String>): State {
        val specs = input.map { it.toSpec() }.associateBy { it.from }
        return State(
            specs = specs,
            current = "AA",
            clock = 30,
            totalPressureRelease = 0,
            openValves = setOf()
        )
    }

    fun solve1(input: Sequence<String>): Int {
        val state = buildState(input)
        val states = traversal(
            startNode = state,
            storage = Queue(),
            index = { "${it.current}-${it.totalPressureRelease}-${it.clock}" },
            nodeGenerator = { it.next() }
        )
        val end = states
            .filter { it.clock == 0 }
            .sortedByDescending { it.totalPressureRelease }
            .first()
        return end.totalPressureRelease
    }

    fun solve2(input: Sequence<String>): Int {
        val state = buildState(input).let {
            State2(
                specs = it.specs,
                me = "AA",
                elephant = "AA",
                clock = 26,
                totalPressureRelease = 0,
                openValves = setOf(),
            )
        }
        val states = traversal(
            startNode = state,
            storage = Queue(),
            index = { "${it.me}-${it.elephant}-${it.totalPressureRelease}" },
            nodeGenerator = { it.next() }
        )
        val end = states
            .filter { it.clock == 0 }
            .sortedByDescending { it.totalPressureRelease }
            .first()
        return end.totalPressureRelease
    }
}
