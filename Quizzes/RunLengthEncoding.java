/* Run-Length Encoding 
Run-Length Encoding (RLE) is a lossless data compression technique that replaces sequences 
of repeated data with a count of repetitions and the data item itself, making it effective 
for files with high redundancy, like images with large areas of uniform color. For example, 
"AAAAABBBCCDAA" would be compressed to "5A3B2C1D2A".

Write Java code that performs run-length encoding on a string of text. 
*/

public class RunLengthEncoding {
    public static String encode(String input) {
        // Empty string
        if (input == null || input.isEmpty()) {
            return "";
        }
        // Build encoded string
        StringBuilder encoded = new StringBuilder();
        int count = 1;
        char currentChar = input.charAt(0);

        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == currentChar) {
                count++;
            } else {
                encoded.append(count).append(currentChar);
                currentChar = input.charAt(i);
                count = 1;
            }
        }
        // Append the last sequence
        encoded.append(count).append(currentChar);
        return encoded.toString();
    }
    // Test
    public static void main(String[] args) {
        String input = "4441111778";
        String encoded = encode(input);
        System.out.println("Encoded: " + encoded);
    }
}
