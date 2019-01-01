package com.ctl.aoc.kotlin.utils

import java.math.BigInteger
import java.security.MessageDigest

object Strings {
    fun permutations(s: String): List<String> {
        if (s.length == 1) return listOf(s)
        val next = permutations(s.substring(1))
        val result = mutableListOf<String>()
        next.forEach {
            for (i in 0..it.length) {
                result.add(it.substring(0, i) + s[0] + it.substring(i))
            }
        }
        return result
    }

    fun md5(s: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(s.toByteArray())).toString(16).padStart(32, '0')
    }
}