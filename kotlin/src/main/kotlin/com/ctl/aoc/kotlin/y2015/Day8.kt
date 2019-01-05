package com.ctl.aoc.kotlin.y2015

object Day8 {
    tailrec fun countChar(string: String, idx: Int = 0, count: Int = 0): Int {
        return when {
            idx >= string.length -> {
                println("$string $count")
                count
            }
            string[idx] == '"' -> countChar(string, idx + 1, count)
            string.substring(idx).startsWith("""\x""") -> countChar(string, idx + 4, count + 1)
            string.substring(idx).startsWith("""\""") -> countChar(string, idx + 2, count + 1)
            else -> countChar(string, idx + 1, count + 1)
        }
    }

    private val specialCharacters = setOf('"', '\\')

    fun encode(s: String): String {
        val e = s.fold("\"") { acc, c ->
            when {
                specialCharacters.contains(c) -> "$acc\\$c"
                else -> acc + c
            }
        }
        return e + "\""
    }

    fun solve1(lines: Sequence<String>): Int {
        return lines.map { it.length - countChar(it) }.sum()
    }

    fun solve2(lines: Sequence<String>): Int = lines.map { encode(it).length - it.length }.sum()
}