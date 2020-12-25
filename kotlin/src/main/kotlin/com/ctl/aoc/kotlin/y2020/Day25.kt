package com.ctl.aoc.kotlin.y2020

object Day25 {
    fun solve1(input: Sequence<String>): Long {
        val publicKey1 = input.first().toLong()
        val publicKey2 = input.drop(1).first().toLong()
        var secretLoop = 0
        val subject = 7L
        var value = 1L
        while (value != publicKey1) {
            value = transformStep(value, subject)
            secretLoop++
        }
        println(secretLoop)
        val secretKey = transform(subject = publicKey2, loop = secretLoop)
        return secretKey
    }

    tailrec fun transform(value: Long = 1L, subject: Long, loop: Int): Long {
        return if (loop == 0) {
            value
        } else {
            transform(transformStep(value, subject), subject, loop - 1)
        }
    }

    fun transformStep(value: Long, subject: Long): Long {
        val v = value * subject
        return v % 20201227L
    }
}