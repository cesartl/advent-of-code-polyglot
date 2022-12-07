package com.ctl.aoc.kotlin.y2022

object Day7 {

    sealed class Node {
        abstract val name: String
        abstract val size: Long
        abstract val parent: Dir?

        data class File(override val name: String, override val size: Long, override val parent: Dir?) : Node()
        data class Dir(
            override val name: String,
            override val parent: Dir?,
            val nodes: MutableList<Node> = mutableListOf()
        ) : Node() {
            override val size: Long by lazy {
                nodes.asSequence().map { it.size }.sum()
            }

            override fun toString(): String {
                return "Dir(name='$name', parent=$parent)"
            }
        }
    }

    sealed class Terminal {
        data class Cd(val dir: String) : Terminal()
        object Ls : Terminal()

        data class FileEntry(val size: Long, val name: String) : Terminal()
        data class DirEntry(val name: String) : Terminal()
    }

    private val cdRegex = """\$ cd ([\w/\\.]+)""".toRegex()
    private val lsRegex = """\$ ls""".toRegex()
    private val dirRegex = """dir (\w+)""".toRegex()
    private val fileRegex = """(\d+) ([\w\\.]+)""".toRegex()

    private fun String.parseEntry(): Terminal {
        cdRegex.matchEntire(this)?.let {
            return Terminal.Cd(it.groupValues[1])
        }
        lsRegex.matchEntire(this)?.let {
            return Terminal.Ls
        }
        dirRegex.matchEntire(this)?.let {
            return Terminal.DirEntry(it.groupValues[1])
        }
        fileRegex.matchEntire(this)?.let {
            val size = it.groupValues[1].toLong()
            val name = it.groupValues[2]
            return Terminal.FileEntry(size, name)
        }
        error(this)
    }

    fun buildTree(input: Sequence<String>): Node {
        val root = Node.Dir("/", null)
        var currentDir: Node.Dir = root
        input.map { it.parseEntry() }.forEach { terminal ->
            when (terminal) {
                is Terminal.Cd -> {
                    val dir = terminal.dir
                    currentDir = when (dir) {
                        ".." -> {
                            currentDir.parent ?: root
                        }

                        "/" -> {
                            root
                        }

                        else -> {
                            currentDir
                                .nodes
                                .filterIsInstance<Node.Dir>()
                                .firstOrNull { it.name == dir } ?: error("$dir not found")
                        }
                    }
                }

                is Terminal.DirEntry -> {
                    val dir = Node.Dir(terminal.name, currentDir)
                    currentDir.nodes.add(dir)
                }

                is Terminal.FileEntry -> {
                    val file = Node.File(terminal.name, terminal.size, currentDir)
                    currentDir.nodes.add(file)
                }

                Terminal.Ls -> {

                }
            }
        }
        return root
    }

    fun Node.dirs(): Sequence<Node.Dir> {
        return when (this) {
            is Node.Dir -> sequence {
                yield(this@dirs)
                this@dirs.nodes.asSequence()
                    .flatMap { it.dirs() }
                    .let { yieldAll(it) }
            }

            is Node.File -> sequenceOf()
        }
    }

    fun solve1(input: Sequence<String>): Long {
        val root = buildTree(input)
        return root.dirs()
            .filter { it.size <= 100000 }
            .map { it.size }
            .sum()
    }

    fun solve2(input: Sequence<String>): Long {
        val root = buildTree(input)
        val free = 70000000L - root.size
        println("free space $free")
        val target = 30000000 - free
        println("target $target")
        return root.dirs()
            .map { it.size }
            .sorted()
            .first { it >= target }
    }
}
