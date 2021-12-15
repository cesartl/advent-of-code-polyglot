package com.ctl.aoc.util;

/**
 * Created by Cesar Tron-Lozai on 13/03/2016.
 */
public interface MinPriorityQueueInt<T> {
    T findMinimum();

    T extractMinimum();

    void merge(MinPriorityQueueInt<? extends T> queue);

    void insert(T element, int priority);

    void decreasePriority(T element, int priority);

    void delete(T element);

    boolean isEmpty();

    boolean contains(T element);
}
