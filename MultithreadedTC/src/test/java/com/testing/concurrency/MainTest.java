package com.testing.concurrency;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A simple unit test
 */
public class MainTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        var counter = new MyCounter();
        counter.increment();
        counter.increment();
        assertEquals(2, counter.getCount());
    }
}

