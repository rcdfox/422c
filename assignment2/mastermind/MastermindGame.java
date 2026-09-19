package assignment2.mastermind;

import assignment2.ConsoleIO;
import assignment2.Game;

import java.util.ArrayList;
import java.util.List;

public class MastermindGame implements Game {

    private final MastermindConfig config;
    private final ConsoleIO io;
    private final boolean testMode;

    private final MastermindRules rules;
    private final SecretCodeGenerator secretGenerator;

    public MastermindGame(
            MastermindConfig config,
            ConsoleIO io,
            boolean testMode) {

        if (config == null) {
            throw new IllegalArgumentException(
                    "Configuration cannot be null."
            );
        }

        if (io == null) {
            throw new IllegalArgumentException(
                    "ConsoleIO cannot be null."
            );
        }

        this.config = config;
        this.io = io;
        this.testMode = testMode;

        this.rules =
                new MastermindRules(config);

        this.secretGenerator =
                new SecretCodeGenerator();
    }

    @Override
    public void play() {

        boolean playAgain;

        do {

            playSingleGame();

            playAgain = askToPlayAgain();

        } while (playAgain);
    }

    private void playSingleGame() {

        String secret =
                secretGenerator.generateSecret(config);

        List<GuessRecord> history =
                new ArrayList<>();

        int guessesUsed = 0;

        printGameStart();

        if (testMode) {
            io.println(
                    "TEST MODE - Secret code: "
                            + secret
            );
        }

        while (guessesUsed
                < config.getMaxGuesses()) {

            int guessesRemaining =
                    config.getMaxGuesses()
                            - guessesUsed;

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

            /*
             * HISTORY is a command rather than a
             * guess, so it does not use an attempt.
             */
            if (input.equalsIgnoreCase(
                    "HISTORY")) {

                printHistory(history);
                continue;
            }

            /*
             * Invalid guesses also do not use an
             * attempt.
             */
            if (!rules.isValidGuess(input)) {

                io.println("Invalid guess.");

                io.println(
                        "A valid guess must contain "
                                + config.getCodeLength()
                                + " legal color symbols."
                );

                io.println(
                        "Legal colors: "
                                + config.getLegalColors()
                );

                continue;
            }

            Feedback feedback =
                    rules.evaluateGuess(
                            secret,
                            input
                    );

            GuessRecord record =
                    new GuessRecord(
                            input,
                            feedback
                    );

            history.add(record);

            guessesUsed++;

            io.println(record.toString());

            /*
             * All positions being black means
             * the secret was guessed exactly.
             */
            if (rules.isWinningFeedback(
                    feedback)) {

                io.println("You win!");
                return;
            }

            io.println("");
        }

        /*
         * Reaching this point means every valid
         * attempt was used without winning.
         */
        io.println("You lose.");

        io.println(
                "The secret code was: "
                        + secret
        );
    }

    private void printGameStart() {

        io.println("");
        io.println("Mastermind");
        io.println(
                "Code length: "
                        + config.getCodeLength()
        );

        io.println(
                "Legal colors: "
                        + config.getLegalColors()
        );

        io.println(
                "Maximum guesses: "
                        + config.getMaxGuesses()
        );

        io.println("");
    }

    private void printHistory(
            List<GuessRecord> history) {

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
                    || response.equalsIgnoreCase(
                    "YES")) {

                return true;
            }

            if (response.equalsIgnoreCase("N")
                    || response.equalsIgnoreCase(
                    "NO")) {

                return false;
            }

            io.println(
                    "Please enter Y or N."
            );
        }
    }
}