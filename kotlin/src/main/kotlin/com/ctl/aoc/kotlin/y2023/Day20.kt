package com.ctl.aoc.kotlin.y2023


typealias BusId = Int
typealias Pulse = Boolean

const val HIGH: Pulse = true
const val LOW: Pulse = false

data class PulseAddress(
    val module: String,
    val busId: BusId
) {
    override fun toString(): String {
        return "$module:$busId"
    }
}

data class PulseInput(
    val origin: String,
    val address: PulseAddress,
    val pulse: Pulse,
) {
    override fun toString(): String {
        val p = if (pulse) "high" else "low"
        return "$origin: $p -> $address"
    }
}

data class ModuleState(
    val moduleName: String,
    val state: Int
)

interface PulseModule {
    fun nextBusId(): BusId

    fun connectsTo(pulseAddress: PulseAddress)
    fun process(pulseInput: PulseInput): Sequence<PulseInput>
    fun getState(): ModuleState

    val name: String
}

class GenericModule(override val name: String) : PulseModule {

    private val outputs: MutableList<PulseAddress> = mutableListOf()
    override fun nextBusId(): BusId = 0
    override fun connectsTo(pulseAddress: PulseAddress) {
        outputs.add(pulseAddress)
    }

    override fun process(pulseInput: PulseInput): Sequence<PulseInput> {
        return outputs
            .asSequence()
            .map { addr -> PulseInput(name, addr, pulseInput.pulse) }
    }

    override fun getState(): ModuleState = ModuleState(name, 0)
}

class FlipFlopModule(override val name: String) : PulseModule {

    private val outputs: MutableList<PulseAddress> = mutableListOf()

    private var on: Boolean = false
    override fun nextBusId(): BusId = 0

    override fun connectsTo(pulseAddress: PulseAddress) {
        outputs.add(pulseAddress)
    }

    override fun process(pulseInput: PulseInput): Sequence<PulseInput> {
        if (pulseInput.pulse) {
            return emptySequence()
        }
        this.on = !on
        return outputs
            .asSequence()
            .map { addr -> PulseInput(name, addr, on) }
    }

    override fun getState(): ModuleState {
        return ModuleState(name, if (on) 1 else 0)
    }
}

class ConjunctionModule(override val name: String) : PulseModule {

    private val outputs: MutableList<PulseAddress> = mutableListOf()

    private val memory: MutableList<Pulse> = mutableListOf()

    private var nextBusId: BusId = 0
    override fun nextBusId(): BusId {
        memory.add(LOW)
        return nextBusId++
    }

    override fun connectsTo(pulseAddress: PulseAddress) {
        outputs.add(pulseAddress)
    }

    override fun process(pulseInput: PulseInput): Sequence<PulseInput> {
        memory[pulseInput.address.busId] = pulseInput.pulse
        val pulse = !memory.all { it }
        return outputs
            .asSequence()
            .map { addr -> PulseInput(name, addr, pulse) }
    }

    override fun getState(): ModuleState {
        val i = memory.foldIndexed(0) { i, acc, p ->
            val next = if (p) {
                1.shl(memory.size - i)
            } else 0
            acc or next
        }
        return ModuleState(name, i)
    }

}

data class PulseState(
    val lowCount: Long,
    val highCount: Long,
    val modules: Set<ModuleState>
)

class PulseCircuit {
    private val modules: MutableMap<String, PulseModule> = mutableMapOf()

    private var lowCount: Long = 0L
    private var highCount: Long = 0L

    fun pushButtons(): Sequence<PulseState> {
        return generateSequence(state()) { pushButton() }
    }

    fun pushButton(): PulseState {
        var newLow = 0L
        var newHigh = 0L
        val queue = ArrayDeque<PulseInput>()
        queue.addFirst(PulseInput("button", PulseAddress("broadcaster", 0), LOW))
        var current: PulseInput
        while (queue.isNotEmpty()) {
            current = queue.removeFirst()
//            println(current)
            if (current.pulse) {
                newHigh++
            } else {
                newLow++
            }
            val module = modules[current.address.module] ?: error("Unknown module")
            module.process(current)
                .forEach { next -> queue.addLast(next) }
        }
//        println("")
        lowCount += newLow
        highCount += newHigh

        return state()
    }

    private fun state(): PulseState {
        return PulseState(
            lowCount, highCount, moduleStates()
        )
    }

    private fun moduleStates(): Set<ModuleState> {
        return modules
            .values
            .asSequence()
            .map { it.getState() }
            .toSet()
    }

    fun importConfig(input: Sequence<String>) {
        val outputs = mutableMapOf<String, List<String>>()
        input
            .filter { it.isNotBlank() }
            .forEach { line ->
                val parts = line.split("->").map { it.trim() }
                val name = parts[0]
                val connections =
                    if (parts.size > 1) {
                        parts[1]
                            .splitToSequence(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                            .toList()
                    } else listOf()

                val module = if (name.startsWith("%")) {
                    FlipFlopModule(name.drop(1))
                } else if (name.startsWith("&")) {
                    ConjunctionModule(name.drop(1))
                } else {
                    GenericModule(name)
                }
                addModule(module)
                outputs[module.name] = connections
            }
        outputs.forEach { (moduleName, outputs) ->
            val from = modules[moduleName] ?: error(moduleName)
            outputs.forEach { output ->
                val to = modules.computeIfAbsent(output) { GenericModule(it) }
                val bus = to.nextBusId()
                val address = PulseAddress(output, bus)
                from.connectsTo(address)
            }
        }
    }

    private fun addModule(module: PulseModule) {
        modules[module.name] = module
    }
}


object Day20 {
    fun solve1(input: Sequence<String>): Long {
        val circuit = PulseCircuit()
        circuit.importConfig(input)
        val s = circuit.pushButtons().drop(1000).first()
        println(s)
        return s.highCount * s.lowCount
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
