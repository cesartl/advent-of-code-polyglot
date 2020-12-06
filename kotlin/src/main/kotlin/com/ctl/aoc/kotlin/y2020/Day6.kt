package com.ctl.aoc.kotlin.y2020

object Day6 {

    data class Group(val answers: List<Set<String>>) {
        val anyAnswered by lazy{
            answers.fold(setOf<String>()) { acc, set -> acc + set }
        }
        val allAnswered by lazy {
            answers.fold(anyAnswered) { acc, set -> acc.intersect(set) }
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val groups = parseGroups(input)
        return groups.map { it.anyAnswered.size }.sum()
    }

    private fun parseGroups(input: Sequence<String>): List<Group> {
        val (groups, last) = input.fold(listOf<Group>() to listOf<Set<String>>()) { (groups, group), line ->
            if (line.isEmpty()) {
                (groups + Group(group)) to listOf<Set<String>>()
            } else {
                groups to group + listOf(parseAnswers(line))
            }
        }
        val g = groups + Group(last)
        return g
    }

    fun parseAnswers(s: String): Set<String> {
        return s.fold(mutableSetOf<String>()) { set, c ->
            set.add(c.toString())
            set
        }
    }

    fun solve2(input: Sequence<String>): Int {
        val groups = parseGroups(input)
        return groups.map { it.allAnswered.size }.sum()
    }
}