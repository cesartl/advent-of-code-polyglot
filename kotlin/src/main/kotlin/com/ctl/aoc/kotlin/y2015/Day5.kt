package com.ctl.aoc.kotlin.y2015

import java.util.regex.Pattern

object Day5 {

    enum class Requirement {

        Vowels {
            private val pattern = Pattern.compile("[aeiou]")
            override fun isValid(s: String): Boolean = pattern.matcher(s).results().count() >= 3
        },

        Twin {
            private val pattern = Pattern.compile("([a-z])\\1")
            override fun isValid(s: String): Boolean = pattern.matcher(s).find()
        },

        Banned {
            private val pattern = Pattern.compile("ab|cd|pq|xy")
            override fun isValid(s: String): Boolean = !pattern.matcher(s).find()
        };

        abstract fun isValid(s: String): Boolean
    }

    enum class Requirement2 {

        Twin {
            private val pattern = Pattern.compile("([a-z][a-z])[a-z]*\\1")
            override fun isValid(s: String): Boolean = pattern.matcher(s).find()
        },

        Repeat {
            private val pattern = Pattern.compile("([a-z])[a-z]\\1")
            override fun isValid(s: String): Boolean = pattern.matcher(s).find()
        };

        abstract fun isValid(s: String): Boolean
    }

    fun isValid(string: String) = Requirement.values().all { it.isValid(string) }
    fun isValid2(string: String) = Requirement2.values().all { it.isValid(string) }

    fun solve1(lines: Sequence<String>): Int = lines.count { isValid(it) }
    fun solve2(lines: Sequence<String>): Int = lines.count { isValid2(it) }
}