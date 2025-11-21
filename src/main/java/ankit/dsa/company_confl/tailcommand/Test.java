package ankit.dsa.company_confl.tailcommand;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Test {
    public static void main(String[] args) throws IOException {
        // Generate file at same location.
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("sampleinput.txt",
                StandardCharsets.UTF_8))) {
            for(int i=1; i<=10; i++){
                writer.write("line number - "+i);
                writer.newLine();
            }
        }

        /*BufferedReader reader = new BufferedReader(new FileReader("sampleinput.txt", StandardCharsets.UTF_8));
        String line = reader.readLine();
        while(line != null){
            System.out.println(line);
            line = reader.readLine();
        }*/
        /*TailCommand1 tail = new TailCommand1();
        tail.tail("sampleinput.txt", 5);
        System.out.println();
        tail.tail("sampleinput.txt", 12);

        System.out.println();
        System.out.println();

        TailWithBufferedReader1 tail2 = new TailWithBufferedReader1();
        tail2.tail("sampleinput.txt", 5);
        System.out.println();
        tail2.tail("sampleinput.txt", 12);*/

        TailWithBufferedReaderAndRandomAccessFile tail3 = new TailWithBufferedReaderAndRandomAccessFile();
        System.out.println("Test 1:");
        tail3.tail("sampleinput.txt", 5);
        System.out.println("Test 2:");
        tail3.tail("sampleinput.txt", 10);
    }
}
