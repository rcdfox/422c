package assignment2.mastermind;

public class Feedback {

    private final int blackPegs;
    private final int whitePegs;

    public Feedback(
            int blackPegs,
            int whitePegs) {

        if (blackPegs < 0
                || whitePegs < 0) {

            throw new IllegalArgumentException(
                    "Peg counts cannot be negative."
            );
        }

        this.blackPegs = blackPegs;
        this.whitePegs = whitePegs;
    }

    public int getBlackPegs() {
        return blackPegs;
    }

    public int getWhitePegs() {
        return whitePegs;
    }

    @Override
    public String toString() {
        return blackPegs
                + "B_"
                + whitePegs
                + "W";
    }
}