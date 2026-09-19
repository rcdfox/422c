package assignment2.mastermind;

import java.util.Set;

public class MastermindConfig {

    private final int maxGuesses;
    private final int codeLength;
    private final Set<Character> legalColors;

    public MastermindConfig(
            int maxGuesses,
            int codeLength,
            Set<Character> legalColors) {

        if (maxGuesses <= 0) {
            throw new IllegalArgumentException(
                    "Maximum guesses must be positive."
            );
        }

        if (codeLength <= 0) {
            throw new IllegalArgumentException(
                    "Code length must be positive."
            );
        }

        if (legalColors == null ||
                legalColors.isEmpty() ||
                legalColors.size() > 10) {

            throw new IllegalArgumentException(
                    "There must be between 1 and 10 legal colors."
            );
        }

        this.maxGuesses = maxGuesses;
        this.codeLength = codeLength;
        this.legalColors = Set.copyOf(legalColors);
    }

    public static MastermindConfig defaultConfig() {
        return new MastermindConfig(
                12,
                4,
                Set.of('B', 'G', 'O', 'P', 'R', 'Y')
        );
    }

    public int getMaxGuesses() {
        return maxGuesses;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public Set<Character> getLegalColors() {
        return legalColors;
    }
}