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
}