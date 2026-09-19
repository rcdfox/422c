package assignment2;

import assignment2.mastermind.MastermindConfig;
import assignment2.mastermind.MastermindGame;

public class Driver {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println(
                    "Usage: java assignment2.Driver mastermind [test]"
            );
            return;
        }

        String gameName = args[0];

        boolean testMode =
                args.length >= 2 &&
                args[1].equalsIgnoreCase("test");

        Game game;

        if (gameName.equalsIgnoreCase("mastermind")) {

            MastermindConfig config =
                    MastermindConfig.defaultConfig();

            ConsoleIO io = new ConsoleIO();

            game = new MastermindGame(
                    config,
                    io,
                    testMode
            );

        } else {
            System.out.println(
                    "Unknown game: " + gameName
            );
            return;
        }

        game.play();
    }
}