package com.ctl.aoc.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.PriorityQueue;

public class JavaPriorityQueue<T> implements MinPriorityQueue<T> {

    private static class Node<T> implements Comparable<Node<T>> {
        final T element;
        final long priority;

        public Node(T element, long priority) {
            this.element = element;
            this.priority = priority;
        }

        @Override
        public int compareTo(@NotNull Node<T> o) {
            return (int) (priority - o.priority);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(element, node.element);
        }

        @Override
        public int hashCode() {
            return Objects.hash(element);
        }
    }

    private final PriorityQueue<Node<T>> queue = new PriorityQueue<>();

    private Node<T> findNode(T element) {
        return queue.stream().filter(e -> e.element == element).findFirst().orElse(null);
    }

    @Override
    public T findMinimum() {
        return queue.peek().element;
    }

    @Override
    public T extractMinimum() {
        return queue.poll().element;
    }

    @Override
    public void merge(MinPriorityQueue<? extends T> queue) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void insert(T element, long priority) {
        queue.add(new Node<>(element, priority));
    }

    @Override
    public void decreasePriority(T element, long priority) {
        delete(element);
        this.insert(element, priority);
    }

    @Override
    public void delete(T element) {
        queue.removeIf(n -> n.element == element);
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean contains(T element) {
        return queue.contains(new Node<>(element, 0));
    }
}
