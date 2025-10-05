package ankit.dsa;

import java.util.HashMap;
import java.util.Map;

public class TestAlphabetFrequency {
    static Map<String, Character> numberMap = new HashMap<>();
    static {
        numberMap.put("1", 'a');
        numberMap.put("2", 'b');
        numberMap.put("3", 'c');
        numberMap.put("4", 'd');

        numberMap.put("10#", 'j');
        numberMap.put("11#", 'k');
    }
    public static void main(String[] args) {
        String str = "1(3)11#(2)10#(5)";
        String[] array = str.split("\\)");
        int[] freq = new int[26];
        for(int i=0; i<array.length; i++){
            String[] charAndFreq = array[i].split("\\(");
            System.out.println(numberMap.get(charAndFreq[0])+"_"+charAndFreq[1]);
            freq[numberMap.get(charAndFreq[0])-'a'] =
                        freq[numberMap.get(charAndFreq[0])-'a']+Integer.valueOf(charAndFreq[1]);
        }
        System.out.println(numberMap);
    }
}
