package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TreeKtTest {
    @Test
    internal fun testBalanced() {
        val t1 = BinaryTree(
                1,
                BinaryTree(
                        2,
                        Tree.binaryTreeLeaf(3),
                        Tree.binaryTreeLeaf(4)
                ),
                BinaryTree(
                        5,
                        Tree.binaryTreeLeaf(6),
                        Tree.binaryTreeLeaf(7)
                )
        )

        val t2 = BinaryTree(
                1,
                BinaryTree(
                        2,
                        Tree.binaryTreeLeaf(3),
                        Tree.binaryTreeLeaf(4)
                ),
                BinaryTree(
                        5,
                        Tree.binaryTreeLeaf(6),
                        null
                )
        )

        val t3 = BinaryTree(
                1,
                BinaryTree(
                        2,
                        Tree.binaryTreeLeaf(3),
                        Tree.binaryTreeLeaf(4)
                ),
                BinaryTree(
                        5,
                        null,
                        null
                )
        )

        val t4 = BinaryTree(
                1,
                BinaryTree(
                        2,
                        Tree.binaryTreeLeaf(3),
                        Tree.binaryTreeLeaf(4)
                ),
                null
        )

        assertTrue(t1.isBalanced())
        println(t1.checkHeight())
        assertTrue(t2.isBalanced())
        println(t2.checkHeight())
        assertTrue(t3.isBalanced())
        println(t3.checkHeight())
        assertFalse(t4.isBalanced())
        println(t4.checkHeight())
    }

    @Test
    internal fun testBST() {
        val t1 = BinaryTree(
                8,
                BinaryTree(
                        4,
                        Tree.binaryTreeLeaf(2),
                        Tree.binaryTreeLeaf(6)),
                BinaryTree(
                        10,
                        null,
                        Tree.binaryTreeLeaf(20))
        )

        val t2 = BinaryTree(
                8,
                BinaryTree(
                        4,
                        Tree.binaryTreeLeaf(2),
                        Tree.binaryTreeLeaf(12)),
                BinaryTree(
                        10,
                        null,
                        Tree.binaryTreeLeaf(20))
        )

        assertTrue(t1.isBST())
        assertFalse(t2.isBST())
    }

    @Test
    internal fun testCommonAncestor() {
        val t1 = BinaryTree(
                1,
                BinaryTree(
                        2,
                        Tree.binaryTreeLeaf(3),
                        Tree.binaryTreeLeaf(4)
                ),
                BinaryTree(
                        5,
                        Tree.binaryTreeLeaf(6),
                        Tree.binaryTreeLeaf(7)
                )
        )

        assertEquals(5, t1.firstCommonAncestor(6, 7)?.value)
        assertEquals(2, t1.firstCommonAncestor(3, 4)?.value)
        assertEquals(1, t1.firstCommonAncestor(7, 4)?.value)
        assertEquals(5, t1.firstCommonAncestor(5, 6)?.value)
    }

    @Test
    internal fun testBstSequence() {
        val t1 = BinaryTree(2, Tree.binaryTreeLeaf(1), Tree.binaryTreeLeaf(3))
        println(t1.bstSequence())


        val t2 = BinaryTree(8,
        BinaryTree(
                4,
                Tree.binaryTreeLeaf(2),
                Tree.binaryTreeLeaf(6)),
        BinaryTree(
                16,
                Tree.binaryTreeLeaf(9),
                Tree.binaryTreeLeaf(32))
        )

        val t2Seqs = t2.bstSequence()
        println(t2Seqs.size)
        println(t2Seqs.distinct().size)

        val x = listOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(1, 2, 3))
        println(x.distinct())
    }
}