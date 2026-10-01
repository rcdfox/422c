package assignment2.wordle;

import java.util.Arrays;

public class WordleFeedback {

    public static final char CORRECT = 'C';
    public static final char PRESENT = 'P';
    public static final char ABSENT = 'A';

    private final char[] statuses;

    public WordleFeedback(char[] statuses) {

        if (statuses == null
                || statuses.length == 0) {
            throw new IllegalArgumentException(
                    "Feedback statuses cannot be empty."
            );
        }

        for (char status : statuses) {
            if (status != CORRECT
                    && status != PRESENT
                    && status != ABSENT) {
                throw new IllegalArgumentException(
                        "Invalid Wordle feedback status: "
                                + status
                );
            }
        }

        this.statuses =
                Arrays.copyOf(
                        statuses,
                        statuses.length
                );
    }

    public int length() {
        return statuses.length;
    }

    public char getStatus(int index) {
        return statuses[index];
    }

    public boolean isAllCorrect() {
        for (char status : statuses) {
            if (status != CORRECT) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {

        StringBuilder result =
                new StringBuilder();

        for (int i = 0;
             i < statuses.length;
             i++) {

            if (i > 0) {
                result.append(' ');
            }

            result.append(statuses[i]);
        }

        return result.toString();
    }
}
