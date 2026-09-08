/* Student Name: Allen Huang, Lab Section: 18690 */
package assignment1;

public class Problem2 {

    public static String findDollarWords(String s) {
        StringBuilder result = new StringBuilder();

        String[] words = s.split(" ");
        for (String word : words) {
            // Handle multiple spaces between words
            if (word.isEmpty()){
                continue;
            }

            int value = 0;

            for (char c : word.toCharArray()) {
                if (c >= 'a' && c <= 'z') {
                    value += c - 'a' + 1;
                } else if (c >= 'A' && c <= 'Z') {
                    value += c - 'A' + 1;
                }
                // Non alphabetics have value 0, continue
            }

            if (value == 100) {
                // Append original word to preserve formatting
                result.append(word).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String s = scanner.nextLine();
        System.out.print(findDollarWords(s));
    }
}
