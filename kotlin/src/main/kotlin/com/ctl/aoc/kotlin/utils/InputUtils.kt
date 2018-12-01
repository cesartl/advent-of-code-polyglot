package com.ctl.aoc.kotlin.utils

import java.io.InputStream

object InputUtils {

    fun getStream(year: String, name: String): InputStream = this.javaClass.classLoader.getResourceAsStream("y$year/$name")

    fun getString(year: String, name: String): String = getStream(year, name).bufferedReader().use { it.readText() }

    fun getLines(year: String, name: String): Sequence<String> = getStream(year, name).bufferedReader().use { it.readLines().asSequence() }
}