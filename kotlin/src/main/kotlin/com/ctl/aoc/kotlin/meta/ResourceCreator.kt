package com.ctl.aoc.kotlin.meta

import com.ctl.aoc.kotlin.utils.InputUtils
import java.io.File

object ResourceCreator {

    private const val ROOT = "/Users/cesar/repos/advent-of-code-polyglot/kotlin"

    fun createResources(year: Int){
        (1 until 26).forEach { day -> createResources(year, day) }
    }

    private fun createResources(year: Int, day: Int){
        createObjectFile(year, day)
        createTestFile(year, day)
        createInputFile(year, day)
    }

    private fun createObjectFile(year: Int, day: Int) {
        val content = InputUtils.getStream("meta/object.template").bufferedReader().use { it.readText() }
        val replaced = content.replace("_YEAR_", year.toString())
                .replace("_DAY_", day.toString())

        val directory = File("$ROOT/src/main/kotlin/com/ctl/aoc/kotlin/y$year")
        if(!directory.exists()){
            directory.mkdirs();
        }
        val objectFile = File(directory, "Day$day.kt")
        if(!objectFile.exists()) {
            objectFile.writeText(replaced, Charsets.UTF_8)
            println("Written ${objectFile.canonicalPath}")
        }else{
            println("${objectFile.canonicalPath} already exists")
        }
    }

    private fun createTestFile(year: Int, day: Int) {
        val content = InputUtils.getStream("meta/test.template").bufferedReader().use { it.readText() }
        val replaced = content.replace("_YEAR_", year.toString())
                .replace("_DAY_", day.toString())

        val directory = File("$ROOT/src/test/java/com/ctl/aoc/kotlin/y$year")
        if(!directory.exists()){
            directory.mkdirs();
        }
        val testFile = File(directory, "Day${day}Test.kt")
        if(!testFile.exists()) {
            testFile.writeText(replaced, Charsets.UTF_8)
            println("Written ${testFile.canonicalPath}")
        }else{
            println("${testFile.canonicalPath} already exists")
        }
    }

    private fun createInputFile(year: Int, day: Int){
        val directory = File("$ROOT/src/main/resources/y$year")
        if(!directory.exists()){
            directory.mkdirs();
        }
        val inputFile = File(directory, "day${day}.txt")
        if(!inputFile.exists()) {
            inputFile.writeText("", Charsets.UTF_8)
            println("Written ${inputFile.canonicalPath}")
        }else{
            println("${inputFile.canonicalPath} already exists")
        }
    }
}