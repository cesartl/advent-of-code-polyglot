package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Graph

object Day21 {

    sealed class Job {
        data class Assignment(val variable: String, val value: Long) : Job()
        data class Operation(val variable: String, val x: String, val o: String, val y: String) : Job()
    }

    private fun Job.variable(): String = when (this) {
        is Job.Assignment -> this.variable
        is Job.Operation -> this.variable
    }

    private fun Job.equation(): String = when (this) {
        is Job.Assignment -> "$variable=${this.value}"
        is Job.Operation -> "$variable=${this.x}${o}$y"
    }

    private fun String.operation(x: Long, y: Long): Long = when (this) {
        "*" -> x * y
        "+" -> x + y
        "-" -> x - y
        "/" -> x / y
        else -> error(this)
    }

    private fun Job.eval(variables: MutableMap<String, Long>) {
        when (this) {
            is Job.Assignment -> {
                variables[this.variable] = this.value
            }

            is Job.Operation -> {
                val xValue = variables[this.x] ?: error("Missing ${this.x}")
                val yValue = variables[this.y] ?: error("Missing ${this.y}")
                variables[this.variable] = this.o.operation(xValue, yValue)
            }
        }
    }

    private fun Job.printEquations(variables: MutableMap<String, String>) {
        when (this) {
            is Job.Assignment -> {
                variables[this.variable] = this.value.toString()
            }

            is Job.Operation -> {
                val xValue = variables[this.x] ?: this.x
                val yValue = variables[this.y] ?: this.y
                variables[this.variable] = "($xValue${o}$yValue)"
            }
        }
    }

    fun solve1(input: Sequence<String>): Long {
        val jobs = input.map { it.parseJob() }
        val sorted = topologicalSort(jobs)
        val variables = mutableMapOf<String, Long>()
        sorted.forEach {
            it.eval(variables)
        }
        return variables["root"]!!
    }

    sealed class Variable {
        data class Evaluated(val value: Long) : Variable()
        data class Unknown(val eq: String) : Variable()
    }

    private fun Variable.mult(n: Long): Variable = when (this) {
        is Variable.Evaluated -> Variable.Evaluated(n * value)
        is Variable.Unknown -> Variable.Unknown("$n*($eq)")
    }

    private fun Variable.div(n: Long): Variable = when (this) {
        is Variable.Evaluated -> Variable.Evaluated(n / value)
        is Variable.Unknown -> Variable.Unknown("$n/($eq)")
    }

    private fun Variable.describe(): String = when (this) {
        is Variable.Evaluated -> "${this.value}"
        is Variable.Unknown -> this.eq
    }

    private fun Job.evaluateVariables(variables: MutableMap<String, Variable>) {
        when (this) {
            is Job.Assignment -> {
                variables[this.variable] = Variable.Evaluated(this.value)
            }

            is Job.Operation -> {
                val xVar = variables[this.x] ?: error("Unknown $x in ${this.equation()}")
                val yVar = variables[this.y] ?: error("Unknown $y in  ${this.equation()}")
                val v = if (xVar is Variable.Evaluated && yVar is Variable.Evaluated) {
                    val eval = this.o.operation(xVar.value, yVar.value)
                    Variable.Evaluated(eval)
//                } else if (xVar is Variable.Evaluated) {
//                    if (this.o == "*") {
//                        yVar.mult(xVar.value)
//                    } else if (this.o == "/") {
//                        yVar.div(xVar.value)
//                    } else {
//                        Variable.Unknown("(${xVar.describe()}${this.o}${yVar.describe()})")
//                    }
//                } else if (yVar is Variable.Evaluated) {
//                    if (this.o == "*") {
//                        xVar.mult(yVar.value)
//                    } else if (this.o == "/") {
//                        xVar.div(yVar.value)
//                    } else {
//                        Variable.Unknown("(${xVar.describe()}${this.o}${yVar.describe()})")
//                    }
                } else {
                    if (this.o == "+" || this.o == "-") {
                        Variable.Unknown("(${xVar.describe()}${this.o}${yVar.describe()})")
                    } else {
                        Variable.Unknown("${xVar.describe()}${this.o}${yVar.describe()}")
                    }
                }
                variables[this.variable] = v
            }
        }
    }

    fun solve2(input: Sequence<String>): Int {
        val jobs = input.map { it.parseJob() }
        val sorted = topologicalSort(jobs).filter {
            it.variable() != "humn"
        }
        val variables = mutableMapOf<String, Variable>()
        variables["humn"] = Variable.Unknown("humn")
        sorted.forEach {
//            println("doing ${it.equation()}")
            it.evaluateVariables(variables)
        }
        val root = jobs.first { it.variable() == "root" } as Job.Operation
        println(variables[root.x]?.describe())
        println(variables[root.y]?.describe())
        println(27276913415446696L/8085)
        TODO()
    }

    fun topologicalSort(jobs: Sequence<Job>): List<Job> {
        val graph = Graph<String>()
        val rootNodes = ArrayDeque<String>()
        val jobIndex = mutableMapOf<String, Job>()
        jobs.forEach { job ->
            when (job) {
                is Job.Operation -> {
                    graph.addDirectedEdge(job.variable, job.x)
                    graph.addDirectedEdge(job.variable, job.y)
                    jobIndex[job.variable] = job
                }

                is Job.Assignment -> {
                    rootNodes.add(job.variable)
                    jobIndex[job.variable] = job
                }
            }
        }
        val sorted = mutableListOf<String>()
        while (rootNodes.isNotEmpty()) {
            val current = rootNodes.removeFirst()
            sorted.add(current)
            graph.incomingNodes(current).forEach { from ->
                graph.removeEdge(from, current)
                if (graph.outgoingNodes(from).isEmpty()) {
                    rootNodes.add(from)
                }
            }
        }

        return sorted.mapNotNull { jobIndex[it] }
    }

    private fun String.parseJob(): Job {
        val s = this.split(":").map { it.trim() }
        val variable = s[0]
        return s[1].toLongOrNull()?.let {
            Job.Assignment(variable, it)
        } ?: run {
            val elements = s[1].split(" ").map { it.trim() }
            Job.Operation(variable, elements[0], elements[1], elements[2])
        }
    }
}
