package com.ctl.aoc.kotlin.y2019

object Day4 {


    fun solve1(from: Int, to: Int): Int {
        return (from..to).asSequence().map { it.toString() }.filter { isPasswordCorrect(it) }.count()
    }

    fun solve2(from: Int, to: Int): Int {
        return (from..to).asSequence().map { it.toString() }.filter { isPasswordCorrect2(it) }.count()
    }

    val adjacentRegex = """([0-9])\1""".toRegex()

    val adjacentRegexLargeGroup = """([0-9])(\1+)""".toRegex()


    fun allDigitIngreasing(password: String): Boolean {
        return password.asSequence()
                .mapIndexed { index, c -> index to c }
                .all { (index, c) -> index == (password.length - 1) || c <= password[index + 1] }
    }

    fun isPasswordCorrect(password: String): Boolean {
        return adjacentRegex.containsMatchIn(password)
                && allDigitIngreasing(password)
    }

    fun duplicateWithoutLargeGroup(s: String): Boolean {
        val find = adjacentRegexLargeGroup.find(s);
        if (find != null) {
            val matchGroup = find.groups[2]!!
            val ok = matchGroup.value.length == 1
            return if (ok) true else duplicateWithoutLargeGroup(s.substring(matchGroup.range.last))
        }
        return false
    }

    fun isPasswordCorrect2(password: String): Boolean {
        return duplicateWithoutLargeGroup(password)
                && allDigitIngreasing(password)
    }
}