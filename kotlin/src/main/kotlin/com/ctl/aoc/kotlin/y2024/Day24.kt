package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Graph

object Day24 {

    private val gateRegex = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex()

    sealed class Gate(open val a: String, open val b: String, open val output: String) {
        abstract fun compute(x: Boolean, y: Boolean): Boolean

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
    }

    data class OrGate(override val a: String, override val b: String, override val output: String) :
        Gate(a, b, output) {
        override fun compute(x: Boolean, y: Boolean): Boolean {
            return x || y
        }
    }

    data class XOrGate(override val a: String, override val b: String, override val output: String) :
        Gate(a, b, output) {
        override fun compute(x: Boolean, y: Boolean): Boolean {
            return x xor y
        }
    }


    fun solve1(input: String): Long {
        val (top, bottom) = input.split("\n\n")
        val values = mutableMapOf<String, Boolean>()
        top.trim().lineSequence().forEach { line ->
            val (name, value) = line.split(": ")
            values[name] = value == "1"
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

        val rootNodes = ArrayDeque<String>()
        rootNodes.addAll(values.keys)


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

        val gatesByOutput = gates.associateBy { it.output }
        sorted.forEach { node ->
            val gate = gatesByOutput[node]
            gate?.execute(values)
        }
        return getNumber("z", values)
    }

    private fun getNumber(prefix: String, values: MutableMap<String, Boolean>): Long{
        return values.asSequence()
            .filter { it.key.startsWith("z") }
            .sortedByDescending { it.key }
            .joinToString(separator = "") { if (it.value) "1" else "0" }
            .toLong(2)
    }

    fun solve2(input: String): Int {
        TODO()
    }
}
