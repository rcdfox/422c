import java.util.HashMap;
import java.util.Scanner;

public class WordCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Integer> wordCounts = new HashMap<>();

        // Read text from the user
        System.out.println("Enter a line of text:");
        String text = scanner.nextLine();

        // Split the text into words
        String[] words = text.split(" ");

        // Count occurrences of each word
        for (String word : words) {
            if (wordCounts.containsKey(word)) {
                wordCounts.put(word, wordCounts.get(word) + 1);
            } else {
                wordCounts.put(word, 1);
            }
        }

        // Print each word and its count
        for (String word : wordCounts.keySet()) {
            System.out.println(word + ": " + wordCounts.get(word));
        }

        scanner.close();
    }
}