package assignment2.mastermind;

import assignment2.ConsoleIO;
import assignment2.Game;

public class MastermindGame implements Game {

    private final MastermindConfig config;
    private final ConsoleIO io;
    private final boolean testMode;

    public MastermindGame(
            MastermindConfig config,
            ConsoleIO io,
            boolean testMode) {

        this.config = config;
        this.io = io;
        this.testMode = testMode;
    }

    @Override
    public void play() {
        // Mastermind game logic will be added later.
    }
}