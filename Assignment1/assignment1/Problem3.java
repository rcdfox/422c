/* Student Name: Allen Huang, Lab Section: 18690 */
package assignment1;
import java.util.Scanner;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Problem3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String sentence = scanner.nextLine();

        // Load model
        String modelPath = "english-left3words-distsim.tagger";
        
        try {
            MaxentTagger tagger = new MaxentTagger(modelPath);
            
            // tagString method automatically handles "_TAG" formatting required by the example
            String tagged = tagger.tagString(sentence);
            
            System.out.println(tagged.trim());
        } catch (Exception e) {
            System.err.println("Failed to load the POS tagger model: " + e.getMessage());
        }
    }
}
