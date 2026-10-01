package assignment2.tests;

import assignment2.mastermind.Feedback;
import assignment2.mastermind.MastermindConfig;
import assignment2.mastermind.MastermindRules;

public class MastermindRulesRegressionTest {

    public static void main(String[] args) {

        MastermindConfig config =
                MastermindConfig.defaultConfig();

        MastermindRules rules =
                new MastermindRules(config);

        checkFeedback(
                rules,
                "RGBY",
                "RGBY",
                4,
                0
        );

        checkFeedback(
                rules,
                "RGBY",
                "GBYR",
                0,
                4
        );

        checkFeedback(
                rules,
                "RRGB",
                "RRRR",
                2,
                0
        );

        check(
                !rules.isValidGuess("RGB"),
                "Wrong-length Mastermind guess "
                        + "should remain invalid."
        );

        check(
                !rules.isValidGuess("RGBX"),
                "Illegal Mastermind color "
                        + "should remain invalid."
        );

        System.out.println(
                "MastermindRulesRegressionTest: PASS"
        );
    }

    private static void checkFeedback(
            MastermindRules rules,
            String secret,
            String guess,
            int black,
            int white) {

        Feedback feedback =
                rules.evaluateGuess(
                        secret,
                        guess
                );

        check(
                feedback.getBlackPegs() == black
                        && feedback.getWhitePegs()
                        == white,
                "Unexpected Mastermind feedback "
                        + "for "
                        + secret
                        + " / "
                        + guess
                        + ": "
                        + feedback
        );
    }

    private static void check(
            boolean condition,
            String message) {

        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
