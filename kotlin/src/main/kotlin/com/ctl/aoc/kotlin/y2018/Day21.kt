package com.ctl.aoc.kotlin.y2018

object Day21 {
    fun solve1(): Int {
        var r4 = 0
        var r3 = 0
        var r5 = 0
        var r1 = 0

        r1 = 65536 or r4
        r4 = 16031208

        do {
            r3 = r1 and 255
            r4 += r3
            r4 = r4 and 16777215
            r4 *= 65899
            r4 = r4 and 16777215

            if (r1 < 256) {
                return r4
            }
            r3 = 0

            do {
                r5 = r3 + 1
                r5 *= 256
                r3 += 1
            } while (r5 <= r1)
            r1 = r3 - 1
        } while (true)
    }

    fun solve2(): Set<Int> {
        var r4 = 0
        var r3: Int
        var r5: Int
        var r1: Int

        var r4s = mutableSetOf<Int>()

        do {
            r1 = 65536 or r4
            r4 = 16031208

            do {
                r3 = r1 and 255
                r4 += r3
                r4 = r4 and 16777215
                r4 *= 65899
                r4 = r4 and 16777215

                if (r1 < 256) {
                    if (r4 <= 0 || r4s.contains(r4)) {
                        return r4s
                    } else {
                        println("adding $r4 (${r4s.size})")
                        r4s.add(r4)
                        break
                    }

                }
                r3 = 0
                do {
                    r5 = r3 + 1
                    r5 *= 256
                    r3 += 1
                } while (r5 <= r1)
                r1 = r3 - 1
            } while (true)
        } while (true)
    }
}