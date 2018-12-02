package com.ctl.aoc.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Cesar Tron-Lozai on 13/03/2016.
 */
public class FibonacciHeap<T> implements MinPriorityQueue<T> {

    private final LinkedList<Node<T>> roots = new LinkedList<>();
    private final Map<T, Node<T>> nodeMap = new HashMap<>();
    private Node<T> minimum;
    private int size = 0;


    @Override
    public T findMinimum() {
        if (minimum != null) {
            return minimum.element;
        }
        return null;
    }

    @Override
    public T extractMinimum() {
        if (minimum != null) {
            //remove minimum node from the list of root trees
            if (!roots.remove(minimum)) {
                throw new IllegalStateException("Minimum pointer not in the list of roots");
            }
            nodeMap.remove(minimum.element);
            //all children all of the minimum become new root trees
//            minimum.children.forEach(this::addToRoot);
            for (Node<T> child : minimum.children) {
                child.parent = null;
                roots.add(child);
                if (child.priority < minimum.priority) {
                    minimum = child;
                }
            }


            final T oldMinimum = minimum.element;

            balanceHeap();

            return oldMinimum;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void balanceHeap() {
        final int n = Long.valueOf(Math.round(Math.log(size))).intValue() + 2;
        //array to store the new roots by the number of children
        final Object[] rootsByDegree = new Object[size];

        Node<T> root = roots.poll();
        while (root != null) {
            if (rootsByDegree[root.numberOfChildren()] == null) {
                //if no other root having the same amount of children, we add it to the array
                rootsByDegree[root.numberOfChildren()] = root;
                //this node is complete, we can go to the next one
                root = roots.poll();
            } else {
                //if there is already a node we merge them, keeping the one with the lowest priority
                final Node<T> otherRoot = (Node<T>) rootsByDegree[root.numberOfChildren()];
                rootsByDegree[root.numberOfChildren()] = null;
                if (root.priority >= otherRoot.priority) {
                    otherRoot.children.add(root);
                    root.parent = otherRoot;
                    root = otherRoot;
                } else {
                    root.children.add(otherRoot);
                    otherRoot.parent = root;
                }
            }
        }

        minimum = null;
        //we add the new roots and search for the minimum
        for (int i = 0; i < rootsByDegree.length; i++) {
            final Node<T> newRoot = (Node<T>) rootsByDegree[i];
            if (newRoot != null) {
                roots.add(newRoot);
                if (minimum == null || newRoot.priority < minimum.priority) {
                    minimum = newRoot;
                }
            }
        }
    }

    @Override
    public void merge(MinPriorityQueue<? extends T> queue) {
        @SuppressWarnings("unchecked")
        final FibonacciHeap<T> otherFibonacci = (FibonacciHeap<T>) queue;
        this.roots.addAll(otherFibonacci.roots);
        this.nodeMap.putAll(otherFibonacci.nodeMap);
        size += otherFibonacci.size;
    }

    @Override
    public void insert(T element, long priority) {
        final Node<T> newNode = new Node<>(element, priority, null);
        if (minimum == null || newNode.priority < minimum.priority) {
            minimum = newNode;
        }
        roots.add(newNode);
        nodeMap.put(element, newNode);
        size++;
    }

    @Override
    public void decreasePriority(T element, long priority) {
        final Node<T> node = nodeMap.get(element);
        if (node == null) {
            throw new IllegalStateException("Could not find node for element: " + node);
        }
        node.priority = priority;
        if (node.parent == null) {
            if (node.priority < minimum.priority) {
                minimum = node;
            }
        } else if (node.priority < node.parent.priority) {
            //order violated, we move the node to the root
            Node<T> parent = node.parent;
            addToRoot(node);
            node.unMark();
            if (parent.isMarked()) {
                while (parent != null && parent.isMarked()) {
                    final Node<T> newParent = parent.parent;
                    parent.unMark();
                    addToRoot(parent);
                    parent = newParent;
                }
            } else {
                if (parent.parent != null) {
                    parent.mark();
                }
            }
        }
    }

    private void addToRoot(Node<T> node) {
        if (node.parent != null) {
            node.parent.children.remove(node);
            node.parent = null;
            roots.add(node);
        }
        if (node.priority < minimum.priority) {
            minimum = node;
        }

    }

    @Override
    public void delete(T element) {
        if (nodeMap.containsKey(element)) {
            decreasePriority(element, Long.MIN_VALUE);
            extractMinimum();
        }
    }

    @Override
    public boolean isEmpty() {
        return roots.isEmpty();
    }

    @Override
    public boolean contains(T element) {
        return nodeMap.containsKey(element);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (Node<T> root : roots) {
            builder.append(root.toString(minimum));
        }
        return builder.toString();
    }

    private static class Node<T> {
        private final T element;
        private long priority;
        private final LinkedList<Node<T>> children = new LinkedList<>();
        private boolean marked = false;
        private Node<T> parent;

        public Node(T element, long priority, Node<T> parent) {
            this.element = element;
            this.priority = priority;
        }


        public boolean isMarked() {
            return marked;
        }

        public void mark() {
            marked = true;
        }

        public void unMark() {
            marked = false;
        }

        public int numberOfChildren() {
            return children.size();
        }


        @Override
        public String toString() {
            return toString("\t", null);
        }

        public String toString(Node<T> minimum) {
            return toString("\t", minimum);
        }

        private String toString(String prefix, Node<T> minimum) {
            final StringBuilder builder = new StringBuilder();
            builder.append(prefix)
                    .append("[")
                    .append(element)
                    .append(" (")
                    .append(priority)
                    .append(")")
                    .append("]");
            if (marked) {
                builder.append("*");
            }
            if (this == minimum) {
                builder.append("-");
            }
            if (parent != null) {
                builder.append(" p->")
                        .append(parent.element);
            }

            builder.append("\n");
            final String newPrefix = prefix + "\t";
            for (int i = 0; i < children.size(); i++) {
                builder.append(children.get(i).toString(newPrefix, minimum));
            }
            return builder.toString();
        }
    }
}
