package assignment2.wordle;

import assignment2.ConsoleIO;
import assignment2.GuessingGame;

public class WordleGame
        extends GuessingGame<WordleFeedback> {

    public static final int WORD_LENGTH = 5;
    public static final int MAX_GUESSES = 6;

    private final WordDictionary dictionary;
    private final WordleRules rules;
    private final boolean testMode;

    public WordleGame(
            ConsoleIO io,
            boolean testMode,
            WordDictionary dictionary) {

        super(io, testMode);

        if (dictionary == null) {
            throw new IllegalArgumentException(
                    "Dictionary cannot be null."
            );
        }

        this.dictionary = dictionary;
        this.rules = new WordleRules(
                dictionary,
                WORD_LENGTH
        );
        this.testMode = testMode;
    }

    @Override
    protected String generateSecret() {

        if (testMode) {
            return dictionary.firstWord();
        }

        return dictionary.randomWord();
    }

    @Override
    protected int getMaxGuesses() {
        return MAX_GUESSES;
    }

    @Override
    protected boolean isValidGuess(String guess) {
        return rules.isValidGuess(guess);
    }

    @Override
    protected WordleFeedback evaluateGuess(
            String secret,
            String guess) {

        return rules.evaluateGuess(secret, guess);
    }

    @Override
    protected boolean isWinningFeedback(
            WordleFeedback feedback) {

        return rules.isWinningFeedback(feedback);
    }

    @Override
    protected void printGameStart() {

        io.println("");
        io.println("Wordle");
        io.println(
                "Word length: "
                        + WORD_LENGTH
        );
        io.println(
                "Maximum guesses: "
                        + MAX_GUESSES
        );
        io.println("");
    }

    @Override
    protected void printInvalidGuess() {

        io.println("Invalid guess.");
        io.println(
                "A valid guess must be a "
                        + WORD_LENGTH
                        + "-letter alphabetic word "
                        + "in the allowed-word list."
        );
    }

    @Override
    protected String getSecretLabel() {
        return "word";
    }
}
