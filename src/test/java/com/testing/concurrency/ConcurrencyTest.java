package com.testing.concurrency;

import com.testing.concurrency.model.UniqueList;


import com.google.testing.threadtester.AnnotatedTestRunner;
import com.google.testing.threadtester.ThreadedAfter;
import com.google.testing.threadtester.ThreadedBefore;
import com.google.testing.threadtester.ThreadedMain;
import com.google.testing.threadtester.ThreadedSecondary;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConcurrencyTest {

    private UniqueList uniqueList;

    @ThreadedBefore
    public void before() {
        uniqueList = new UniqueList();
    }

    @ThreadedMain
    public void mainThread() {
        System.out.println("mainThread");
        uniqueList.putIfAbsent("Hello");
    }

    @ThreadedSecondary
    public void secondThread() {
        System.out.println("secondThread");
        uniqueList.putIfAbsent("Hello");
    }

    @ThreadedAfter
    public void after() {
        assertEquals(1, uniqueList.size());
    }

    @Test
    public void testCounter() {
        new AnnotatedTestRunner().runTests(this.getClass(), UniqueList.class);
    }
}
