package com.testing.concurrency;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import org.junit.Test;

public class ThreadSafeCounterTest extends MultithreadedTestCase {

    private MyConcurrentCounter counter;
    @Override
    public void initialize() {
        counter = new MyConcurrentCounter();
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
    public void testThreadSafeCounter() throws Throwable {
        TestFramework.runManyTimes(new ThreadSafeCounterTest(), 1000);
    }
}
