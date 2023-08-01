package com.testing.concurrency.demo;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * This test should show that the mtc library does not test all the possible combinations of executions
 * of the threads. It is a good library to test concurrency but it is not enough to test all the possible
 * combinations of executions. It is also possible to use it to test specific combinations using its apis
 * that it provides to test the interleaving of the threads.
 */
public class ConcurrencyTest extends MultithreadedTestCase {

    private final Queue<Integer> queue = new LinkedBlockingQueue<>();

    private final HashMap<String,Integer> combinations = new HashMap<>();

    @AfterEach
    public void printCombinations(){
        }

    @Override
    public void initialize() {
        //System.out.println("Execution "+execution);
    }

    public void thread1(){
        queue.add(1);
    }

    public void thread2(){
        queue.add(2);
    }

    public void thread3(){
        queue.add(3);
    }

    @Override
    public void finish() {
        Integer first = queue.poll();
        Integer second = queue.poll();
        Integer third = queue.poll();

        String combination = first.toString()+second.toString()+third.toString();
        combinations.putIfAbsent(combination,0);
        combinations.computeIfPresent(combination,(k,v)->v+1);
    }

    @Test
    public void doesNotTestAllTheCombinationsOfExecutions() throws Throwable {
        this.setTrace(true);
        System.setProperty("tunit.runLimit","20");
        TestFramework.runManyTimes(this, 10);
        combinations.forEach((k,v)->{
            System.out.println(k+" "+v);
        });
        assertEquals("There should be 3! = 6 combinations",6,combinations.size());
    }

    @Test
    public void shouldTestAllTheCombinationsOfExecutions() throws Throwable {
        this.setTrace(true);
        System.setProperty("tunit.runLimit","20");
        int attempts = 0;
        while(combinations.size()<6 && attempts <100) {
            TestFramework.runManyTimes(this, 100);
            attempts++;
        }
        combinations.forEach((k,v)->{
            System.out.println(k+" "+v);
        });
        assertEquals("There should be 3! = 6 combinations",6,combinations.size());
    }
}
