package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.*


private val regex = """Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? ([\w ,]+)""".toRegex()
private typealias Valve = String

object Day16 {

    data class ValveSpec(
        val from: Valve,
        val flowRate: Int,
        val to: List<Valve>
    )

    private fun String.toSpec(): ValveSpec {
        return regex.matchEntire(this)
            ?.groupValues
            ?.let { values ->
                val from = values[1]
                val flowRate = values[2].toInt()
                val to = values[3].split(",").map { it.trim() }
                ValveSpec(from, flowRate, to)
            }
            ?: error(this)
    }

    /**
     * Approach 1: BFS with every state 1s apart
     */
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

    /**
     * Approach 2: BFS with every state 1s apart using State2 containing elephant and elf.
     * Using part1 for each and then merging
     */
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

    /**
     * State for part1
     */
    data class State(
        val specs: Map<Valve, ValveSpec>,
        val current: Valve,
        val clock: Int,
        val totalPressureRelease: Int,
        val openValves: Set<Valve>
    ) {
        /**
         * We model moving an opening as 2 separate states
         */
        fun next(): Sequence<State> {
            val spec = specs[current]!!
            return sequence {
                val currentSpec = specs[current]!!
                //if the current valve can be open we open it
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
                //move to all adjacent nodes
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
        val specs: Map<Valve, ValveSpec>,
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
            if (clock == 0) {
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

    private fun buildState(input: Sequence<String>): State {
        val specs = input.map { it.toSpec() }.associateBy { it.from }
        return State(
            specs = specs,
            current = "AA",
            clock = 30,
            totalPressureRelease = 0,
            openValves = setOf()
        )
    }

    //-- Second approach, rather than modelling each move we build a distance map for between all nodes.
    // After that we only move from useful valve to useful valve

    fun solve1Bis(input: Sequence<String>): Int {
        val start = buildStartNode(input, 26)
        val best = traversal(
            startNode = start,
            storage = Queue(),
            index = { node -> node.path.joinToString(separator = ",") { it.from } },
            nodeGenerator = { it.next() }
        )
            .maxBy { it.totalPressureRelease }

        println(best)
        return best.totalPressureRelease
    }

    fun solve2Bis(input: Sequence<String>): Int {
        val start = buildStartNode(input, 26)
        val all = traversal(
            startNode = start,
            storage = Queue(),
            index = { node -> node.path.joinToString(separator = ",") { it.from } },
            nodeGenerator = { it.next() }
        )
            .filter { it.totalPressureRelease > 100 }
            .groupBy { it.effectivePathSet }
            .mapValues { listEntry -> listEntry.value.maxBy { it.totalPressureRelease } }
            .values
            .toList()
        return sequence {
            all.forEach { human ->
                all.forEach { elephant ->
                    if (human.effectivePathSet.intersect(elephant.effectivePathSet).isEmpty()) {
                        yield(human.totalPressureRelease + elephant.totalPressureRelease)
                    }
                }
            }
        }.max()
    }

    data class SearchNode(
        val clock: Int,
        val path: List<ValveSpec>,
        val totalPressureRelease: Int,
        val pathingMap: Map<ValveSpec, PathingResult<ValveSpec>>
    ) {

        val pathSet: Set<ValveSpec> by lazy {
            path.toSet()
        }

        val effectivePathSet: Set<ValveSpec> by lazy {
            pathSet.filter { it.from != "AA" }.toSet()
        }

        val last: ValveSpec by lazy {
            path.last()
        }

        private val pathingResult: PathingResult<ValveSpec> by lazy {
            pathingMap[last] ?: error("No pathing from $last")
        }


        fun next(): Sequence<SearchNode> = sequence {
            (pathingMap.keys - pathSet).forEach { to ->
                val travelTime = pathingResult.steps[to] ?: error("No path from $last to $to")
                val newClock = clock - travelTime.toInt() - 1
                if (newClock >= 0) {
                    yield(
                        copy(
                            clock = newClock,
                            totalPressureRelease = totalPressureRelease + to.flowRate * newClock,
                            path = path + listOf(to)
                        )
                    )
                }
            }
        }

        override fun toString(): String {
            return "SearchNode(clock=$clock, path=${
                path.joinToString(separator = ",") { it.from }
            }, totalPressureRelease = $totalPressureRelease)"
        }
    }

    private fun buildStartNode(input: Sequence<String>, startClock: Int): SearchNode {
        val specs = input.map { it.toSpec() }
        val valveToSpec = specs.associateBy { it.from }
        val pathingMap: Map<ValveSpec, PathingResult<ValveSpec>> = buildPathingMap(specs, valveToSpec)

        return SearchNode(
            clock = 26,
            path = listOf(valveToSpec["AA"]!!),
            totalPressureRelease = 0,
            pathingMap = pathingMap
        )
    }


    private fun buildPathingMap(
        specs: Sequence<ValveSpec>,
        valveToSpec: Map<Valve, ValveSpec>
    ): Map<ValveSpec, PathingResult<ValveSpec>> {
        val graph = Graph<ValveSpec>()
        specs.forEach { spec ->
            spec.to.forEach { to ->
                graph.addEdge(spec, valveToSpec[to]!!)
            }
        }
        return specs
            .filter { it.from == "AA" || it.flowRate > 0 }
            .associateWith { graph.dijkstra(it, null) }
    }

}
