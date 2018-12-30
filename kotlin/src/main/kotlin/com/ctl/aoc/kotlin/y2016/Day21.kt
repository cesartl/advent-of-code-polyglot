package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.y2016.Day21.Scrambler.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object Day21 {

    interface ScramblerI {
        fun scramble(string: String): String
        fun reverse(string: String): String
    }

    sealed class Scrambler : ScramblerI {

        data class SwapPosition(val x: Int, val y: Int) : Scrambler() {
            override fun reverse(string: String): String = scramble(string)

            override fun scramble(string: String): String {
                val min = Math.min(x, y)
                val max = Math.max(x, y)
                return string.substring(0, min) + string[max] + string.substring(min + 1, max) + string[min] + string.substring(max + 1)
            }
        }

        data class SwapLetter(val x: Char, val y: Char) : Scrambler() {
            override fun reverse(string: String): String = scramble(string)

            override fun scramble(string: String): String {
                return string.replace(x, '~').replace(y, x).replace('~', y)
            }
        }

        data class Rotate(val steps: Int) : Scrambler() {
            override fun reverse(string: String): String = this.copy(steps = -steps).scramble(string)

            override fun scramble(string: String): String {
                val n = ((steps % string.length) + string.length) % string.length
                return string.substring(string.length - n) + string.substring(0, string.length - n)
            }
        }

        data class RotateLetter(val letter: Char) : Scrambler() {
            override fun reverse(string: String): String {
                var scramble = ""
                var reverse = ""
                var i = 0
                while (scramble != string) {
                    reverse = Rotate(-i).scramble(string)
                    scramble = this.scramble(reverse)
                    i++
                }
                return reverse
            }

            override fun scramble(string: String): String {
                val idx = string.indexOf(letter)
                val n = (1 + idx + if (idx >= 4) 1 else 0) % string.length
                return string.substring(string.length - n) + string.substring(0, string.length - n)
            }
        }

        data class Reverse(val x: Int, val y: Int) : Scrambler() {
            override fun reverse(string: String): String = scramble(string)

            override fun scramble(string: String): String {
                val min = Math.min(x, y)
                val max = Math.max(x, y)
                return string.substring(0, min) + string.substring(min, max + 1).reversed() + string.substring(max + 1)
            }
        }

        data class Move(val x: Int, val y: Int) : Scrambler() {
            override fun reverse(string: String): String = copy(x = y, y = x).scramble(string)

            override fun scramble(string: String): String {
                return when {
                    x < y -> string.substring(0, x) + string.substring(x + 1, y + 1) + string[x] + string.substring(y + 1)
                    y < x -> string.substring(0, y) + string[x] + string.substring(y, x) + string.substring(x + 1)
                    else -> string
                }
            }
        }
    }

    enum class Parser {
        SwapPositionP {
            override val pattern: Pattern
                get() = Pattern.compile("swap position ([\\d]+) with position ([\\d]+)")

            override fun fromMatcher(matcher: Matcher): ScramblerI {
                return SwapPosition(matcher.group(1).toInt(), matcher.group(2).toInt())
            }
        },
        SwapLetterP {
            override val pattern: Pattern
                get() = Pattern.compile("swap letter ([a-z]+) with letter ([a-z]+)")

            override fun fromMatcher(matcher: Matcher): ScramblerI {
                return SwapLetter(matcher.group(1)[0], matcher.group(2)[0])
            }
        },
        RotateP {
            override val pattern: Pattern
                get() = Pattern.compile("rotate (right|left) ([\\d]+) steps?")

            override fun fromMatcher(matcher: Matcher): ScramblerI {
                val sign = if (matcher.group(1) == "right") 1 else -1
                return Rotate(matcher.group(2).toInt() * sign)
            }
        },
        RotateLetterP {
            override val pattern: Pattern
                get() = Pattern.compile("rotate based on position of letter ([a-z]+)")

            override fun fromMatcher(matcher: Matcher): ScramblerI {
                return RotateLetter(matcher.group(1)[0])
            }
        },
        ReverseP {
            override val pattern: Pattern
                get() = Pattern.compile("reverse positions ([\\d]+) through ([\\d]+)")

            override fun fromMatcher(matcher: Matcher): ScramblerI {
                return Reverse(matcher.group(1).toInt(), matcher.group(2).toInt())
            }
        },
        MoveP {
            override val pattern: Pattern
                get() = Pattern.compile("move position ([\\d]+) to position ([\\d]+)")

            override fun fromMatcher(matcher: Matcher): ScramblerI {
                return Move(matcher.group(1).toInt(), matcher.group(2).toInt())
            }
        }
        ;

        abstract val pattern: Pattern
        abstract fun fromMatcher(matcher: Matcher): ScramblerI
    }


    fun parse(s: String): ScramblerI {
        val (m, p) = Parser.values().asSequence().map { it.pattern.matcher(s) to it }.find { it.first.matches() }
                ?: throw IllegalArgumentException(s)
        return p.fromMatcher(m)
    }

    private fun scramble(password: String, scramblers: Sequence<ScramblerI>): String {
        return scramblers.fold(password) { acc, scramblerI -> scramblerI.scramble(acc) }
    }

    private fun deScramble(scrambled: String, scramblers: List<ScramblerI>): String {
        return scramblers.foldRight(scrambled) { scramblerI, acc -> scramblerI.reverse(acc) }
    }

    fun solve1(lines: Sequence<String>, password: String): String {
        val scramblers = lines.map { parse(it) }
        return scramble(password, scramblers)
    }

    fun solve2(lines: Sequence<String>, scrambed: String): String {
        val scramblers = lines.map { parse(it) }.toList()
        return deScramble(scrambed, scramblers)
    }

}