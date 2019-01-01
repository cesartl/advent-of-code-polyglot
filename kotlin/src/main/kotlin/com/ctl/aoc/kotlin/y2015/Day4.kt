package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Strings

object Day4 {
    fun solve1(key: String): Int {
        var hash: String
        var i = 0
        do {
            i++
            hash = Strings.md5("$key$i")
        } while (!hash.startsWith("00000"))
        return i
    }

    fun solve2(key: String): Int {
        var hash: String
        var i = 0
        do {
            i++
            hash = Strings.md5("$key$i")
        } while (!hash.startsWith("000000"))
        return i
    }
}