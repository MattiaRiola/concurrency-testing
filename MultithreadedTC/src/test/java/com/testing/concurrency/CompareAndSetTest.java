package com.testing.concurrency;

import edu.umd.cs.mtc.MultithreadedTest;
import edu.umd.cs.mtc.TestFramework;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.TestCase.*;

public class CompareAndSetTest extends MultithreadedTest {
    AtomicInteger ai;
    @Override public void initialize() {
        ai = new AtomicInteger(1);
    }

    public void thread1() {
        while(!ai.compareAndSet(2, 3)) Thread.yield();
    }

    public void thread2() {
        assertTrue(ai.compareAndSet(1, 2));
    }

    @Override public void finish() {
        assertEquals(ai.get(), 3);
    }

    @Test
    public void testThreadSafeCounter() throws Throwable {
        TestFramework.runManyTimes(new CompareAndSetTest(), 5);
    }
}
