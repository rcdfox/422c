/* Student Name: Allen Huang, Lab Section: 18690 */
package assignment1;

public class Problem1 {

    public static long largestProduct(String s, int n) {
        long largest = 0;

        // Iterate through entire string length
        for (int i = 0; i <= s.length(); i++) {
            long product = 1;
            
            for (int j = 0; j < n && (i+j) < s.length(); j++) {
                product *= (s.charAt(i + j) - '0');
            
                // Update largest at every length to satisfy "at most n"
                if (product > largest) {
                    largest = product;
                }
                // Break early if we hit a zero
                if (product == 0) {
                    break;
                }
            }
        }

        return largest;
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        String s = scanner.nextLine();
        System.out.println(largestProduct(s, n));
    }
}
