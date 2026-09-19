package assignment2.mastermind;

public class GuessRecord {

    private final String guess;
    private final Feedback feedback;

    public GuessRecord(
            String guess,
            Feedback feedback) {

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

    public Feedback getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return guess + " -> " + feedback;
    }
}