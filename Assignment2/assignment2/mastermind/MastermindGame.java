package assignment2.mastermind;

import assignment2.ConsoleIO;
import assignment2.GuessingGame;

public class MastermindGame
        extends GuessingGame<Feedback> {

    private final MastermindConfig config;
    private final MastermindRules rules;
    private final SecretCodeGenerator secretGenerator;

    public MastermindGame(
            MastermindConfig config,
            ConsoleIO io,
            boolean testMode) {

        super(io, testMode);

        if (config == null) {
            throw new IllegalArgumentException(
                    "Configuration cannot be null."
            );
        }

        this.config = config;
        this.rules = new MastermindRules(config);
        this.secretGenerator =
                new SecretCodeGenerator();
    }

    @Override
    protected String generateSecret() {
        return secretGenerator.generateSecret(config);
    }

    @Override
    protected int getMaxGuesses() {
        return config.getMaxGuesses();
    }

    @Override
    protected boolean isValidGuess(String guess) {
        return rules.isValidGuess(guess);
    }

    @Override
    protected Feedback evaluateGuess(
            String secret,
            String guess) {

        return rules.evaluateGuess(secret, guess);
    }

    @Override
    protected boolean isWinningFeedback(
            Feedback feedback) {

        return rules.isWinningFeedback(feedback);
    }

    @Override
    protected void printGameStart() {

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

    @Override
    protected void printInvalidGuess() {

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
    }

    @Override
    protected String getSecretLabel() {
        return "code";
    }
}
