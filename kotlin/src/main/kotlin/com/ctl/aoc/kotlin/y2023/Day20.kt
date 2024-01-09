package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.lcm


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
    val lowCount: Long,
    val highCount: Long
)

abstract class PulseModule {

    val outputs: MutableList<PulseAddress> = mutableListOf()

    private var lowCount: Long = 0L
    private var highCount: Long = 0L
    abstract fun nextBusId(): BusId

    abstract val prefix: String

    fun describe(): String = "$name ($prefix)"

    fun reset() {
        lowCount = 0
        highCount = 0
        innerReset()
    }

    abstract fun innerReset()

    fun connectsTo(pulseAddress: PulseAddress) {
        outputs.add(pulseAddress)
    }

    fun process(pulseInput: PulseInput): Sequence<PulseInput> {
        if (pulseInput.pulse) {
            highCount++
        } else {
            lowCount++
        }
        return innerProcess(pulseInput)
    }

    abstract fun innerProcess(pulseInput: PulseInput): Sequence<PulseInput>

    fun getState(): ModuleState {
        return ModuleState(lowCount, highCount)
    }

    abstract val name: String

    protected fun sendToAll(pulse: Pulse): Sequence<PulseInput> {
        return outputs
            .asSequence()
            .map { addr -> PulseInput(name, addr, pulse) }
    }
}

class GenericModule(override val name: String) : PulseModule() {

    override fun nextBusId(): BusId = 0
    override val prefix: String
        get() = ""

    override fun innerReset() {}

    override fun innerProcess(pulseInput: PulseInput): Sequence<PulseInput> {
        return sendToAll(pulseInput.pulse)
    }

}

class FlipFlopModule(override val name: String) : PulseModule() {

    private var on: Boolean = false
    override fun nextBusId(): BusId = 0

    override val prefix: String
        get() = "%"

    override fun innerReset() {
        this.on = false
    }

    override fun innerProcess(pulseInput: PulseInput): Sequence<PulseInput> {
        if (pulseInput.pulse) {
            return emptySequence()
        }
        this.on = !on
        return sendToAll(on)
    }
}

class ConjunctionModule(override val name: String) : PulseModule() {

    private val memory: MutableList<Pulse> = mutableListOf()

    private var nextBusId: BusId = 0

    override val prefix: String
        get() = "&"

    override fun innerReset() {
        memory.indices.forEach {
            memory[it] = LOW
        }
    }

    override fun nextBusId(): BusId {
        memory.add(LOW)
        return nextBusId++
    }

    override fun innerProcess(pulseInput: PulseInput): Sequence<PulseInput> {
        memory[pulseInput.address.busId] = pulseInput.pulse
        val pulse = !memory.all { it }
        return sendToAll(pulse)
    }

}

data class PulseState(
    val lowCount: Long,
    val highCount: Long,
    val modules: Map<String, ModuleState>
)

class PulseCircuit {
    private val modules: MutableMap<String, PulseModule> = mutableMapOf()

    private var lowCount: Long = 0L
    private var highCount: Long = 0L

    fun pushButtons(): Sequence<PulseState> {
        return generateSequence(state()) { pushButton() }
    }

    fun reset() {
        lowCount = 0
        highCount = 0
        modules.values.forEach { it.reset() }
    }

    fun findIndex(module: String, predicate: (ModuleState) -> Boolean): Int {
        return findIndex { predicate(it.modules[module]!!) }
    }

    fun findIndex(predicate: (PulseState) -> Boolean): Int {
        return pushButtons()
            .withIndex()
            .first { predicate(it.value) }
            .index
    }

    fun pushButton(): PulseState {
        var newLow = 0L
        var newHigh = 0L
        val queue = ArrayDeque<PulseInput>()
        queue.addFirst(PulseInput("button", PulseAddress("broadcaster", 0), LOW))
        var current: PulseInput
        while (queue.isNotEmpty()) {
            current = queue.removeFirst()
            if (current.pulse) {
                newHigh++
            } else {
                newLow++
            }
            val module = modules[current.address.module] ?: error("Unknown module")
            module.process(current)
                .forEach { next -> queue.addLast(next) }
        }
        lowCount += newLow
        highCount += newHigh

        return state()
    }

    private fun state(): PulseState {
        return PulseState(
            lowCount, highCount, moduleStates()
        )
    }

    private fun moduleStates(): Map<String, ModuleState> {
        return modules.mapValues { it.value.getState() }
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

    fun printDependencies() {
        println("button -> broadcaster")
        modules.values.forEach { module ->
            module.outputs.forEach { output ->
                val target: PulseModule = modules[output.module] ?: error("")
                println(
                    """
                    "${module.describe()}" -> "${target.describe()}" 
                """.trimIndent()
                )
            }
        }
    }

    fun findPredecessors(module: String): Sequence<PulseModule> {
        return modules.values
            .asSequence()
            .filter { m -> m.outputs.any { it.module == module } }
    }
}


object Day20 {
    fun solve1(input: Sequence<String>): Long {
        val circuit = PulseCircuit()
        circuit.importConfig(input)
        val s = circuit.pushButtons().drop(1000).first()
        return s.highCount * s.lowCount
    }

    fun solve2(input: Sequence<String>): Long {
        val circuit = PulseCircuit()
        circuit.importConfig(input)
//        circuit.printDependencies()

        val cycles = circuit.findPredecessors("rx")
            .flatMap { circuit.findPredecessors(it.name) }
            .map { m ->
                circuit.reset()
                circuit.findIndex(m.name) { it.lowCount > 0 }
            }.map { it.toBigInteger() }
        return lcm(cycles).toLong()
    }

}
