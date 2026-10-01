package assignment2.tests;

import assignment2.wordle.WordDictionary;
import assignment2.wordle.WordleFeedback;
import assignment2.wordle.WordleRules;

import java.util.List;

public class WordleRulesTest {

    public static void main(String[] args) {

        WordDictionary dictionary =
                new WordDictionary(
                        List.of(
                                "APPLE",
                                "ALLEY",
                                "PUPPY",
                                "ALARM",
                                "GRAPE",
                                "PLANT"
                        ),
                        5
                );

        WordleRules rules =
                new WordleRules(dictionary, 5);

        checkFeedback(
                rules,
                "APPLE",
                "APPLE",
                "C C C C C"
        );

        checkFeedback(
                rules,
                "APPLE",
                "ALLEY",
                "C P A P A"
        );

        // Guess contains more P's than the secret.
        checkFeedback(
                rules,
                "APPLE",
                "PUPPY",
                "P A C A A"
        );

        // The first A is CORRECT. The second A
        // must not incorrectly receive PRESENT.
        checkFeedback(
                rules,
                "APPLE",
                "ALARM",
                "C P A A A"
        );

        check(
                rules.isValidGuess("apple"),
                "Lowercase dictionary word should be valid."
        );

        check(
                !rules.isValidGuess("APP"),
                "Wrong-length guess should be invalid."
        );

        check(
                !rules.isValidGuess("APP1E"),
                "Nonalphabetic guess should be invalid."
        );

        check(
                !rules.isValidGuess("XXXXX"),
                "Word not in dictionary should be invalid."
        );

        System.out.println(
                "WordleRulesTest: PASS"
        );
    }

    private static void checkFeedback(
            WordleRules rules,
            String secret,
            String guess,
            String expected) {

        WordleFeedback feedback =
                rules.evaluateGuess(
                        secret,
                        guess
                );

        check(
                feedback.toString()
                        .equals(expected),
                "Expected "
                        + expected
                        + " for secret "
                        + secret
                        + " and guess "
                        + guess
                        + ", but got "
                        + feedback
        );
    }

    private static void check(
            boolean condition,
            String message) {

        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
