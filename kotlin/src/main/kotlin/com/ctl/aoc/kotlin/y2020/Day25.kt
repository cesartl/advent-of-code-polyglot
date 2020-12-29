package com.ctl.aoc.kotlin.y2020

import java.math.BigInteger

object Day25 {
    fun solve1(input: Sequence<String>, startOffset: Int = 0 ): Long {
        val publicKey1 = input.first().toLong()
        val publicKey2 = input.drop(1).first().toLong()
        var secretLoop = startOffset
        val subject = 7L
        var value = transform(subject, startOffset)
        while (value != publicKey1) {
            value = transformStep(value, subject)
            secretLoop++
        }
        println(secretLoop)
        val secretKey = transform(subject = publicKey2, loop = secretLoop)
        return secretKey
    }

    fun transform(subject: Long, loop: Int): Long {
        return BigInteger.valueOf(subject).modPow(loop.toBigInteger(), 20201227L.toBigInteger()).toLong()
    }

    fun transformStep(value: Long, subject: Long): Long {
        val v = value * subject
        return v % 20201227L
    }
}