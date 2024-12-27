package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Graph
import com.ctl.aoc.kotlin.y2024.Day24.AndGate
import com.ctl.aoc.kotlin.y2024.Day24.OrGate
import com.ctl.aoc.kotlin.y2024.Day24.XOrGate
import kotlin.random.Random

object Day24 {

    sealed class Gate(open val a: String, open val b: String, open val output: String) {
        abstract fun compute(x: Boolean, y: Boolean): Boolean

        abstract fun gateLabel(): String

        abstract fun replaceOutput(newOutput: String): Gate

        fun execute(values: MutableMap<String, Boolean>) {
            val x = values[a] ?: error("Unknown value: $a")
            val y = values[b] ?: error("Unknown value: $b")
            values[output] = compute(x, y)
        }
    }

    data class AndGate(override val a: String, override val b: String, override val output: String) :
        Gate(a, b, output) {
        override fun compute(x: Boolean, y: Boolean): Boolean {
            return x && y
        }

        override fun gateLabel(): String {
            return "AND"
        }

        override fun replaceOutput(newOutput: String): Gate {
            return copy(output = newOutput)
        }
    }

    data class OrGate(override val a: String, override val b: String, override val output: String) :
        Gate(a, b, output) {
        override fun compute(x: Boolean, y: Boolean): Boolean {
            return x || y
        }

        override fun gateLabel(): String {
            return "OR"
        }

        override fun replaceOutput(newOutput: String): Gate {
            return copy(output = newOutput)
        }
    }

    data class XOrGate(override val a: String, override val b: String, override val output: String) :
        Gate(a, b, output) {
        override fun compute(x: Boolean, y: Boolean): Boolean {
            return x xor y
        }

        override fun gateLabel(): String {
            return "XOR"
        }

        override fun replaceOutput(newOutput: String): Gate {
            return copy(output = newOutput)
        }
    }

    data class WiringSpec(
        val initialValues: Map<String, Boolean>,
        val gates: List<Gate>
    ) {
        val sorted: List<String> by lazy {
            val rootNodes = ArrayDeque<String>()
            rootNodes.addAll(initialValues.keys)
            val graph = Graph<String>()
            gates.forEach { gate ->
                graph.addDirectedEdge(gate.output, gate.a)
                graph.addDirectedEdge(gate.output, gate.b)
            }

            val sorted = mutableListOf<String>()
            while (rootNodes.isNotEmpty()) {
                val node = rootNodes.removeFirst()
                sorted.add(node)
                graph.incomingNodes(node).forEach { from ->
                    graph.removeEdge(from, node)
                    if (graph.outgoingNodes(from).isEmpty()) {
                        rootNodes.add(from)
                    }
                }
            }
            sorted
        }

        private val gatesByOutput = gates.associateBy { it.output }

        fun execute(init: Map<String, Boolean> = initialValues): Long {
            val values = init.toMutableMap()
            this.sorted.forEach { node ->
                val gate = gatesByOutput[node]
                gate?.execute(values)
            }
            return getNumber("z", values)
        }

        fun rewrite(wires: Set<Pair<String, String>>): WiringSpec {
            val newGates = this.gates.toMutableList()
            wires.forEach { (a, b) ->
                val ia = newGates.indexOfFirst { it.output == a }
                val ib = newGates.indexOfFirst { it.output == b }
                val ga = newGates[ia]
                val gb = newGates[ib]
                newGates.removeIf { it.output == a }
                newGates.removeIf { it.output == b }
                newGates.add(ga.replaceOutput(b))
                newGates.add(gb.replaceOutput(a))
            }
            return this.copy(gates = newGates)
        }

        fun checkBits(init: Map<String, Boolean>) {
            val values = init.toMutableMap()
            val x = getNumber("x", values)
            val y = getNumber("y", values)
            println("x = $x")
            println("y = $y")
            val sum = x + y
            val z = execute(values)
            val zString = z.toString(2).reversed()
            println("sum\t${sum.toString(2)}")
            println("z\t${z.toString(2)}")
            sum.toString(2).reversed().forEachIndexed { index, c ->
                if (c != zString[index]) {
                    println("Different at $index")
                }
            }
        }
    }

    fun solve1(input: String): Long {
        val wiringSpec = input.parseWiringSpec()
        return wiringSpec.execute()
    }

    fun solve2(input: String): String {
        val reWire = setOf(
            "z11" to "sps",
            "z23" to "frt",
            "z05" to "tst",
            "pmd" to "cgh"
        )
        val wiringSpec = input.parseWiringSpec().rewrite(
            reWire
        )
        val max = "".padStart(45, '1').toLong(2)

        repeat(10) {
            val a = Random.nextLong(max)
            val b = Random.nextLong(max)
            val values = mutableMapOf<String, Boolean>().setValues(a, b)
            wiringSpec.checkBits(values)
        }

        return reWire.asSequence()
            .flatMap { it.toList() }
            .sorted()
            .joinToString(",")
    }

    private fun getNumber(prefix: String, values: Map<String, Boolean>): Long {
        return getNumberString(prefix, values)
            .toLong(2)
    }

    private fun getNumberString(
        prefix: String,
        values: Map<String, Boolean>,
        separator: String = ""
    ) = values.asSequence()
        .filter { it.key.startsWith(prefix) }
        .sortedByDescending { it.key }
        .joinToString(separator = separator) { if (it.value) "1" else "0" }
}

private val gateRegex = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex()

private fun String.parseWiringSpec(): Day24.WiringSpec {
    val (top, bottom) = this.split("\n\n")
    val values: Map<String, Boolean> = buildMap {
        top.trim().lineSequence().forEach { line ->
            val (name, value) = line.split(": ")
            this[name] = value == "1"
        }
    }

    val gates = bottom.trim().lineSequence()
        .map { line ->
            val matchEntire = gateRegex.matchEntire(line) ?: error("Invalid line: $line")
            val (a, op, b, output) = matchEntire.destructured
            when (op) {
                "AND" -> AndGate(a, b, output)
                "OR" -> OrGate(a, b, output)
                "XOR" -> XOrGate(a, b, output)
                else -> error("Invalid op: $op")
            }
        }
        .toList()
    return Day24.WiringSpec(values, gates)
}

private fun MutableMap<String, Boolean>.setValues(x: Long, y: Long): MutableMap<String, Boolean> {
    val xString = x.toString(2).padStart(45, '0').reversed()
    val yString = y.toString(2).padStart(45, '0').reversed()
    repeat(45) {
        val padded = it.toString().padStart(2, '0')
        this["x$padded"] = xString[it] == '1'
        this["y$padded"] = yString[it] == '1'
    }
    return this
}
