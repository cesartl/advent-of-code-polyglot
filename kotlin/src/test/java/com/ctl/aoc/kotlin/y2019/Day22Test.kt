package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2019.Day22.ShuffleTechnique.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day22Test {

    val puzzleInput = InputUtils.getLines(2019, 22)

    val example1 = """deal with increment 7
deal into new stack
deal into new stack""".splitToSequence('\n')

    val example2 = """cut 6
deal with increment 7
deal into new stack""".splitToSequence('\n')

    val example3 = """deal with increment 7
deal with increment 9
cut -2""".splitToSequence('\n')

    val example4 = """deal into new stack
cut -2
deal with increment 7
cut 8
cut -4
deal with increment 7
cut 3
deal with increment 9
deal with increment 3
cut -1""".splitToSequence('\n')

    @Test
    internal fun dealNewTest() {
        val deck = sequenceOf(0, 1, 2, 3, 4)
        assertThat(DealNew.shuffle(deck).toList())
                .containsExactlyElementsOf(listOf(4, 3, 2, 1, 0))
    }

    @Test
    internal fun cutTest() {
        val deck = sequenceOf(0, 1, 2, 3, 4)
        assertThat(Cut(3).shuffle(deck).toList())
                .containsExactlyElementsOf(listOf(3, 4, 0, 1, 2))

        val t = Day22.ShuffleTechnique.parse("cut -4", 10)
        val deck10 = (0..9).asSequence()
        assertThat(t.shuffle(deck10).toList())
                .isEqualTo("6 7 8 9 0 1 2 3 4 5".split(" ").map { it.toInt() }.toList())
    }

    @Test
    internal fun testDeal() {
        val deck = (0..9).asSequence()
        assertThat(Deal(3).shuffle(deck).toList())
                .containsExactlyElementsOf(listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3))

        println((35 - 10007) % ( 10007))
        val deck2 = (0..10006).asSequence()
        println(Deal(35).shuffle(deck2).toList())
        println((35 * 3431) % 10007)
        println((35 * 3431) % 10007)
        println((35 * 6862) % 10007)
    }

    @Test
    internal fun testLogic() {
        val deck = (0..9).asSequence()
        val e1 = Day22.apply(deck, Day22.parse(example1, 10)).toList()
        assertThat(e1).isEqualTo(listOf(0, 3, 6, 9, 2, 5, 8, 1, 4, 7))

        val e2 = Day22.apply(deck, Day22.parse(example2, 10)).toList()
        assertThat(e2).isEqualTo("3 0 7 4 1 8 5 2 9 6".split(" ").map { it.toInt() }.toList())

        val e3 = Day22.apply(deck, Day22.parse(example3, 10)).toList()
        assertThat(e3).isEqualTo("6 3 0 7 4 1 8 5 2 9".split(" ").map { it.toInt() }.toList())

        val e4 = Day22.apply(deck, Day22.parse(example4, 10)).toList()
        assertThat(e4)
                .isEqualTo(listOf(9, 2, 5, 8, 1, 4, 7, 0, 3, 6))
    }

    @Test
    fun solve1() {
        assertThat(Day22.solve1(puzzleInput)).isEqualTo(6831)
        assertThat(Day22.solve1Bis(puzzleInput)).isEqualTo(6831)
    }

    @Test
    internal fun solve2() {
        val n = 119315717514047L //119,315,717,514,047
        val m = 101741582076661L //101,741,582,076,661
        println(Day22.solve2(puzzleInput))
    }
}