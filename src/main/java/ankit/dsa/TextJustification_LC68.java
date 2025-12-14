package ankit.dsa;

import java.util.ArrayList;
import java.util.List;

public class TextJustification_LC68 {
    public static void main(String[] args) {
        TextJustification_LC68 obj = new TextJustification_LC68();
        System.out.println(obj.fullJustify(new String[] {"This", "is", "an", "example", "of", "text", "justification."}, 16));
    }
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> result = new ArrayList<>();
        int idx = 0;
        int n = words.length;
        while(idx < n){
            StringBuilder builder = new StringBuilder();
            builder.append(words[idx++]);
            int totalWords = 1;
            while(idx < n && builder.length() +  words[idx].length() < maxWidth){
                builder.append(" ").append(words[idx++]);
                totalWords++;
            }
            String[] sentence = builder.toString().split(" ");
            // Append spaces now.
            int shortFall = maxWidth - builder.length();
            if(idx == n || sentence.length==1){
                // All spaces in right.
                builder.append(" ".repeat(shortFall));
            }else if(totalWords > 1){
                int spaces = shortFall / (totalWords-1);
                int module = shortFall % (totalWords-1);

                builder = new StringBuilder();
                for(int i=0; i<sentence.length-1; i++){
                    builder.append(sentence[i]);
                    builder.append(" ".repeat(spaces+1));
                    if(module > 0){
                        builder.append(" ");
                        module--;
                    }
                }
                builder.append(sentence[sentence.length-1]);
            }
            result.add(builder.toString());
        }
        return result;
    }
}
