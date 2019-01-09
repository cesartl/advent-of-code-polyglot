package com.ctl.aoc.kotlin.y2015

object Day11 {

    fun increment(s: String): String {
        val x = s.foldRightIndexed(StringBuilder() to 1) { index, c, (builder, carry) ->
            if (carry == 0) {
                return s.substring(0, index + 1) + builder.reverse().toString()
            }
            val tmp = c - 'a' + carry
            val newChar = (tmp % ('z' - 'a' + 1) + 'a'.toInt()).toChar()
            builder.append(newChar)
            builder to (tmp / ('z' - 'a' + 1))
        }
        if (x.second == 1) {
//            x.first.append('a')
        }
        return x.first.reverse().toString()
    }

    val consequtiveTripple: Sequence<String> = sequence {
        for (i in 'a'.toInt()..'z'.toInt()) {
            val b = StringBuilder()
            b.append(i.toChar())
            b.append((i + 1).toChar())
            b.append((i + 2).toChar())
            yield(b.toString())
        }
    }

    val doubleRegex = """([\w])\1""".toRegex()
    fun doubles(password: String): Set<String> = doubleRegex.findAll(password).map { it.groupValues[1] }.toSet()


    fun isPasswordValid(password: String): Boolean {
        return consequtiveTripple.any { password.contains(it) } &&
                listOf('i', 'o', 'l').all { !password.contains(it) } &&
                doubles(password).size > 1
    }

    fun solve1(password: String): String {
        val sequence = generateSequence(password) { increment(it) }.filter { isPasswordValid(it) }
        return sequence.first()
    }

    fun solve2(password: String): String {
        val sequence = generateSequence(password) { increment(it) }.filter { isPasswordValid(it) }
        return sequence.drop(1).first()
    }
}