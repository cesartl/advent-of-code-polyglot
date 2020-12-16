package com.ctl.aoc.kotlin.y2020

object Day16 {

    fun solve1(input: String): Int {
        val (_, otherTickets, rules) = Input.parse(input)
        return otherTickets.flatMap { it.values }
                .filter { value -> !rules.any { it.valueAllowed(value) } }
                .sum()
    }

    fun solve2(input: String): Long {
        val (myTicket, otherTickets, rules) = Input.parse(input)

        val validTickets = otherTickets.filter { ticket -> ticket.isValid(rules) }

        val initialState = SearchState.build(rules, validTickets)
        val finalState = initialState.search()

        val fieldValues = finalState.fields.map { it as Field.Known }.mapIndexed { idx, f -> f.name to myTicket.values[idx] }.toMap()
        println(fieldValues)
        return fieldValues.filterKeys { it.contains("departure") }.map { it.value.toLong() }.fold(1L) { acc, n -> acc * n }
    }

    data class SearchState(val fields: List<Field>) {
        val knownFields = fields.filterIsInstance(Field.Known::class.java).map { it.name }.toSet()

        companion object {
            fun build(rules: List<Rule>, validTickets: List<Ticket>): SearchState {
                val rulesByName = rules.map { it.fieldName to it }.toMap()
                val initialFields = validTickets.first().values.map { Field.Unknown(rulesByName.keys.toList()) }
                val newFields = initialFields.mapIndexed { idx, field ->
                    val actualValues = validTickets.map { it.values[idx] }
                    val filtered = field.possibleNames.filter { ruleName ->
                        val rule = rulesByName[ruleName] ?: error("")
                        actualValues.all { rule.valueAllowed(it) }
                    }
                    Field.Unknown(filtered)
                }
                return SearchState(newFields)
            }
        }
    }

    private tailrec fun SearchState.search(): SearchState {

        val newFields = fields.map { field ->
            when (field) {
                is Field.Known -> field
                is Field.Unknown -> Field.Unknown(field.possibleNames.filter { !knownFields.contains(it) })
            }
        }.map { field ->
            when (field) {
                is Field.Unknown -> if (field.possibleNames.size == 1) Field.Known(field.possibleNames.first()) else field
                is Field.Known -> field
            }
        }

        return if (newFields == fields) {
            if (fields.any { it is Field.Unknown }) {
                throw Error("Unresolved $fields")
            }
            return this
        } else {
            SearchState(newFields).search()
        }
    }

    sealed class Field {
        data class Known(val name: String) : Field()
        data class Unknown(val possibleNames: List<String>) : Field()
    }

    data class Ticket(val values: List<Int>) {
        fun isValid(rules: List<Rule>): Boolean {
            return values.all { value -> rules.any { it.valueAllowed(value) } }
        }
    }

    data class Rule(val fieldName: String, val ranges: List<IntRange>) {
        fun valueAllowed(value: Int): Boolean = ranges.any { it.contains(value) }

        companion object {
            val regex = """([\w ]+): ([\d]+)-([\d]+) or ([\d]+)-([\d]+)""".toRegex()
            fun parse(s: String): Rule {
                val m = regex.matchEntire(s) ?: throw Error(s)
                val values = m.groupValues
                val ranges = listOf(values[2].toInt()..values[3].toInt(), values[4].toInt()..values[5].toInt())
                return Rule(values[1], ranges)
            }
        }
    }

    data class Input(val myTicket: Ticket, val otherTickets: List<Ticket>, val rules: List<Rule>) {
        companion object {
            fun parse(input: String): Input {
                val parts = input.split("\n\n").toList()
                val rules = parts[0].split("\n").map { Rule.parse(it) }

                val myTicket = Ticket(parts[1].split("\n")[1].split(",").map { it.toInt() })

                val otherTickets = parts[2].split("\n").drop(1).map { Ticket(it.split(",").map { s -> s.toInt() }) }
                return Input(myTicket, otherTickets, rules)
            }
        }
    }

}