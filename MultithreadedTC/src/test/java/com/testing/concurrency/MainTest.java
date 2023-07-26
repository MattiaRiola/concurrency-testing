package com.testing.concurrency;


import org.junit.Test;

import java.util.HashMap;

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
        counter.increment(new HashMap<>());
        counter.increment(new HashMap<>());
        assertEquals(2, counter.getCount());
    }
}

