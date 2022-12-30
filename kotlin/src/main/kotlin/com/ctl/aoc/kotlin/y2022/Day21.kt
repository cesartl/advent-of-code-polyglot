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
        val sorted = sortJobs(jobs)
        val variables = mutableMapOf<String, Long>()
        sorted.forEach {
            it.eval(variables)
        }
        return variables["root"]!!
    }

    fun solve2(input: Sequence<String>): Long {
        val jobs = input.map { it.parseJob() }
        val sorted = sortJobs(jobs).filter {
            it.variable() != "humn"
        }
//        val variables = mutableMapOf<String, Variable>()
//        variables["humn"] = Variable.Unknown("humn")
//        sorted.forEach {
//            it.evaluateVariables(variables)
//        }
//        val root = jobs.first { it.variable() == "root" } as Job.Operation
//        println(variables[root.x]?.describe())
//        println(variables[root.y]?.describe())
//        println(27276913415446696L / 8085)

        val expressions = mutableMapOf<String, Expression>()
        expressions["humn"] = Expression.Variable("humn")
        sorted.forEach {
            it.buildExpression(expressions)
        }
        val root = jobs.first { it.variable() == "root" } as Job.Operation

        val x = expressions[root.x]!!
        val y = expressions[root.y]!!
        println(x.describe())
        println(y.describe())
        return solve(x, y)
    }

    sealed class Expression {
        data class Mult(val a: Expression, val b: Expression) : Expression()
        data class Div(val a: Expression, val b: Expression) : Expression()
        data class Add(val a: Expression, val b: Expression) : Expression()
        data class Subtract(val a: Expression, val b: Expression) : Expression()

        data class Variable(val name: String) : Expression()
        data class Value(val value: Long) : Expression()

        fun describe(): String = when (this) {
            is Add -> "(${a.describe()}+${b.describe()})"
            is Div -> "(${a.describe()}/${b.describe()})"
            is Mult -> "(${a.describe()}*${b.describe()})"
            is Subtract -> "(${a.describe()}-${b.describe()})"
            is Value -> this.value.toString()
            is Variable -> this.name
        }

        fun evaluate(): Long = when (this) {
            is Add -> a.evaluate() + b.evaluate()
            is Div -> a.evaluate() / b.evaluate()
            is Mult -> a.evaluate() * b.evaluate()
            is Subtract -> a.evaluate() - b.evaluate()
            is Value -> value
            is Variable -> error("Cannot evaluate unknown variable")
        }

        fun containsVariable(): Boolean = when (this) {
            is Add -> a.containsVariable() || b.containsVariable()
            is Div -> a.containsVariable() || b.containsVariable()
            is Mult -> a.containsVariable() || b.containsVariable()
            is Subtract -> a.containsVariable() || b.containsVariable()
            is Value -> false
            is Variable -> true
        }
    }

    private fun Job.buildExpression(expressions: MutableMap<String, Expression>) {
        when (this) {
            is Job.Assignment -> {
                expressions[this.variable] = Expression.Value(this.value)
            }

            is Job.Operation -> {
                val a = expressions[this.x] ?: error("Unknown $x in ${this.equation()}")
                val b = expressions[this.y] ?: error("Unknown $y in ${this.equation()}")

                val e = when (this.o) {
                    "+" -> {
                        Expression.Add(a, b)
                    }

                    "-" -> {
                        Expression.Subtract(a, b)
                    }

                    "*" -> {
                        Expression.Mult(a, b)
                    }

                    "/" -> {
                        Expression.Div(a, b)
                    }

                    else -> error("")
                }
                expressions[this.variable] = e
            }
        }
    }

    private fun solve(lhs: Expression, rhs: Expression): Long = when (lhs) {
        is Expression.Add -> if (lhs.a.containsVariable()) {
            solve(lhs.a, Expression.Subtract(rhs, lhs.b))
        } else {
            solve(lhs.b, Expression.Subtract(rhs, lhs.a))
        }

        is Expression.Subtract -> if (lhs.a.containsVariable()) {
            solve(lhs.a, Expression.Add(rhs, lhs.b))
        } else {
            solve(lhs.b, Expression.Subtract(lhs.a, rhs))
        }

        is Expression.Mult -> if (lhs.a.containsVariable()) {
            solve(lhs.a, Expression.Div(rhs, lhs.b))
        } else {
            solve(lhs.b, Expression.Div(rhs, lhs.a))
        }

        is Expression.Div -> if (lhs.a.containsVariable()) {
            solve(lhs.a, Expression.Mult(rhs, lhs.b))
        } else {
            solve(lhs.b, Expression.Div(lhs.b, rhs))
        }

        is Expression.Value -> lhs.value
        is Expression.Variable -> rhs.evaluate()
    }

    fun sortJobs(jobs: Sequence<Job>): List<Job> {
        val graph = Graph<String>()
        val jobIndex = mutableMapOf<String, Job>()
        jobs.forEach { job ->
            when (job) {
                is Job.Operation -> {
                    graph.addDirectedEdge(job.variable, job.x)
                    graph.addDirectedEdge(job.variable, job.y)
                    jobIndex[job.variable] = job
                }

                is Job.Assignment -> {
                    jobIndex[job.variable] = job
                }
            }
        }
        return graph.topologicalSort().mapNotNull { jobIndex[it] }
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
