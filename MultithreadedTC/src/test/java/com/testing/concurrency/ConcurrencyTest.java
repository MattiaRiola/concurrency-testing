package com.testing.concurrency;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.Display;
import org.junit.Test;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.testing.concurrency.ConcurrencyTestPrinter.*;
import static java.util.Arrays.asList;

public class ConcurrencyTest extends MultithreadedTestCase {

    private MyCounter counter;
    static private final ConcurrentHashMap<ThreadInfo, Integer> threadInfoMap = new ConcurrentHashMap<>();

    @Override
    public void initialize() {
        counter = new MyCounter();
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
    public void multipleTests() throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        Display display = new Display(terminal, true);
        int passed = 0;
        int failed = 0;
        int totoalAttempts = 1000;
        int attempts = 0;
        for (int i = 0; i < totoalAttempts && passed == 0; i++, attempts++) {
            try {
                printProgressBar(i, totoalAttempts);

                testThreadUnsafeCounter();
                //System.err.println("Test passed");
                passed++;
                //printThreadInfo(threadInfoMap);
            } catch (Throwable e) {
                failed++;
                //System.out.println("Test failed: " + e.getMessage());
            }
        }
        assertEquals("attempt number "+attempts+ " test shouldn't pass",0,passed);
    }


    public void testThreadUnsafeCounter() throws Throwable {
        TestFramework.runManyTimes(new ConcurrencyTest(), 400);
    }

}
