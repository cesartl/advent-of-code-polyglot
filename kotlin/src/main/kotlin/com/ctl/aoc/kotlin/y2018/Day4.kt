package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.head
import com.ctl.aoc.kotlin.utils.tail
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.regex.Pattern

sealed class LogEntry {
    abstract val timestamp: LocalDateTime
}

data class GuardBeginsShift(val id: Int, override val timestamp: LocalDateTime) : LogEntry()
data class GuardFallsAsleep(override val timestamp: LocalDateTime) : LogEntry()
data class GuardWakesUp(override val timestamp: LocalDateTime) : LogEntry()

data class GuardLog(val id: Int, val entries: List<LogEntry>) {
    fun appendEntry(entry: LogEntry): GuardLog = this.copy(entries = this.entries + entry)
}


data class SleepLog(val start: LocalDateTime, val end: LocalDateTime) {
    fun minutes(): Sequence<Int> = sequence {
        var current = start
        while (current.isBefore(end)) {
            yield(current.minute)
            current = current.plusMinutes(1)
        }
    }
}

data class GuardSleepLog(val id: Int, val sleepLogs: List<SleepLog>) {
    fun minutesisAsleep(): Long = sleepLogs.map { it.start.until(it.end, ChronoUnit.MINUTES) }.sum()
}

data class SleepLogBuilder(val previous: GuardFallsAsleep?, val sleepLogs: List<SleepLog>)

fun buildGuardSleepLog(guardLog: GuardLog): GuardSleepLog {
    for (i in guardLog.entries.indices) {
    }

    val sleepLogs = guardLog.entries.fold(SleepLogBuilder(null, emptyList())) { builder, entry ->
        when (entry) {
            is GuardFallsAsleep -> builder.copy(previous = entry)
            is GuardWakesUp -> {
                val asleep: GuardFallsAsleep = builder.previous!!
                builder.copy(sleepLogs = builder.sleepLogs + SleepLog(start = asleep.timestamp, end = entry.timestamp))
            }
            else -> throw IllegalArgumentException(entry.toString())
        }
    }.sleepLogs
    return GuardSleepLog(guardLog.id, sleepLogs)

}

val SHIFT_START_PATTERN = Pattern.compile("\\[(.*)\\] Guard #([\\d]+) begins shift")
val ASLEEP_PATTERN = Pattern.compile("\\[(.*)\\] falls asleep")
val WAKES_PATTERN = Pattern.compile("\\[(.*)\\] wakes up")

//1518-11-01 00:00
val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

object Day4 {
    fun parseGuardLog(s: String): LogEntry {
        val m1 = SHIFT_START_PATTERN.matcher(s)
        if (m1.matches()) {
            return GuardBeginsShift(m1.group(2).toInt(), LocalDateTime.parse(m1.group(1), DATE_FORMAT))
        }
        val m2 = ASLEEP_PATTERN.matcher(s)
        if (m2.matches()) {
            return GuardFallsAsleep(LocalDateTime.parse(m2.group(1), DATE_FORMAT))
        }
        val m3 = WAKES_PATTERN.matcher(s)
        if (m3.matches()) {
            return GuardWakesUp(LocalDateTime.parse(m3.group(1), DATE_FORMAT))
        }
        throw IllegalArgumentException(s)
    }

    fun solve1(lines: Sequence<String>): Int {
        val logs = lines.map { parseGuardLog(it) }.sortedBy { it.timestamp }
        val guardLogs = logs.fold(listOf<GuardLog>()) { list, entry ->
            //            println("doing $entry")
            when (entry) {
                is GuardBeginsShift -> if (list.isEmpty()) listOf(GuardLog(entry.id, listOf())) else list + listOf(GuardLog(entry.id, listOf()))
                else -> list.dropLast(1) + listOf(list.last().appendEntry(entry))
            }
        }.asSequence()

        val guardSleepLogs = guardLogs.map { buildGuardSleepLog(it) }.asSequence()

        val totalSleep = guardSleepLogs.fold(mapOf<Int, Long>()) { map, e ->
            map + (e.id to e.minutesisAsleep() + (map[e.id] ?: 0))
        }

        val longestGuardAsleep = totalSleep.maxBy { it.value }?.key!!

        val minuteCounts = guardSleepLogs.filter { it.id == longestGuardAsleep }.flatMap { g -> g.sleepLogs.asSequence().flatMap { it.minutes() } }
                .groupingBy { it }.eachCount()

        val mostMinute = minuteCounts.maxBy { it.value }?.key!!
        return mostMinute * longestGuardAsleep
    }

    fun solve2(lines: Sequence<String>): Int {
        val logs = lines.map { parseGuardLog(it) }.sortedBy { it.timestamp }
        val guardLogs = logs.fold(listOf<GuardLog>()) { list, entry ->
            //            println("doing $entry")
            when (entry) {
                is GuardBeginsShift -> if (list.isEmpty()) listOf(GuardLog(entry.id, listOf())) else list + listOf(GuardLog(entry.id, listOf()))
                else -> list.dropLast(1) + listOf(list.last().appendEntry(entry))
            }
        }.asSequence()

        val guardSleepLogs = guardLogs.map { buildGuardSleepLog(it) }.asSequence()

        var x = guardSleepLogs.groupBy { it.id }.mapValues { it.value.flatMap { g -> g.sleepLogs.asSequence().flatMap { it.minutes() }.toList() }.groupingBy { it }.eachCount() }

        println(x)

        val y = x.mapValues { it.value.maxBy { it.value }?.toPair() }
        println(y)

        val z = y.maxBy { it.value?.second ?: 0 }!!.toPair()
        println(z)


        return z.first * (z.second?.first ?:0)

    }
}