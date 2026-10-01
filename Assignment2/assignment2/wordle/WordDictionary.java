package assignment2.wordle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class WordDictionary {

    private final List<String> words;
    private final Set<String> allowedWords;
    private final Random random;
    private final int wordLength;

    public WordDictionary(
            Collection<String> sourceWords,
            int wordLength) {

        this(sourceWords, wordLength, new Random());
    }

    public WordDictionary(
            Collection<String> sourceWords,
            int wordLength,
            Random random) {

        if (sourceWords == null) {
            throw new IllegalArgumentException(
                    "Word collection cannot be null."
            );
        }

        if (wordLength <= 0) {
            throw new IllegalArgumentException(
                    "Word length must be positive."
            );
        }

        if (random == null) {
            throw new IllegalArgumentException(
                    "Random generator cannot be null."
            );
        }

        this.wordLength = wordLength;
        this.random = random;
        this.words = new ArrayList<>();
        this.allowedWords = new HashSet<>();

        for (String sourceWord : sourceWords) {

            if (sourceWord == null) {
                continue;
            }

            String word = normalize(sourceWord);

            if (!isAlphabeticWord(word)
                    || word.length() != wordLength) {
                continue;
            }

            if (allowedWords.add(word)) {
                words.add(word);
            }
        }

        if (words.isEmpty()) {
            throw new IllegalArgumentException(
                    "Dictionary contains no valid "
                            + wordLength
                            + "-letter words."
            );
        }
    }

    public static WordDictionary load(
            Path path,
            int wordLength) throws IOException {

        if (path == null) {
            throw new IllegalArgumentException(
                    "Dictionary path cannot be null."
            );
        }

        return new WordDictionary(
                Files.readAllLines(path),
                wordLength
        );
    }

    public boolean contains(String word) {

        if (word == null) {
            return false;
        }

        return allowedWords.contains(
                normalize(word)
        );
    }

    public String randomWord() {
        return words.get(
                random.nextInt(words.size())
        );
    }

    public String firstWord() {
        return words.get(0);
    }

    public int getWordLength() {
        return wordLength;
    }

    private static String normalize(String word) {
        return word.trim().toUpperCase(Locale.ROOT);
    }

    private static boolean isAlphabeticWord(
            String word) {

        if (word.isEmpty()) {
            return false;
        }

        for (int i = 0;
             i < word.length();
             i++) {

            if (!Character.isLetter(
                    word.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
