package assignment2.wordle;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WordleRules {

    private final WordDictionary dictionary;
    private final int wordLength;

    public WordleRules(
            WordDictionary dictionary,
            int wordLength) {

        if (dictionary == null) {
            throw new IllegalArgumentException(
                    "Dictionary cannot be null."
            );
        }

        if (wordLength <= 0) {
            throw new IllegalArgumentException(
                    "Word length must be positive."
            );
        }

        this.dictionary = dictionary;
        this.wordLength = wordLength;
    }

    public boolean isValidGuess(String guess) {

        if (guess == null) {
            return false;
        }

        String normalized = normalize(guess);

        if (normalized.length() != wordLength) {
            return false;
        }

        for (int i = 0;
             i < normalized.length();
             i++) {

            if (!Character.isLetter(
                    normalized.charAt(i))) {
                return false;
            }
        }

        return dictionary.contains(normalized);
    }

    public WordleFeedback evaluateGuess(
            String secret,
            String guess) {

        String normalizedSecret =
                normalize(secret);

        String normalizedGuess =
                normalize(guess);

        if (!isValidSecret(normalizedSecret)) {
            throw new IllegalArgumentException(
                    "Secret word is invalid."
            );
        }

        if (!isValidGuess(normalizedGuess)) {
            throw new IllegalArgumentException(
                    "Guess is invalid."
            );
        }

        char[] statuses =
                new char[wordLength];

        boolean[] exactMatches =
                new boolean[wordLength];

        Map<Character, Integer> unmatchedSecretLetters =
                new HashMap<>();

        /*
         * First pass: assign CORRECT matches and
         * count only unmatched secret letters.
         */
        for (int i = 0;
             i < wordLength;
             i++) {

            char secretLetter =
                    normalizedSecret.charAt(i);

            char guessLetter =
                    normalizedGuess.charAt(i);

            if (secretLetter == guessLetter) {

                statuses[i] =
                        WordleFeedback.CORRECT;

                exactMatches[i] = true;

            } else {

                unmatchedSecretLetters.put(
                        secretLetter,
                        unmatchedSecretLetters
                                .getOrDefault(
                                        secretLetter,
                                        0
                                ) + 1
                );
            }
        }

        /*
         * Second pass: an unmatched secret-letter
         * occurrence can satisfy at most one guess.
         */
        for (int i = 0;
             i < wordLength;
             i++) {

            if (exactMatches[i]) {
                continue;
            }

            char guessLetter =
                    normalizedGuess.charAt(i);

            int remaining =
                    unmatchedSecretLetters
                            .getOrDefault(
                                    guessLetter,
                                    0
                            );

            if (remaining > 0) {

                statuses[i] =
                        WordleFeedback.PRESENT;

                if (remaining == 1) {
                    unmatchedSecretLetters.remove(
                            guessLetter
                    );
                } else {
                    unmatchedSecretLetters.put(
                            guessLetter,
                            remaining - 1
                    );
                }

            } else {
                statuses[i] =
                        WordleFeedback.ABSENT;
            }
        }

        return new WordleFeedback(statuses);
    }

    public boolean isWinningFeedback(
            WordleFeedback feedback) {

        return feedback != null
                && feedback.isAllCorrect();
    }

    private boolean isValidSecret(String secret) {

        if (secret == null
                || secret.length() != wordLength) {
            return false;
        }

        for (int i = 0;
             i < secret.length();
             i++) {

            if (!Character.isLetter(
                    secret.charAt(i))) {
                return false;
            }
        }

        return dictionary.contains(secret);
    }

    private static String normalize(String word) {
        return word.trim().toUpperCase(Locale.ROOT);
    }
}
