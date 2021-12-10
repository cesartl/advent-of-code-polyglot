package com.ctl.aoc.kotlin.utils

import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object InputUtils {

    private val secrets: Map<String, String> by lazy {
        this.javaClass.classLoader.getResourceAsStream("secret.properties").use { secrets ->
            val properties = Properties()
            properties.load(secrets)
            properties.toMap()
        } as Map<String, String>
    }

    private fun getStream(year: Int, day: Int): InputStream {
        return secrets["inputFolder"]
                ?.let { FileInputStream("$it/y$year/day$day.txt") }
                ?: error("Missing inputFolder")
    }

    fun getStream(fileName: String): InputStream = this.javaClass.classLoader.getResourceAsStream(fileName)

    fun getString(fileName: String): String = getStream(fileName).bufferedReader().use { it.readText() }

    private fun getStream(year: String, name: String): InputStream = this.javaClass.classLoader.getResourceAsStream("y$year/$name")

    fun getString(year: String, name: String): String = getStream(year, name).bufferedReader().use { it.readText() }

    fun getString(year: Int, day: Int): String = getStream(year, day).bufferedReader().use { it.readText() }

    fun getLines(year: String, name: String): Sequence<String> = getStream(year, name).bufferedReader().use { it.readLines().asSequence() }

    fun getLines(year: Int, day: Int): Sequence<String> = getStream(year, day).bufferedReader().use { it.readLines().asSequence() }.filter { it.isNotBlank() }

    fun downloadAndGetLines(year: Int, day: Int): Sequence<String> {
        downloadInputIfNeeded(year, day)
        return getLines(year, day)
    }

    fun downloadAndGetString(year: Int, day: Int): String{
        downloadInputIfNeeded(year, day)
        return getString(year, day)
    }

    private fun downloadInputIfNeeded(year: Int, day: Int) {
        val inputPath = Paths.get(secrets["inputFolder"]
                ?: error("Missing inputFolder secret"), "y$year", "day$day.txt")
        if (!Files.exists(inputPath)) {
            println("Input for y$year d$day doesn't exist. Downloading from adventofcode.com")
            //https://adventofcode.com/2021/day/1/input3
            val url = "https://adventofcode.com/$year/day/$day/input"
            val cookies = secrets.filterKeys { it == "session" }
            DownloadUtils.downloadFile(url, cookies).use { input ->
                inputPath.toFile().outputStream().use { output ->
                    val bytes = input.transferTo(output)
                    println("Downloaded $bytes bytes to ${inputPath.toAbsolutePath()}")
                }
            }
        }
    }

}