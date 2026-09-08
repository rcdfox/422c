/* Student Name: Allen Huang, Lab Section: 18690 */
package assignment1;

public class Problem1Tester {
    public static void main(String[] args) {
        // Example of the required format only. Replace/add cases based on the specification.
        TestSupport.checkEquals(5832L,
                Problem1.largestProduct("9989", 4),
                "example-sized prefix");
        
        // Shorter sequence beats longer one with a zero
        TestSupport.checkEquals(9L,
            Problem1.largestProduct("909",3),
            "shorter sequence preferable due to zero");

        // Sequence shorter than n
        TestSupport.checkEquals(81L,
            Problem1.largestProduct("99", 5),
            "input string is shorter than n");

        // Exact n is best and no zeros
        TestSupport.checkEquals(24L, 
                Problem1.largestProduct("234", 3), 
                "exact n digits yields the largest product");

        // All zeros
        TestSupport.checkEquals(0L, 
                Problem1.largestProduct("0000", 3), 
                "string of all zeros");

        TestSupport.finish();
    }
}
