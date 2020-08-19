package org.friendbuy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void testSetUnset() {
        InMemoryDB imdb = new InMemoryDB();

        imdb.executeCommand("SET x 10");
        assertEquals(imdb.executeCommand("GET x"),"10");
        imdb.executeCommand("UNSET x");
        assertEquals(imdb.executeCommand("GET x"),"NULL");
    }

    @Test
    public void testNumberEqualTo() {
        InMemoryDB imdb = new InMemoryDB();

        imdb.executeCommand("SET a 10");
        imdb.executeCommand("SET b 10");
        imdb.executeCommand("SET c 10");
        assertEquals(imdb.executeCommand("NUMEQUALTO 10"),"3");
        assertEquals(imdb.executeCommand("NUMEQUALTO 20"),"0");
        imdb.executeCommand("SET b 30");
        assertEquals(imdb.executeCommand("NUMEQUALTO 10"),"2");
    }

    @Test
    public void testRollback() {
        InMemoryDB imdb = new InMemoryDB();

        imdb.executeCommand("BEGIN");
        imdb.executeCommand("SET a 10");
        assertEquals(imdb.executeCommand("GET a"),"10");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("SET a 20");
        assertEquals(imdb.executeCommand("GET a"),"20");
        imdb.executeCommand("ROLLBACK");
        assertEquals(imdb.executeCommand("GET a"),"10");
        imdb.executeCommand("ROLLBACK");
        assertEquals(imdb.executeCommand("GET a"),"NULL");
    }

    @Test
    public void testCommit() {
        InMemoryDB imdb = new InMemoryDB();

        imdb.executeCommand("BEGIN");
        imdb.executeCommand("SET a 30");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("SET a 40");
        imdb.executeCommand("COMMIT");
        assertEquals(imdb.executeCommand("GET a"),"40");
        assertEquals(imdb.executeCommand("ROLLBACK"),"NO TRANSACTION");
    }

    @Test
    public void testRollbackCommit() {
        InMemoryDB imdb = new InMemoryDB();

        imdb.executeCommand("SET a 50");
        imdb.executeCommand("BEGIN");
        assertEquals(imdb.executeCommand("GET a"),"50");
        imdb.executeCommand("SET a 60");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("UNSET a");
        assertEquals(imdb.executeCommand("GET a"),"NULL");
        imdb.executeCommand("ROLLBACK");
        assertEquals(imdb.executeCommand("GET a"),"60");
        imdb.executeCommand("COMMIT");
        assertEquals(imdb.executeCommand("GET a"),"60");
    }

    @Test
    public void testNumberEqualToInTransaction() {
        InMemoryDB imdb = new InMemoryDB();

        imdb.executeCommand("SET a 10");
        imdb.executeCommand("BEGIN");
        assertEquals(imdb.executeCommand("NUMEQUALTO 10"),"1");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("UNSET a");
        assertEquals(imdb.executeCommand("NUMEQUALTO 10"),"0");
        imdb.executeCommand("ROLLBACK");
        assertEquals(imdb.executeCommand("NUMEQUALTO 10"),"1");
        imdb.executeCommand("COMMIT");
    }

}
