package assignment2;

import java.util.ArrayList;
import java.util.List;

public abstract class GuessingGame<F> implements Game {

    protected final ConsoleIO io;
    private final boolean testMode;

    protected GuessingGame(
            ConsoleIO io,
            boolean testMode) {

        if (io == null) {
            throw new IllegalArgumentException(
                    "ConsoleIO cannot be null."
            );
        }

        this.io = io;
        this.testMode = testMode;
    }

    @Override
    public final void play() {

        boolean playAgain;

        do {
            playSingleGame();
            playAgain = askToPlayAgain();
        } while (playAgain);
    }

    private void playSingleGame() {

        String secret = generateSecret();

        List<GuessRecord<F>> history =
                new ArrayList<>();

        int guessesUsed = 0;

        printGameStart();

        if (testMode) {
            io.println(
                    "TEST MODE - Secret "
                            + getSecretLabel()
                            + ": "
                            + secret
            );
        }

        while (guessesUsed < getMaxGuesses()) {

            int guessesRemaining =
                    getMaxGuesses() - guessesUsed;

            io.println(
                    "You have "
                            + guessesRemaining
                            + " guess(es) left."
            );

            io.print(
                    "Enter your guess or HISTORY: "
            );

            String input =
                    io.readLine().trim();

            if (input.equalsIgnoreCase("HISTORY")) {
                printHistory(history);
                continue;
            }

            if (!isValidGuess(input)) {
                printInvalidGuess();
                continue;
            }

            F feedback =
                    evaluateGuess(secret, input);

            GuessRecord<F> record =
                    new GuessRecord<>(
                            input,
                            feedback
                    );

            history.add(record);
            guessesUsed++;

            io.println(record.toString());

            if (isWinningFeedback(feedback)) {
                io.println("You win!");
                return;
            }

            io.println("");
        }

        io.println("You lose.");
        io.println(
                "The secret "
                        + getSecretLabel()
                        + " was: "
                        + secret
        );
    }

    private void printHistory(
            List<GuessRecord<F>> history) {

        if (history.isEmpty()) {
            io.println("No valid guesses yet.");
            return;
        }

        io.println("History:");

        for (int i = 0;
             i < history.size();
             i++) {

            io.println(
                    (i + 1)
                            + ". "
                            + history.get(i)
            );
        }
    }

    private boolean askToPlayAgain() {

        while (true) {

            io.print(
                    "Would you like to play again? "
                            + "(Y/N): "
            );

            String response =
                    io.readLine().trim();

            if (response.equalsIgnoreCase("Y")
                    || response.equalsIgnoreCase("YES")) {
                return true;
            }

            if (response.equalsIgnoreCase("N")
                    || response.equalsIgnoreCase("NO")) {
                return false;
            }

            io.println("Please enter Y or N.");
        }
    }

    protected abstract String generateSecret();

    protected abstract int getMaxGuesses();

    protected abstract boolean isValidGuess(String guess);

    protected abstract F evaluateGuess(
            String secret,
            String guess);

    protected abstract boolean isWinningFeedback(F feedback);

    protected abstract void printGameStart();

    protected abstract void printInvalidGuess();

    protected abstract String getSecretLabel();
}
