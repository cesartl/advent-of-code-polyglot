package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.y2021.Day22.size

data class MachinePart(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int,
) {
    val totalRating: Int by lazy {
        x + m + a + s
    }
}

typealias MachinePartGetter = (MachinePart) -> Int

private fun String.partGetter(): MachinePartGetter = when (this) {
    "x" -> {
        { it.x }
    }

    "m" -> {
        { it.m }
    }

    "a" -> {
        { it.a }
    }

    "s" -> {
        { it.s }
    }

    else -> error(this)
}

private val partRegex = """\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)\}""".toRegex()

private fun String.parseMachinePart(): MachinePart {
    val match = partRegex.matchEntire(this) ?: error("Invalid: $this")
    val x = match.groupValues[1].toInt()
    val m = match.groupValues[2].toInt()
    val a = match.groupValues[3].toInt()
    val s = match.groupValues[4].toInt()
    return MachinePart(x, m, a, s)
}


typealias RuleTest = (MachinePart) -> Boolean

data class Rule(
    val test: RuleTest,
    val target: String,
    val ruleDef: String
)

data class Workflow(
    val name: String,
    val rules: List<Rule>
)

private val workflowRegex = """(\w+)\{(.+)\}""".toRegex()
private val ruleRegex = """((\w+)([<>])(\d+):)?(\w+)""".toRegex()

private fun String.parseWorkflow(): Workflow {
    val w = workflowRegex.matchEntire(this) ?: error(this)
    val name = w.groupValues[1]
    val rules = w
        .groupValues[2]
        .splitToSequence(",")
        .map { ruleRegex.matchEntire(it)?.groupValues ?: error(it) }
        .map {
            //x>10:one
            val target = it[5]
            if (it[2].isNotEmpty()) {
                val getter = it[2].partGetter()
                val n = it[4].toInt()
                val def = it[1].dropLast(1)
                val test: RuleTest = when (it[3]) {
                    ">" -> {
                        { part -> getter(part) > n }
                    }

                    "<" -> {
                        { part -> getter(part) < n }
                    }

                    else -> error(it[3])
                }
                Rule(test, target, def)
            } else {
                val test: RuleTest = { true }
                Rule(test, target, "")
            }
        }.toList()
    return Workflow(name, rules)
}

class WorkflowEngine(
    private val workflows: List<Workflow>
) {
    private val workflowsByName: Map<String, Workflow> = workflows.associateBy { it.name }

    val accepted: MutableList<MachinePart> = mutableListOf()
    val rejected: MutableList<MachinePart> = mutableListOf()

    fun process(part: MachinePart) {
        process(part, "in")
    }

    private tailrec fun process(part: MachinePart, workflowName: String) {
        val workflow = workflowsByName[workflowName] ?: error("Unknown workflow: $workflowName")
        val target = workflow.rules.first { it.test(part) }.target
        when (target) {
            "A" -> {
                accepted.add(part)
            }

            "R" -> {
                rejected.add(part)
            }

            else -> {
                process(part, target)
            }
        }
    }

}

data class PartRange(
    val x: IntRange = 1..4000,
    val m: IntRange = 1..4000,
    val a: IntRange = 1..4000,
    val s: IntRange = 1..4000,
) {

    val size: Long by lazy {
        x.size() * m.size() * a.size() * s.size()
    }

    fun updateX(f: (IntRange) -> IntRange): PartRange {
        return this.copy(x = f(x))
    }

    fun updateM(f: (IntRange) -> IntRange): PartRange {
        return this.copy(m = f(m))
    }

    fun updateA(f: (IntRange) -> IntRange): PartRange {
        return this.copy(a = f(a))
    }

    fun updateS(f: (IntRange) -> IntRange): PartRange {
        return this.copy(s = f(s))
    }
}

private val conditionRegex = """(\w+)([<>])(\d+)""".toRegex()

data class Condition(
    val rule: String,
    val negate: Boolean = false
) {

    private val match = conditionRegex.matchEntire(rule) ?: error(rule)

    private val attribute = match.groupValues[1]
    private val operation = match.groupValues[2]
    private val value = match.groupValues[3].toInt()

    fun adapt(partRange: PartRange): PartRange {
        val f: (IntRange) -> IntRange = when (operation) {
            ">" -> {
                if (negate) {
                    { it.atMost(value) }
                } else {
                    { it.atLeast(value + 1) }
                }
            }

            "<" -> {
                if (negate) {
                    { it.atLeast(value) }
                } else {
                    { it.atMost(value - 1) }
                }
            }

            else -> error("Unknown operation $operation")
        }
        return when (attribute) {
            "x" -> partRange.updateX(f)
            "m" -> partRange.updateM(f)
            "a" -> partRange.updateA(f)
            "s" -> partRange.updateS(f)
            else -> error("Unknown attribute $attribute")
        }
    }
}

private fun IntRange.atLeast(n: Int): IntRange {
    return this.first.coerceAtLeast(n)..this.last
}

private fun IntRange.atMost(n: Int): IntRange {
    return this.first..this.last.coerceAtMost(n)
}

private fun buildConditions(
    from: String,
    current: List<Condition>,
    reverseIndex: Map<String, List<String>>,
    workflowsByName: Map<String, Workflow>
): Sequence<List<Condition>> {
    val origins = reverseIndex[from] ?: return sequenceOf(current)
    return origins
        .asSequence()
        .flatMap { origin ->
            val workflow = workflowsByName[origin] ?: error("Unknown workflow: $origin")
            conditionsForWorkflow(workflow, from, current, origin, reverseIndex, workflowsByName)
        }
}

private fun conditionsForWorkflow(
    workflow: Workflow,
    from: String,
    current: List<Condition>,
    origin: String,
    reverseIndex: Map<String, List<String>>,
    workflowsByName: Map<String, Workflow>
): Sequence<List<Condition>> {
    val i = workflow.rules.indexOfFirst { it.target == from }
    assert(i > -1) { "Workflow ${workflow.name} doesn't have a rule targeting $from" }
    val negations = workflow
        .rules
        .subList(0, i)
        .map { Condition(it.ruleDef, true) }

    val assertion = workflow.rules[i].ruleDef.takeIf { it.isNotBlank() }?.let { Condition(it) }
    val newConditions = current + negations + (assertion?.let { listOf(it) } ?: listOf())
    return buildConditions(origin, newConditions, reverseIndex, workflowsByName)
}

private fun buildRange(conditions: List<Condition>): PartRange {
    return conditions.fold(PartRange()) { acc, condition -> condition.adapt(acc) }
}

object Day19 {

    data class Input(
        val workflows: List<Workflow>,
        val parts: List<MachinePart>
    )

    private fun parseInput(input: String): Input {
        val s = input.split("\n\n")
        val workflows = s[0].splitToSequence("\n")
            .map { it.parseWorkflow() }
            .toList()
        val parts = s[1].splitToSequence("\n")
            .filter { it.isNotBlank() }
            .map { it.parseMachinePart() }
            .toList()
        return Input(workflows, parts)
    }

    fun solve1(input: String): Int {
        val (workflows, parts) = parseInput(input)
        val engine = WorkflowEngine(workflows)
        parts.forEach { engine.process(it) }
        return engine.accepted.sumOf { it.totalRating }
    }

    fun solve2(input: String): Long {
        val (workflows, parts) = parseInput(input)
        val reverseIndex = workflows
            .flatMap { w ->
                w.rules.map { rule -> rule.target to w.name }
            }.groupBy { it.first }
            .mapValues { (_, values) -> values.map { it.second } }
        val workflowsByName: Map<String, Workflow> = workflows.associateBy { it.name }
        val conditions = buildConditions("A", listOf(), reverseIndex, workflowsByName).toList()
        val ranges = conditions
            .map { buildRange(it) }
            .distinct()
            .toList()
        return ranges.sumOf { it.size }
    }

}
