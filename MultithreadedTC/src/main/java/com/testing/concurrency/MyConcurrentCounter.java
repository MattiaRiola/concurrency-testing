package com.testing.concurrency;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyConcurrentCounter {
    private int count;

    private int sum;


    /**     example:
     * #        count val  sum val
     * start:   0           0
     * 1        1           1
     * 2        2           3
     * 3        3           6
     * 4        4           10
     * 5        5           15
     * 6        6           21
     *
     */
    public synchronized void increment(Map<ThreadInfo,Integer> threadInfoMap) {
        String threadName = Thread.currentThread().getName();
        List<ThreadInfo> threadList = new LinkedList<>();
        threadList.add(new ThreadInfo(threadName, new CounterState(count, sum, 0)));
        int temp = count;

        threadList.add(new ThreadInfo(threadName, new CounterState(count, sum, 1)));
        count = temp + 1;

        threadList.add(new ThreadInfo(threadName, new CounterState(count, sum, 2)));
        sum = sum + count;
        for (ThreadInfo threadInfo : threadList) {
            threadInfoMap.computeIfPresent(threadInfo, (k,v) -> v+1);
            threadInfoMap.putIfAbsent(threadInfo, 1);
        }
    }

    public int getCount() {
        return count;
    }

    public int getSum() {
        return sum;
    }
}
