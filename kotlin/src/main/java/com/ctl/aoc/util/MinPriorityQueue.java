package com.ctl.aoc.util;

/**
 * Created by Cesar Tron-Lozai on 13/03/2016.
 */
public interface MinPriorityQueue<T> {
    T findMinimum();

    T extractMinimum();

    void merge(MinPriorityQueue<? extends T> queue);

    void insert(T element, long priority);

    void decreasePriority(T element, long priority);

    void delete(T element);

    boolean isEmpty();

    boolean contains(T element);
}
