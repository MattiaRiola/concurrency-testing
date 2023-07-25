package com.testing.concurrency;

public class MyConcurrentCounter {
    private int count;
    public synchronized void increment() {
        int temp = count;
        count = temp + 1;
    }

    public int getCount() {
        return count;
    }
}
