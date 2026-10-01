package assignment2;

public class GuessRecord<F> {

    private final String guess;
    private final F feedback;

    public GuessRecord(
            String guess,
            F feedback) {

        if (guess == null) {
            throw new IllegalArgumentException(
                    "Guess cannot be null."
            );
        }

        if (feedback == null) {
            throw new IllegalArgumentException(
                    "Feedback cannot be null."
            );
        }

        this.guess = guess;
        this.feedback = feedback;
    }

    public String getGuess() {
        return guess;
    }

    public F getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return guess + " -> " + feedback;
    }
}
