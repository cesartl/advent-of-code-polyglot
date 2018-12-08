package com.ctl.aoc.kotlin.y2018

object Day8 {
    data class TreeNode(val id: String = "", val metaData: List<Int>, val children: List<TreeNode>)

    data class ParsingInfo(val treeNode: TreeNode, val offset: Int)

    fun parseTree(sequence: List<Int>): ParsingInfo {
        val nChildren = sequence[0]
        val nMetaData = sequence[1]
        var child: ParsingInfo
        var offset = 2
        val children = mutableListOf<TreeNode>()
        for (i in 0..(nChildren -1)) {
            child = parseTree(sequence.drop(offset))
            children.add(child.treeNode)
            offset += child.offset
        }
        val metaData = sequence.subList(offset, offset + nMetaData)
        return ParsingInfo(TreeNode(metaData = metaData, children = children), offset + nMetaData)
    }

    fun sumMetadata(treeNode: TreeNode): Int {
        return treeNode.metaData.sum() + treeNode.children.map { sumMetadata(it) }.sum()
    }

    fun treeValue(treeNode: TreeNode): Int {
        return if(treeNode.children.isEmpty()){
            treeNode.metaData.sum()
        }else{
            treeNode.metaData.asSequence().filter { it  <= treeNode.children.size }.map { it -> treeNode.children[it - 1] }.map { treeValue(it) }.sum()
        }
    }

    fun solve1(input: String): Int {
        val list = input.split(" ").map { it.toInt() }
        val root = parseTree(list)
        return sumMetadata(root.treeNode)
    }


    fun solve2(input: String): Int {
        val list = input.split(" ").map { it.toInt() }
        val root = parseTree(list)
        return treeValue(root.treeNode)
    }
}