package assignment2.mastermind;

import java.util.HashMap;
import java.util.Map;

public class MastermindRules {

    private final MastermindConfig config;

    public MastermindRules(MastermindConfig config) {
        if (config == null) {
            throw new IllegalArgumentException(
                    "Configuration cannot be null."
            );
        }

        this.config = config;
    }

    public boolean isValidGuess(String guess) {

        if (guess == null) {
            return false;
        }

        if (guess.length() != config.getCodeLength()) {
            return false;
        }

        for (int i = 0; i < guess.length(); i++) {
            char color = guess.charAt(i);

            if (!config.getLegalColors().contains(color)) {
                return false;
            }
        }

        return true;
    }

    public Feedback evaluateGuess(String secret, String guess) {

        if (!isValidCode(secret)) {
            throw new IllegalArgumentException(
                    "Secret code is invalid."
            );
        }

        if (!isValidGuess(guess)) {
            throw new IllegalArgumentException(
                    "Guess is invalid."
            );
        }

        int blackPegs = 0;
        int whitePegs = 0;

        boolean[] exactMatches =
                new boolean[config.getCodeLength()];

        Map<Character, Integer> unmatchedSecretColors =
                new HashMap<>();

        // First pass:
        // Find all exact matches.
        for (int i = 0; i < config.getCodeLength(); i++) {

            char secretColor = secret.charAt(i);
            char guessColor = guess.charAt(i);

            if (secretColor == guessColor) {

                blackPegs++;
                exactMatches[i] = true;

            } else {

                unmatchedSecretColors.put(
                        secretColor,
                        unmatchedSecretColors.getOrDefault(
                                secretColor,
                                0
                        ) + 1
                );
            }
        }

        // Second pass:
        // Find correct colors in incorrect positions.
        for (int i = 0; i < config.getCodeLength(); i++) {

            if (exactMatches[i]) {
                continue;
            }

            char guessColor = guess.charAt(i);

            int remaining =
                    unmatchedSecretColors.getOrDefault(
                            guessColor,
                            0
                    );

            if (remaining > 0) {

                whitePegs++;

                if (remaining == 1) {
                    unmatchedSecretColors.remove(guessColor);
                } else {
                    unmatchedSecretColors.put(
                            guessColor,
                            remaining - 1
                    );
                }
            }
        }

        return new Feedback(blackPegs, whitePegs);
    }

    public boolean isWinningFeedback(Feedback feedback) {

        if (feedback == null) {
            return false;
        }

        return feedback.getBlackPegs()
                == config.getCodeLength();
    }

    private boolean isValidCode(String code) {

        if (code == null) {
            return false;
        }

        if (code.length() != config.getCodeLength()) {
            return false;
        }

        for (int i = 0; i < code.length(); i++) {

            if (!config.getLegalColors().contains(
                    code.charAt(i))) {

                return false;
            }
        }

        return true;
    }
}