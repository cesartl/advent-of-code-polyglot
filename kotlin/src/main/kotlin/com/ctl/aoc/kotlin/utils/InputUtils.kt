package com.ctl.aoc.kotlin.utils

import java.io.InputStream

object InputUtils {

    fun getStream(fileName: String): InputStream = this.javaClass.classLoader.getResourceAsStream(fileName)

    fun getString(fileName: String): String = getStream(fileName).bufferedReader().use { it.readText() }

    fun getStream(year: String, name: String): InputStream = this.javaClass.classLoader.getResourceAsStream("y$year/$name")

    fun getString(year: String, name: String): String = getStream(year, name).bufferedReader().use { it.readText() }

    fun getString(year: Int, day: Int): String = getStream(year.toString(), "day$day.txt").bufferedReader().use { it.readText() }

    fun getLines(year: String, name: String): Sequence<String> = getStream(year, name).bufferedReader().use { it.readLines().asSequence() }

    fun getLines(year: Int, day: Int): Sequence<String> = getLines(year.toString(), "day$day.txt")

}