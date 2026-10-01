package assignment2;

import assignment2.mastermind.MastermindConfig;
import assignment2.mastermind.MastermindGame;
import assignment2.wordle.WordDictionary;
import assignment2.wordle.WordleGame;

import java.io.IOException;
import java.nio.file.Path;

public class Driver {

    private static final String DEFAULT_WORD_FILE =
            "words.txt";

    public static void main(String[] args) {

        if (args.length < 1) {
            printUsage();
            return;
        }

        String gameName = args[0];

        boolean testMode =
                args.length >= 2
                        && args[1]
                        .equalsIgnoreCase("test");

        ConsoleIO io = new ConsoleIO();
        Game game;

        if (gameName.equalsIgnoreCase(
                "mastermind")) {

            MastermindConfig config =
                    MastermindConfig.defaultConfig();

            game = new MastermindGame(
                    config,
                    io,
                    testMode
            );

        } else if (gameName.equalsIgnoreCase(
                "wordle")) {

            String dictionaryFile =
                    getDictionaryFile(
                            args,
                            testMode
                    );

            try {
                WordDictionary dictionary =
                        WordDictionary.load(
                                Path.of(
                                        dictionaryFile
                                ),
                                WordleGame.WORD_LENGTH
                        );

                game = new WordleGame(
                        io,
                        testMode,
                        dictionary
                );

            } catch (IOException
                     | IllegalArgumentException e) {

                System.out.println(
                        "Unable to load Wordle "
                                + "dictionary: "
                                + dictionaryFile
                );
                System.out.println(e.getMessage());
                return;
            }

        } else {
            System.out.println(
                    "Unknown game: " + gameName
            );
            printUsage();
            return;
        }

        game.play();
    }

    private static String getDictionaryFile(
            String[] args,
            boolean testMode) {

        int dictionaryIndex =
                testMode ? 2 : 1;

        if (args.length > dictionaryIndex) {
            return args[dictionaryIndex];
        }

        return DEFAULT_WORD_FILE;
    }

    private static void printUsage() {

        System.out.println(
                "Usage: java assignment2.Driver "
                        + "mastermind [test]"
        );

        System.out.println(
                "   or: java assignment2.Driver "
                        + "wordle [test] "
                        + "[word-list-file]"
        );
    }
}
