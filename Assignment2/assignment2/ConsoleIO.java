package assignment2;

import java.util.Scanner;

public class ConsoleIO {

    private final Scanner scanner;

    public ConsoleIO() {
        scanner = new Scanner(System.in);
    }

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public String readLine() {
        return scanner.nextLine();
    }
}