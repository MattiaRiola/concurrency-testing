package com.testing.concurrency;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadSafeCounterTest extends MultithreadedTestCase {

    private MyConcurrentCounter counter;
    static private final ConcurrentHashMap<ThreadInfo, Integer> threadInfoMap = new ConcurrentHashMap<>();

    @Override
    public void initialize() {
        counter = new MyConcurrentCounter();
    }
    public void thread1() throws InterruptedException {
        counter.increment(threadInfoMap);
        counter.increment(threadInfoMap);
    }
    public void thread2() throws InterruptedException {
        counter.increment(threadInfoMap);
        counter.increment(threadInfoMap);
    }
    @Override
    public void finish() {
        assertEquals(4, counter.getCount());
        assertEquals(1+2+3+4, counter.getSum());
    }

    @Test
    public void testThreadSafeCounter() throws Throwable {
        TestFramework.runManyTimes(new ThreadSafeCounterTest(), 1000);

        Comparator<ThreadInfo> threadInfoComparator = Comparator.comparing(ThreadInfo::getCount)
                .thenComparing(ThreadInfo::getInstruction)
                .thenComparing(ThreadInfo::getThreadName)
                ;
        threadInfoMap.keySet().stream()
                .sorted(threadInfoComparator)
                .forEach(System.out::println);
    }
}
