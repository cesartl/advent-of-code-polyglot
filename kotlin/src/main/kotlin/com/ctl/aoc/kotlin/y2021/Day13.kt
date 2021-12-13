package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position

object Day13 {

    sealed class Fold {
        data class XFold(val x: Int) : Fold()
        data class YFold(val y: Int) : Fold()
    }

    fun Position.fold(fold: Fold): Position {
        return when (fold) {
            is Fold.XFold -> {
                if (x > fold.x) {
                    copy(x = fold.x - (x - fold.x))
                } else {
                    this
                }
            }
            is Fold.YFold -> {
                if (y > fold.y) {
                    copy(y = fold.y - (y - fold.y))
                } else {
                    this
                }
            }
        }
    }

    data class Paper(val dots: Set<Position>) {
        private val bottomRight: Position by lazy { Position(dots.maxByOrNull { it.x }!!.x, dots.maxByOrNull { it.y }!!.y) }

        fun fold(fold: Fold): Paper {
            return Paper(dots = dots.map { it.fold(fold) }.toSet())
        }

        fun foldAll(folds: List<Fold>): Paper {
            return folds.fold(this) { paper, fold -> paper.fold(fold) }
        }

        fun print() {
            (0..bottomRight.y).forEach { y ->
                (0..bottomRight.x).forEach { x ->
                    print(if (dots.contains(Position(x, y))) "#" else ".")
                }
                println()
            }
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val (paper, folds) = parse(input)
        val folded = paper.fold(folds.first())
        return folded.dots.size
    }

    fun solve2(input: Sequence<String>) {
        val (paper, folds) = parse(input)
        paper.foldAll(folds).print()
    }

    private val foldRegex = """fold along [xy]=([\d]+)""".toRegex()
    fun parse(lines: Sequence<String>): Pair<Paper, List<Fold>> {
        val dots = mutableSetOf<Position>()
        val folds = mutableListOf<Fold>()
        lines.forEach { line ->
            when {
                line.contains(",") -> {
                    val (x, y) = line.split(",")
                    dots.add(Position(x.toInt(), y.toInt()))
                }
                line.contains("x") -> {
                    val x = foldRegex.matchEntire(line)!!.groupValues[1].toInt()
                    folds.add(Fold.XFold(x))
                }
                line.contains("y") -> {
                    val y = foldRegex.matchEntire(line)!!.groupValues[1].toInt()
                    folds.add(Fold.YFold(y))
                }
            }
        }
        return Paper(dots) to folds
    }
}