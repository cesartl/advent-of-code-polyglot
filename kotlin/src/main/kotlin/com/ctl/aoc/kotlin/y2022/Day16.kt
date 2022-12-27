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
        val start = buildStartNode(input, 30)
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
            .filter { it.pathSet.size >= 6 }
            .groupBy { it.effectivePathSet }
            .mapValues { listEntry -> listEntry.value.maxBy { it.totalPressureRelease } }
            .values
            .toList()
        val (pressure, pair) = sequence {
            all.forEach { human ->
                all.forEach { elephant ->
                    if (human.effectivePathSet.intersect(elephant.effectivePathSet).isEmpty()) {
                        yield(human.totalPressureRelease + elephant.totalPressureRelease to (human to elephant))
                    }
                }
            }
        }.maxBy { it.first }
        val (human, elephant) = pair
        println(elephant.effectivePathSet.map { it.from })
        println(human.effectivePathSet.map { it.from })
        return pressure
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
        val pathingMap: Map<ValveSpec, PathingResult<ValveSpec>> = buildPathingMap(valveToSpec)

        return SearchNode(
            clock = startClock,
            path = listOf(valveToSpec["AA"]!!),
            totalPressureRelease = 0,
            pathingMap = pathingMap
        )
    }


    private fun buildPathingMap(
        valveToSpec: Map<Valve, ValveSpec>
    ): Map<ValveSpec, PathingResult<ValveSpec>> {
        val graph = Graph<ValveSpec>()
        valveToSpec.values.forEach { spec ->
            spec.to.forEach { to ->
                graph.addEdge(spec, valveToSpec[to]!!)
            }
        }
        return valveToSpec.values
            .filter { it.from == "AA" || it.flowRate > 0 }
            .associateWith { graph.dijkstra(it, null) }
    }

    //--- DP approach

    data class MemoKey(
        val currentVale: Valve,
        val openValves: Set<Valve>,
        val clock: Int,
        val player: Int = 0
    )

    fun solve1Dp(input: Sequence<String>): Int {
        val specs: Map<Valve, ValveSpec> = input.map { it.toSpec() }.associateBy { it.from }
        val cache = mutableMapOf<MemoKey, Int>()
        val result = dp(specs, MemoKey("AA", setOf(), 30), cache)
        println("cache size: ${cache.size}")
        return result
    }

    fun solve2Dp(input: Sequence<String>): Int {
        val specs: Map<Valve, ValveSpec> = input.map { it.toSpec() }.associateBy { it.from }
        val cache = mutableMapOf<MemoKey, Int>()
        val result = dp(specs, MemoKey("AA", setOf(), 26, 1), cache)
        println("cache size: ${cache.size}")
        return result
    }

    private fun dp(
        valves: Map<Valve, ValveSpec>,
        memoKey: MemoKey,
        cache: MutableMap<MemoKey, Int>
    ): Int {
        val (currentVale, openValves, clock, player) = memoKey
        if (clock == 0 && player == 0) {
            return 0
        } else if (clock == 0) {
            return dp(valves, MemoKey("AA", openValves, 26, player - 1), cache)
        }
        //state already exists
        cache[memoKey]?.let {
            return it
        }
        var maxPressureRelease = Int.MIN_VALUE

        val spec = valves[currentVale] ?: error("Unknown valve: $currentVale")
        if (spec.flowRate > 0 && !openValves.contains(currentVale)) {
            val newValves = openValves + currentVale
            val candidate = (clock - 1) * spec.flowRate + dp(
                valves,
                memoKey.copy(openValves = newValves, clock = clock - 1),
                cache
            )
            maxPressureRelease = candidate
        }
        spec.to.forEach { to ->
            val candidate = dp(valves, memoKey.copy(currentVale = to, clock = clock - 1), cache)
            maxPressureRelease = maxPressureRelease.coerceAtLeast(candidate)
        }

        cache[memoKey] = maxPressureRelease
        return maxPressureRelease
    }

    data class MemoFastKey(
        val currentVale: ValveSpec,
        val openValves: Set<ValveSpec>,
        val clock: Int,
        val player: Int = 0
    )

    fun solve1DpFast(input: Sequence<String>): Int {
        val specs: Map<Valve, ValveSpec> = input.map { it.toSpec() }.associateBy { it.from }
        val pathingMap: Map<ValveSpec, PathingResult<ValveSpec>> = buildPathingMap(specs)
        val cache = mutableMapOf<MemoFastKey, Int>()
        val result = dpFast(specs["AA"]!!, MemoFastKey(specs["AA"]!!, setOf(), 30, 0), pathingMap, cache)
        println("cache size: ${cache.size}")
        return result
    }

    fun solve2DpFast(input: Sequence<String>): Int {
        val specs: Map<Valve, ValveSpec> = input.map { it.toSpec() }.associateBy { it.from }
        val pathingMap: Map<ValveSpec, PathingResult<ValveSpec>> = buildPathingMap(specs)
        val cache = mutableMapOf<MemoFastKey, Int>()
        val startValve = specs["AA"]!!
        val result = dpFast(startValve, MemoFastKey(startValve, setOf(), 26, 1), pathingMap, cache)
        println("cache size: ${cache.size}")
        return result
    }


    private fun dpFast(
        startValve: ValveSpec,
        memoKey: MemoFastKey,
        pathingMap: Map<ValveSpec, PathingResult<ValveSpec>>,
        cache: MutableMap<MemoFastKey, Int>
    ): Int {
        val (currentVale, openValves, clock, player) = memoKey
        if (clock == 0) {
            return if (player == 0) {
                0
            } else {
                dpFast(startValve, MemoFastKey(startValve, openValves, 26, player - 1), pathingMap, cache)
            }
        }
        //state already exists
        cache[memoKey]?.let {
            return it
        }
        var maxPressureRelease = 0

        (pathingMap.keys - openValves).forEach { toSpec ->
            val travelTime = pathingMap[currentVale]!!.steps[toSpec]!!.toInt()
            val newClock = clock - travelTime - 1
            if (newClock >= 0) {
                val newValves = openValves + toSpec
                val candidate = newClock * toSpec.flowRate + dpFast(
                    startValve,
                    memoKey.copy(currentVale = toSpec, openValves = newValves, clock = newClock),
                    pathingMap,
                    cache
                )
                maxPressureRelease = maxPressureRelease.coerceAtLeast(candidate)
            }
        }
        cache[memoKey] = maxPressureRelease
        return maxPressureRelease
    }


}
