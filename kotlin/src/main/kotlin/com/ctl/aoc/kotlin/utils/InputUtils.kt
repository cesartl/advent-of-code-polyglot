package com.ctl.aoc.kotlin.utils

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

object InputUtils {

    private val inputPath: Path by lazy {
        val root = Paths.get("").toAbsolutePath().toString()
        Paths.get(root, "src/main/resources")
    }

    private val secrets: Map<String, String> by lazy {
        this.javaClass.classLoader.getResourceAsStream("secret.properties").use { secrets ->
            val properties = Properties()
            properties.load(secrets)
            properties.toMap()
        } as Map<String, String>
    }

    private fun resourcePath(year: Int, day: Int): Path {
        return Paths.get(inputPath.toAbsolutePath().toString(), "y$year", "day$day.txt")
    }

    private fun getStream(year: Int, day: Int): InputStream {
        return Files.newInputStream(resourcePath(year, day))
    }

    fun getStream(fileName: String): InputStream = this.javaClass.classLoader.getResourceAsStream(fileName)

    fun getString(fileName: String): String = getStream(fileName).bufferedReader().use { it.readText() }

    private fun getStream(year: String, name: String): InputStream =
        this.javaClass.classLoader.getResourceAsStream("y$year/$name")

    fun getString(year: String, name: String): String = getStream(year, name).bufferedReader().use { it.readText() }

    fun getString(year: Int, day: Int): String = getStream(year, day).bufferedReader().use { it.readText() }

    fun getLines(year: String, name: String): Sequence<String> =
        getStream(year, name).bufferedReader().use { it.readLines().asSequence() }

    fun getLines(year: Int, day: Int): Sequence<String> =
       getAllLines(year, day).filter { it.isNotBlank() }

    private fun getAllLines(year: Int, day: Int): Sequence<String> =
        getStream(year, day).bufferedReader().use { it.readLines().asSequence() }

    fun downloadAndGetLines(year: Int, day: Int): Sequence<String> {
        downloadInputIfNeeded(year, day)
        return getLines(year, day)
    }

    fun downloadAndGetAllLines(year: Int, day: Int): Sequence<String> {
        downloadInputIfNeeded(year, day)
        return getAllLines(year, day)
    }


    fun downloadAndGetString(year: Int, day: Int): String {
        downloadInputIfNeeded(year, day)
        return getString(year, day)
    }

    private fun downloadInputIfNeeded(year: Int, day: Int) {
        val resourcePath = resourcePath(year, day)
        if (!Files.exists(resourcePath)) {
            println("Input for y$year d$day doesn't exist. Downloading from adventofcode.com")
            //https://adventofcode.com/2021/day/1/input3
            val url = "https://adventofcode.com/$year/day/$day/input"
            val cookies = secrets.filterKeys { it == "session" }
            DownloadUtils.downloadFile(url, cookies).use { input ->
                resourcePath.toFile().outputStream().use { output ->
                    val bytes = input.transferTo(output)
                    println("Downloaded $bytes bytes to ${resourcePath.toAbsolutePath()}")
                }
            }
        }
    }

}
