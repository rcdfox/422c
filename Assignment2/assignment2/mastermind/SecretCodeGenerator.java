package assignment2.mastermind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SecretCodeGenerator {

    private final Random random;

    public SecretCodeGenerator() {
        this.random = new Random();
    }

    public SecretCodeGenerator(Random random) {

        if (random == null) {
            throw new IllegalArgumentException(
                    "Random generator cannot be null."
            );
        }

        this.random = random;
    }

    public String generateSecret(
            MastermindConfig config) {

        if (config == null) {
            throw new IllegalArgumentException(
                    "Configuration cannot be null."
            );
        }

        List<Character> colors =
                new ArrayList<>(
                        config.getLegalColors()
                );

        /*
         * Sort the symbols so that using a seeded
         * Random object produces deterministic results
         * regardless of Set iteration order.
         */
        Collections.sort(colors);

        StringBuilder secret =
                new StringBuilder();

        for (int i = 0;
             i < config.getCodeLength();
             i++) {

            int index =
                    random.nextInt(colors.size());

            secret.append(colors.get(index));
        }

        return secret.toString();
    }
}