package org.friendbuy;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void runTest1(InMemoryDB imdb) {
        imdb.executeCommand("SET x 10");
        imdb.executeCommand("GET x");
        imdb.executeCommand("UNSET x");
        imdb.executeCommand("GET x");
        imdb.executeCommand("END");
    }

    public static void runTest2(InMemoryDB imdb) {
        imdb.executeCommand("SET a 10");
        imdb.executeCommand("SET b 10");
        imdb.executeCommand("SET c 10");
        imdb.executeCommand("NUMEQUALTO 10");
        imdb.executeCommand("NUMEQUALTO 20");
        imdb.executeCommand("SET b 30");
        imdb.executeCommand("NUMEQUALTO 10");
        imdb.executeCommand("END");
    }

    public static void runTest3(InMemoryDB imdb) {
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("SET a 10");
        imdb.executeCommand("GET a");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("SET a 20");
        imdb.executeCommand("GET a");
        imdb.executeCommand("ROLLBACK");
        imdb.executeCommand("GET a");
        imdb.executeCommand("ROLLBACK");
        imdb.executeCommand("GET a");
        imdb.executeCommand("END");
    }
    public static void runTest4(InMemoryDB imdb) {
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("SET a 30");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("SET a 40");
        imdb.executeCommand("COMMIT");
        imdb.executeCommand("GET a");
        imdb.executeCommand("ROLLBACK");
        imdb.executeCommand("END");
    }

    public static void runTest5(InMemoryDB imdb) {
        imdb.executeCommand("SET a 50");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("GET a");
        imdb.executeCommand("SET a 60");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("UNSET a");
        imdb.executeCommand("GET a");
        imdb.executeCommand("ROLLBACK");
        imdb.executeCommand("GET a");
        imdb.executeCommand("COMMIT");
        imdb.executeCommand("GET a");
        imdb.executeCommand("END");
    }

    public static void runTest6(InMemoryDB imdb) {
        imdb.executeCommand("SET a 10");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("NUMEQUALTO 10");
        imdb.executeCommand("BEGIN");
        imdb.executeCommand("UNSET a");
        imdb.executeCommand("NUMEQUALTO 10");
        imdb.executeCommand("ROLLBACK");
        imdb.executeCommand("NUMEQUALTO 10");
        imdb.executeCommand("COMMIT");
        imdb.executeCommand("END");
    }

    public static void main( String[] args )
    {
        InMemoryDB imdb = new InMemoryDB();
        Scanner scanner = new Scanner(System.in);
        runTest3(imdb);
//        while (true) {
//            System.out.println("Enter command:");
//            String cmd = scanner.nextLine();
//            imdb.executeCommand(cmd);
//        }
    }
}
