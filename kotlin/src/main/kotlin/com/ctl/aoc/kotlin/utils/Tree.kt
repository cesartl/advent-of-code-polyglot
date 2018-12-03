package com.ctl.aoc.kotlin.utils

import java.lang.IllegalArgumentException


data class BinaryTree<T>(val value: T, val left: BinaryTree<T>?, val right: BinaryTree<T>?)

object Tree {

    fun <T> binaryTreeLeaf(value: T): BinaryTree<T> = BinaryTree(value, null, null)

    fun <T> checkHeight(node: BinaryTree<T>?, height: Int): Int {
        if (node == null) return -1
        else {
            val left = checkHeight(node.left, height)
            if (left == Int.MIN_VALUE) return Int.MIN_VALUE
            val right = checkHeight(node.right, height)
            if (right == Int.MIN_VALUE) return -Int.MIN_VALUE
            if (Math.abs(right - left) > 1) return Int.MIN_VALUE
            return Math.max(right, left) + 1
        }
    }

    fun validateBST(node: BinaryTree<Int>?, min: Int, max: Int): Boolean {
//        println("Node ${node?.value} min: $min max: $max")
        if (node == null) return true
        if (node.value < min || node.value > max) return false
        val newMin = Math.max(node.value, min)
        val newMax = Math.min(node.value, max)
        return validateBST(node.left, min, newMax) && validateBST(node.right, newMin, max)
    }

    fun <T> firstCommonAncestor(root: BinaryTree<T>?, p: T, q: T): BinaryTree<T>? {
        if (root == null) return null
        if (root.value == p && root.value == q) return root
        val x = firstCommonAncestor(root.left, p, q)
        if (x != null && x.value != p && x.value != q) {
            return x
        }
        val y = firstCommonAncestor(root.right, p, q)
        if (y != null && y.value != p && y.value != q) {
            return y
        }
        return if (x != null && y != null) {
            root
        } else if (root.value == p || root.value == q) {
            root
        } else {
            x ?: y
        }
    }

    fun <T> bstSequence(root: BinaryTree<T>?): List<List<T>> {
        if (root == null) return listOf()
        val allLeft = bstSequence(root.left)
        val allRight = bstSequence(root.right)
        val result = mutableListOf<List<T>>()

        for (left in allLeft) {
            for (right in allRight) {
                result.addAll(Lists.weave(left, right).map { listOf(root.value) + it })
            }
        }

        if (result.isEmpty()) {
            result.add(listOf(root.value))
        }

        return result
    }
}

fun <T> BinaryTree<T>.checkHeight(): Int = Tree.checkHeight(this, 0)
fun <T> BinaryTree<T>.isBalanced(): Boolean = this.checkHeight() >= 0
fun BinaryTree<Int>.isBST(): Boolean = Tree.validateBST(this, Int.MIN_VALUE, Int.MAX_VALUE)
fun <T> BinaryTree<T>.firstCommonAncestor(p: T, q: T): BinaryTree<T>? = Tree.firstCommonAncestor(this, p, q)
fun BinaryTree<Int>.bstSequence(): List<List<Int>> {
    if (!this.isBST()) throw IllegalArgumentException("not a bst")
    return Tree.bstSequence(this)
}