package com.ctl.aoc.kotlin.y2020

object Day19 {

    fun solve1(input: String): Int {
        val (rules, messages) = Input.parse(input)

        val rulesById = rules.map { it.id to it }.toMap()

        val rule0 = evaluateRule(rulesById[0] ?: error(""), mutableMapOf(), rulesById)
        return messages.count { rule0.matchRegex(it) }
    }

    fun solve1Bis(input: String): Int {
        val (rules, messages) = Input.parse(input)

        val rulesById = rules.map { it.id to it }.toMap()

        return messages.count { isMatch(it, listOf(rulesById.getValue(0)), rulesById) }
    }

    fun solve2Bis(input: String): Int {
        val (rules, messages) = Input.parse(input)

        val rulesById = rules.map { it.id to it }.toMap() +
                listOf(8 to Rule.SubRule(8, listOf(listOf(42), listOf(42, 8))), 11 to Rule.SubRule(11, listOf(listOf(42, 31), listOf(42, 11, 31)))).toMap()

        return messages.count { isMatch(it, listOf(rulesById.getValue(0)), rulesById) }
    }

    fun solve2(input: String): Int {
        val (rules, messages) = Input.parse(input)

        val rulesById = rules.map { it.id to it }.toMap().toMutableMap()
        //8: 42 | 42 8
        //11: 42 31 | 42 11 31
        rulesById[8] = Rule.SubRule(8, listOf(listOf(42), listOf(42, 888)))
        rulesById[11] = Rule.SubRule(11, listOf(listOf(42, 31), listOf(42, 999, 31)))
        rulesById[888] = Rule.FixedRule(888, "K")
        rulesById[999] = Rule.FixedRule(999, "L")

        val evaluatedRules = mutableMapOf<Int, EvaluatedRule>()
        val rule0 = evaluateRule(rulesById[0] ?: error(""), evaluatedRules, rulesById)

        var baseRegex = rule0.regexPattern

        val repeatK = evaluatedRules[8]?.let { it.regexPattern } ?: error("")
        val repeatL = evaluatedRules[11]?.let { it.regexPattern } ?: error("")

        var count = 0
        var prevCount = -1
        var regexPattern = ""
        var i = 0
        while (count != prevCount) {
            prevCount = count
            regexPattern = baseRegex.replace("L", "").replace("K", "K")
            val regex = regexPattern.toRegex()
            count = messages.count { it.matches(regex) }

            baseRegex = baseRegex.replace("K", repeatK).replace("L", repeatL)
            i++
        }
        println("Stopped after $i iterations")
        return count
    }

    fun isMatch(message: String, rules: List<Rule>, rulesById: Map<Int, Rule>): Boolean {
        return when {
            message.isEmpty() -> {
                rules.isEmpty()
            }
            rules.isEmpty() -> {
                false
            }
            else -> {
                return when (val firstRule = rules.first()) {
                    is Rule.FixedRule -> {
                        if (message.startsWith(firstRule.r)) {
                            isMatch(message.drop(1), rules.drop(1), rulesById)
                        } else {
                            false
                        }
                    }
                    is Rule.SubRule -> firstRule.subRules.any { subRule ->
                        isMatch(message, subRule.map { rulesById.getValue(it) } + rules.drop(1), rulesById)
                    }
                }
            }
        }
    }

    sealed class Rule {
        abstract val id: Int

        data class FixedRule(override val id: Int, val r: String) : Rule()
        data class SubRule(override val id: Int, val subRules: List<List<Int>>) : Rule()
    }

    data class EvaluatedRule(val id: Int, val regexPattern: String = "") {

        val pattern by lazy {
            regexPattern.toRegex()
        }

        fun matchRegex(message: String) = message.matches(pattern)
    }


    fun evaluateRule(rule: Rule, evaluatedRules: MutableMap<Int, EvaluatedRule>, rulesById: Map<Int, Rule>): EvaluatedRule {
        val cached = evaluatedRules[rule.id]
        if (cached != null) {
            return cached
        }
        val evaluated = when (rule) {
            is Rule.FixedRule -> {
                EvaluatedRule(rule.id, rule.r)
            }
            is Rule.SubRule -> {
                val subRules = rule.subRules.map { ruleIds ->
                    ruleIds.map {
                        val cached = evaluatedRules[it]
                        if (cached == null) {
                            val subRule = rulesById[it] ?: error("Could not find rule id $it")
                            evaluateRule(subRule, evaluatedRules, rulesById)
                        } else {
                            cached
                        }
                    }
                }
                val regexPattern = subRules.joinToString(prefix = "(", postfix = ")", separator = "|") { rule -> rule.joinToString(prefix = "", postfix = "", separator = "") { it.regexPattern } }
                EvaluatedRule(rule.id, regexPattern)
            }
        }
        evaluatedRules[evaluated.id] = evaluated
        return evaluated
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