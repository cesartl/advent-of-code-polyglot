package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.microsoft.z3.BoolExpr
import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import com.microsoft.z3.Model
import com.microsoft.z3.Status


object Day10 {

    data class MachineReq(
        val onLights: Int,
        val buttons: List<Int>,
        val buttonsNormalised: List<List<Int>>,
        val buttonsRaw: List<List<Int>>,
        val joltages: List<Int>
    )

    private val lightsRegex = """\[([.#]+)\]""".toRegex()
    private val buttonsRegex = """\(([\d,]+)\)""".toRegex()
    private val joltagesRegex = """\{([\d,]+)\}""".toRegex()

    private fun parseReg(line: String): MachineReq {
        val lights = lightsRegex.find(line)?.groupValues[1]?.let { parseDots(it) } ?: error("")
        val buttonsRaw = buttonsRegex.findAll(line)
            .map { match -> match.groupValues[1].split(",").map { it.toInt() } }
            .toList()
        val buttons = buttonsRaw
            .map { parseButtons(it) }
        val joltages = joltagesRegex.find(line)
            ?.groupValues[1]?.split(",")?.map { it.toInt() }
            ?: error("No joltages found")

        val buttonsNormalised = buttonsRaw
            .asSequence()
            .map { button ->
                val set = button.toSet()
                joltages.mapIndexed { i, _ ->
                    if (set.contains(i)) {
                        1
                    } else {
                        0
                    }
                }
            }
            .toList()

        return MachineReq(lights, buttons, buttonsNormalised = buttonsNormalised, buttonsRaw = buttonsRaw, joltages)
    }

    fun parseDots(dotString: String): Int {
        return dotString.foldIndexed(0) { i, acc, c ->
            val diff = if (c == '#') {
                1 shl i
            } else {
                0
            }
            diff + acc
        }
    }

    fun parseButtons(button: List<Int>): Int {
        return button.fold(0) { acc, light ->
            val diff = 1 shl light
            diff + acc
        }
    }

    fun parseJoltage(button: List<Int>): Int {
        return button.foldIndexed(0) { i, acc, joltage ->
            val diff = joltage * (1 shl i)
            diff + acc
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val reqs = input.map { parseReg(it) }.toList()
        return reqs.sumOf {
            val minButton = minButton(it)
            println("minButton: $minButton")
            minButton
        }
    }

    data class State(
        val lights: Int,
    )

    private fun minButton(machineReq: MachineReq): Int {
        val start = State(0)
        val result = Dijkstra.traverseIntPredicate(
            start = start,
            end = { it?.lights == machineReq.onLights },
            nodeGenerator = { state ->
                machineReq.buttons.asSequence()
                    .map { button ->
                        val newLights = state.lights xor button
                        State(newLights)
                    }
            },
            distance = { _, _ -> 1 }
        )
        val last = result.lastNode
        return result.steps[last!!]!!
    }

    data class CounterState(
        val counters: List<Int>,
    ) {
        fun distance(machineReq: MachineReq): Int {
            return machineReq.joltages.asSequence().zip(this.counters.asSequence()).sumOf { (j, s) -> j - s }
        }

        fun isValid(machineReq: MachineReq): Boolean {
            return machineReq.joltages.asSequence().zip(this.counters.asSequence()).all { (j, s) -> j >= s }
        }
    }

    private fun minLever(machineReq: MachineReq): Int {
        val start = CounterState(machineReq.joltages.map { 0 })
        val result = Dijkstra.traverseIntPredicate(
            start = start,
            end = {
                it?.counters == machineReq.joltages
            },
            nodeGenerator = { state ->
                machineReq.buttonsNormalised.asSequence()
                    .map { button ->
                        val newLights = addToCounter(state.counters, button)
                        CounterState(newLights)
                    }
                    .filter { it.isValid(machineReq) }
            },
            distance = { _, _ -> 1 },
            heuristic = { state ->
                state.distance(machineReq)
            }
        )
        val last = result.lastNode
        return result.steps[last!!]!!
    }

    private fun addToCounter(counter: List<Int>, button: List<Int>): List<Int> {
        assert(counter.size == button.size)
        return counter.asSequence().zip(button.asSequence()).map { (a, b) -> a + b }.toList()
    }

    fun solve2(input: Sequence<String>): Int {
        val reqs = input.map { parseReg(it) }.toList()
        return reqs.sumOf { solve(it) }
    }

    fun solve2Bis(input: Sequence<String>): Int {
        val reqs = input.map { parseReg(it) }.toList()
        return reqs.sumOf {
            val minButton = minLever(it)
            println("minButton: $minButton")
            minButton
        }
    }

    fun solve(req: MachineReq): Int {
        val joltageToButtons: Map<Int, List<Int>> = req.joltages.withIndex().associate { (i, _) ->
            i to req.buttonsRaw.asSequence()
                .withIndex()
                .filter { it.value.contains(i) }
                .map { it.index }
                .toList()
        }
        val cfg = hashMapOf("model" to "true")

        Context(cfg).use { ctx ->
            val opt = ctx.mkOptimize()
            val variables = req.buttonsRaw.indices.map { ctx.mkIntConst("x$it") }
            variables.forEach { v ->
                val constraint = ctx.mkGt(v, ctx.mkInt(-1))
                opt.Add(constraint)
            }
            val sum = ctx.mkAdd(*variables.toTypedArray())
            opt.MkMinimize(sum)
            joltageToButtons.forEach { (joltateIndex, buttons) ->
                val eq = buttons.joinToString(" + ") { "x_$it" }
                println("${req.joltages[joltateIndex]} = $eq")
                val v = buttons.map { variables[it] }
                val c1: BoolExpr = ctx.mkEq(
                    ctx.mkReal(req.joltages[joltateIndex]),
                    ctx.mkAdd(*v.toTypedArray())
                )
                opt.Add(c1)
            }
            if (opt.Check() === Status.SATISFIABLE) {
                val model: Model = opt.model
                println("Status: " + opt.Check())
                println("Model:")
                val result = model.evaluate(sum, false)
                println("Sum = $result")
                return (result as IntNum).int
            } else {
                error("Problem is UNSAT or UNKNOWN")
            }
        }
    }
}
