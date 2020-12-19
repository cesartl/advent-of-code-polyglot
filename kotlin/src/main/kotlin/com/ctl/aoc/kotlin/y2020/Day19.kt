package com.ctl.aoc.kotlin.y2020

object Day19 {

    fun solve1(input: String): Int {
        val (rules, messages) = Input.parse(input)

        val rulesById = rules.map { it.id to it }.toMap()

        val rule0 = evaluateRule(rulesById[0] ?: error(""), mutableMapOf(), rulesById)
        return messages.count { rule0.patternsSet.contains(it) }
    }

    fun solve2(input: String): Int {
        TODO()
    }

    sealed class Rule {
        abstract val id: Int

        data class FixedRule(override val id: Int, val r: String) : Rule()
        data class SubRule(override val id: Int, val subRules: List<List<Int>>) : Rule()
    }

    data class EvaluatedRule(val id: Int, val patterns: List<String>) {
        val patternsSet by lazy {
            patterns.toSet()
        }
    }


    private fun evaluateRule(rule: Rule, evaluatedRules: MutableMap<Int, EvaluatedRule>, rulesById: Map<Int, Rule>): EvaluatedRule {
        val cached = evaluatedRules[rule.id]
        if (cached != null) {
            return cached
        }
        val evaluated = when (rule) {
            is Rule.FixedRule -> {
                EvaluatedRule(rule.id, listOf(rule.r))
            }
            is Rule.SubRule -> {
                val subRules = rule.subRules.map { ruleIds ->
                    ruleIds.map {
                        val subRule = rulesById[it] ?: error("Could not find rule id $it")
                        evaluateRule(subRule, evaluatedRules, rulesById)
                    }
                }
                val patterns = subRules.flatMap { allPatterns(subRules = it).toList() }
                EvaluatedRule(rule.id, patterns)
            }
        }
        evaluatedRules[evaluated.id] = evaluated
        return evaluated
    }

    fun allPatterns(prefix: String = "", subRules: List<EvaluatedRule>): Sequence<String> = sequence {
        if (subRules.isEmpty()) {
            yield(prefix)
        } else {
            val rule = subRules.first()
            rule.patterns.forEach { pattern ->
                yieldAll(allPatterns(prefix + pattern, subRules.drop(1)))
            }

        }
    }

    data class Input(val rules: List<Rule>, val messages: List<String>) {
        companion object {
            fun parse(input: String): Input {
                val s = input.split("\n\n")

                val rules: List<Rule> = s[0].split("\n").map { line ->
                    val p1 = line.split(":")
                    val ruleId = p1[0].toInt()
                    if (p1[1].contains('"')) {
                        val r = p1[1].substringAfter('"').substringBefore('"')
                        Rule.FixedRule(ruleId, r)
                    } else {
                        val subRules = p1[1].trim().split("|").map { s -> s.trim().split(" ").map { it.toInt() } }
                        Rule.SubRule(ruleId, subRules)
                    }
                }
                return Input(rules, s[1].split("\n"))
            }
        }
    }
}