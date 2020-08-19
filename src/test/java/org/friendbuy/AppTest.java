package org.friendbuy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testSetUnset() {
        InMemoryDB imdb = new InMemoryDB();

        imdb.executeCommand("SET x 10");
        assertEquals(imdb.executeCommand("GET x"),"10");
        imdb.executeCommand("UNSET x");
        assertEquals(imdb.executeCommand("GET x"),"NULL");
    }
}
