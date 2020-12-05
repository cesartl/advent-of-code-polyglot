package com.ctl.aoc.kotlin.y2020

object Day4 {
    fun solve1(input: Sequence<String>): Int {
        val fields = parseFields(input)

        return fields.filter {
            it.containsKey("byr")
                    && it.containsKey("iyr")
                    && it.containsKey("eyr")
                    && it.containsKey("hgt")
                    && it.containsKey("hcl")
                    && it.containsKey("ecl")
                    && it.containsKey("pid")
        }.count()
    }

    val hgtRegex = """([0-9]+)(cm|in)""".toRegex()
    val hairColourRegex = """#[0-9a-f]{6}""".toRegex()

    val validators = mapOf<String, (String) -> Boolean>(
            "byr" to { s -> s.all { c -> c.isDigit() } && s.toInt() in (1920..2002) },
            "iyr" to { s -> s.all { c -> c.isDigit() } && s.toInt() in (2010..2020) },
            "eyr" to { s -> s.all { c -> c.isDigit() } && s.toInt() in (2020..2030) },
            "hgt" to { s ->
                hgtRegex.matchEntire(s)?.let { match ->
                    val height = match.groupValues[1].toInt()
                    if (match.groupValues[2] == "cm") {
                        height in (150..193)
                    } else {
                        height in (59..76)
                    }

                } ?: false
            },
            "hcl" to { s -> s.matches(hairColourRegex) },
            "ecl" to { s -> setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(s) },
            "pid" to { s -> s.all { c -> c.isDigit() } && s.length == 9 }
    )

    fun solve2(input: Sequence<String>): Int {
        val fields = parseFields(input)
        return fields.filter {
            it.containsKey("byr")
                    && it.containsKey("iyr")
                    && it.containsKey("eyr")
                    && it.containsKey("hgt")
                    && it.containsKey("hcl")
                    && it.containsKey("ecl")
                    && it.containsKey("pid")
                    && it.all { entry ->
                validate(entry.key, entry.value)
            }
        }.count()
    }

    fun validate(field: String, value: String): Boolean {
        return validators[field]?.let { validator ->
            validator(value)
        } ?: true
    }

    private fun parseFields(input: Sequence<String>): List<Map<String, String>> {
        val (full, last) = input.fold(listOf<String>() to "") { (acc, current), line ->
            if (line.isEmpty()) {
                (acc + current) to ""
            } else {
                acc to ("$current$line ")
            }
        }
        val entries = full + last
        return entries.map { entry ->
            entry.trim().split(" ").map { field ->
                val s = field.split(":")
                s[0] to s[1]
            }.toMap()
        }
    }
}