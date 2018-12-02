package com.ctl.aoc.kotlin.utils


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
}

fun <T> BinaryTree<T>.checkHeight(): Int = Tree.checkHeight(this, 0)
fun <T> BinaryTree<T>.isBalanced(): Boolean = this.checkHeight() >= 0
fun BinaryTree<Int>.isBST(): Boolean = Tree.validateBST(this, Int.MIN_VALUE, Int.MAX_VALUE)