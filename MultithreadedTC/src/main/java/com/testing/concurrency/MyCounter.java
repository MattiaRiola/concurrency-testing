package com.testing.concurrency;

public class MyCounter {
    private int count;
    public void increment() {
        String threadName = Thread.currentThread().getName();
        String prefix = "Thread-"+threadName+" : ";
        System.out.println(prefix + "is running");
        int temp = count;
        System.out.println(prefix + "temp = " + temp);
        count = temp + 1;
        System.out.println(prefix + "count = " + count);
    }

    public int getCount() {
        return count;
    }
}
