package ankit.dsa.company_confl.tailcommand;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class TailWithBufferedReaderAndDQueue {
    public void tail(String filePath, int n) throws FileNotFoundException {
        Deque<String> lastNLines = new ArrayDeque<>(n);
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(lastNLines.size() == n){
                    lastNLines.removeFirst();
                }
                lastNLines.offer(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lastNLines.forEach(System.out::println);

    }
}
