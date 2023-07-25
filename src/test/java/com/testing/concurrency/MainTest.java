package com.testing.concurrency;

import static org.junit.jupiter.api.Assertions.*;

import com.testing.concurrency.model.UniqueList;
import org.junit.jupiter.api.Test;

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
        var uniqueList = new UniqueList<String>();
        uniqueList.putIfAbsent("Hello");
        uniqueList.putIfAbsent("World");
        uniqueList.putIfAbsent("Hello");
        assertEquals(2, uniqueList.size());
        assertTrue(uniqueList.contains("Hello"));
        assertTrue(uniqueList.contains("World"));
    }
}
