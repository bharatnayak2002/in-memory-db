package org.friendbuy;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        InMemoryDB imdb = new InMemoryDB();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter command:");
            String cmd = scanner.nextLine();
            imdb.executeCommand(cmd);
        }
    }
}
