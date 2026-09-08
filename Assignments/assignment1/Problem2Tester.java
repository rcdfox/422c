package assignment1;

/** Student-written tests for Problem 2. Add your own test cases below. */
public class Problem2Tester {
    public static void main(String[] args) {
        String input = "The wicked wizard`s wily wraith garnishes his master`s pasta with garlic.";
        String expected = "wizard`s" + System.lineSeparator()
                + "garnishes" + System.lineSeparator();

        TestSupport.checkEquals(expected,
                Problem2.findDollarWords(input),
                "assignment example");

        // Defect #1: Case preservation
        TestSupport.checkEquals("EXCELLENT" + System.lineSeparator(),
                Problem2.findDollarWords("EXCELLENT"),
                "preserves uppercase exactly as it appeared");

        // Defect #2: Punctuation inside words
        TestSupport.checkEquals("",
                Problem2.findDollarWords("boycott's"),
                "non-alphabetic characters do not end the word prematurely");

        // Defect #3: Multiple spaces
        TestSupport.checkEquals("telephone" + System.lineSeparator(),
                Problem2.findDollarWords("telephone     "),
                "handles trailing spaces or multiple spaces");

        TestSupport.finish();
    }
}
