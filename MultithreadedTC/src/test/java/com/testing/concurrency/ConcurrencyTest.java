package com.testing.concurrency;

import org.junit.Test;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;

public class ConcurrencyTest extends MultithreadedTestCase {

    private MyCounter counter;
    @Override
    public void initialize() {
        counter = new MyCounter();
    }
    public void thread1() throws InterruptedException {
        counter.increment();
    }
    public void thread2() throws InterruptedException {
        counter.increment();
    }
    @Override
    public void finish() {
        assertEquals(2, counter.getCount());
    }

    @Test
    public void testThreadUnsafeCounter() throws Throwable {
        TestFramework.runManyTimes(new ConcurrencyTest(), 1000);
    }
}
