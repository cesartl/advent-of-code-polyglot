package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.*


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
            return spec.to
                .asSequence()
                .flatMap { next ->
                    sequence {
                        val isNextOpen = openValves.contains(next)
                        val nextSpec = specs[next] ?: error("no spec for $next")
                        val shouldOpen = nextSpec.flowRate > 0
                        val timeSpent = 1 + if (isNextOpen || !shouldOpen) 0 else 1
                        val newClock = clock - timeSpent
                        val pressureRelease = if (isNextOpen) {
                            0
                        } else {
                            newClock * nextSpec.flowRate
                        }
                        if (newClock >= 0) {
                            yield(
                                copy(
                                    current = next,
                                    clock = newClock,
                                    totalPressureRelease = totalPressureRelease,
                                    openValves = openValves
                                )
                            )
                            if (shouldOpen && !isNextOpen) {
                                yield(
                                    copy(
                                        current = next,
                                        clock = newClock,
                                        totalPressureRelease = totalPressureRelease + pressureRelease,
                                        openValves = openValves + next
                                    )
                                )
                            }
                        }
                    }
                }
                .sortedByDescending { it.current.compareTo(this.current) }
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
            val meSpec = specs[me]!!
            val elephantSpec = specs[elephant]!!
            TODO()
        }
    }

    fun State.cost(): Long {
        val remainingFlowRates = specs
            .asSequence()
            .filterNot { this.openValves.contains(it.key) }
            .map { it.value.flowRate }
            .sum()
            .toLong()
        return 30 * remainingFlowRates - totalPressureRelease + 10 * clock
    }

    fun State.path(): PathingResult<State> {
        return Dijkstra.traverse(
            start = this,
            end = null,
            nodeGenerator = { it.next() },
            distance = { from, to -> cost() },
            constraints = listOf(CustomConstraint { s, _ -> s.clock > 0 }),
            debug = false,
        )
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
//        val result = state.path()
//        result.findPath(result.lastNode!!).map {
//            "t=${it.clock} " + it.current + "(${it.totalPressureRelease}) - ${it.openValves.sorted()}"
//        }.forEach {
//            println(it)
//        }
//        return result.lastNode?.totalPressureRelease ?: 0
        val states = traversal(
            startNode = state,
            storage = Queue(),
            index = { "${it.current}-${it.totalPressureRelease}" },
            nodeGenerator = { it.next() }
        )
        val end = states
            .filter { it.clock == 0 }
            .sortedByDescending { it.totalPressureRelease }
            .first()
        return end.totalPressureRelease
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
