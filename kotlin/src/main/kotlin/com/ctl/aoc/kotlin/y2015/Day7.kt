package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.y2015.Day7.Instruction.Assignment
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

object Day7 {

    sealed class Connector {

        abstract fun isReady(circuitState: CircuitState): Boolean
        fun ifWire(f: (String) -> Unit) {
            when (this) {
                is Wire -> f(this.name)
            }
        }

        data class Wire(val name: String) : Connector() {
            override fun isReady(circuitState: CircuitState): Boolean = circuitState.wires.containsKey(name)
        }

        data class Value(val value: Short) : Connector() {
            override fun isReady(circuitState: CircuitState): Boolean = true
        }

        companion object {
            fun parse(s: String): Connector = s.toShortOrNull()?.let { Value(it) } ?: Wire(s)
        }
    }

    data class CircuitState(val wires: Map<String, Short> = mapOf()) {
        fun setWire(wire: String, value: Short): CircuitState {
            return copy(wires = wires + (wire to value))
        }

        fun getValue(connector: Connector): Short {
            return when (connector) {
                is Connector.Wire -> wires[connector.name]!!
                is Connector.Value -> connector.value
            }
        }
    }

    sealed class Instruction {

        abstract fun execute(circuitState: CircuitState): CircuitState
        abstract fun isReady(circuitState: CircuitState): Boolean
        abstract val provideValueFor: String

        data class Assignment(val value: Short, val wire: String) : Instruction() {
            override val provideValueFor: String
                get() = wire

            override fun isReady(circuitState: CircuitState): Boolean = true

            override fun execute(circuitState: CircuitState): CircuitState = circuitState.setWire(wire, value)
        }

        sealed class Gate : Instruction() {
            abstract fun compute(x: Short, y: Short): Short
            abstract val left: Connector
            abstract val right: Connector
            abstract val target: String
            override fun execute(circuitState: CircuitState): CircuitState {
                val v = compute(circuitState.getValue(left), circuitState.getValue(right))
                return circuitState.setWire(target, v)
            }

            override fun isReady(circuitState: CircuitState): Boolean = left.isReady(circuitState) && right.isReady(circuitState)

            override val provideValueFor: String
                get() = target

            data class OrGate(override val left: Connector, override val right: Connector, override val target: String) : Gate() {
                override fun compute(x: Short, y: Short): Short = x or y
            }

            data class AndGate(override val left: Connector, override val right: Connector, override val target: String) : Gate() {
                override fun compute(x: Short, y: Short): Short = x and y
            }
        }

        sealed class Operator : Instruction() {
            abstract fun compute(x: Short): Short
            abstract val arg: Connector
            abstract val target: String
            override fun execute(circuitState: CircuitState): CircuitState {
                val v = compute(circuitState.getValue(arg))
                return circuitState.setWire(target, v)
            }

            override val provideValueFor: String
                get() = target

            override fun isReady(circuitState: CircuitState): Boolean = arg.isReady(circuitState)

            data class Not(override val arg: Connector, override val target: String) : Operator() {
                override fun compute(x: Short): Short = x.inv()
            }

            data class LShift(override val arg: Connector, val n: Int, override val target: String) : Operator() {
                override fun compute(x: Short): Short = (x.toInt() shl n).toShort()
            }

            data class RShift(override val arg: Connector, val n: Int, override val target: String) : Operator() {
                override fun compute(x: Short): Short = (x.toInt() shr n).toShort()
            }
        }

        enum class Parser {
            Assignment {
                override fun regex(): Regex = """([\d]+) -> ([a-z]+)""".toRegex()

                override fun parse(m: MatchResult): Instruction {
                    return m.let { Assignment(it.groupValues[1].toShort(), it.groupValues[2]) }
                }
            },
            AndGate {
                override fun regex(): Regex = """([\w]+) AND ([\w]+) -> ([a-z]+)""".toRegex()

                override fun parse(m: MatchResult): Instruction {
                    val left = Connector.parse(m.groupValues[1])
                    val right = Connector.parse(m.groupValues[2])
                    val target = m.groupValues[3]
                    return Gate.AndGate(left, right, target)
                }
            },
            OrGate {
                override fun regex(): Regex = """([\w]+) OR ([\w]+) -> ([a-z]+)""".toRegex()

                override fun parse(m: MatchResult): Instruction {
                    val left = Connector.parse(m.groupValues[1])
                    val right = Connector.parse(m.groupValues[2])
                    val target = m.groupValues[3]
                    return Gate.OrGate(left, right, target)
                }
            },
            Not {
                override fun regex(): Regex = """NOT ([a-z]+) -> ([a-z]+)""".toRegex()

                override fun parse(m: MatchResult): Instruction {
                    val arg = Connector.parse(m.groupValues[1])
                    val target = m.groupValues[2]
                    return Operator.Not(arg, target)
                }
            },
            LShift {
                override fun regex(): Regex = """([a-z]+) LSHIFT ([\d]+) -> ([a-z]+)""".toRegex()

                override fun parse(m: MatchResult): Instruction {
                    val arg = Connector.parse(m.groupValues[1])
                    val n = m.groupValues[2].toInt()
                    val target = m.groupValues[3]
                    return Operator.LShift(arg, n, target)
                }
            },
            RShift {
                override fun regex(): Regex = """([a-z]+) RSHIFT ([\d]+) -> ([a-z]+)""".toRegex()

                override fun parse(m: MatchResult): Instruction {
                    val arg = Connector.parse(m.groupValues[1])
                    val n = m.groupValues[2].toInt()
                    val target = m.groupValues[3]
                    return Operator.RShift(arg, n, target)
                }
            };

            abstract fun regex(): Regex
            abstract fun parse(m: MatchResult): Instruction

            companion object {
                fun parse(s: String): Instruction = values().asSequence()
                        .map { it to it.regex().matchEntire(s) }
                        .filter { it.second != null }
                        .map { it.first.parse(it.second!!) }
                        .firstOrNull()
                        ?: throw IllegalArgumentException(s)
            }
        }

        companion object {
            fun parse(s: String): Instruction = Parser.values().asSequence()
                    .map { it to it.regex().matchEntire(s) }
                    .filter { it.second != null }
                    .map { it.first.parse(it.second!!) }
                    .firstOrNull()
                    ?: throw IllegalArgumentException(s)
        }
    }

    fun resolve(instructions: Sequence<Instruction>): CircuitState {
        var circuitState = CircuitState()

        val readyQ = ArrayDeque<Instruction>()
        val dependencies = mutableMapOf<String, List<Instruction>>()

        instructions.forEach { instr ->
            when (instr) {
                is Instruction.Assignment -> readyQ.add(instr)
                is Instruction.Gate -> {
                    instr.left.ifWire { dependencies.merge(it, listOf(instr)) { t, u -> t + u } }
                    instr.right.ifWire { dependencies.merge(it, listOf(instr)) { t, u -> t + u } }
                }
                is Instruction.Operator -> {
                    instr.arg.ifWire { dependencies.merge(it, listOf(instr)) { t, u -> t + u } }
                }
            }
        }

        var current: Instruction
        while (readyQ.isNotEmpty()) {
            current = readyQ.remove()
            circuitState = current.execute(circuitState)
            dependencies[current.provideValueFor]?.forEach { instr ->
                if (instr.isReady(circuitState)) readyQ.add(instr)
            }
        }

        return circuitState
    }

    fun solve1(lines: Sequence<String>): Short {
        val instructions = lines.map { Instruction.parse(it) }
        val state = resolve(instructions)
        return state.getValue(Connector.Wire("a"))
    }
}