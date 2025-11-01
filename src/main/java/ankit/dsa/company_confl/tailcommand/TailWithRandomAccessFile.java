package ankit.dsa.company_confl.tailcommand;

import java.io.*;

/*
* tail -n
* Using RandonAccessFile class.
* */
public class TailWithRandomAccessFile {

    public void tail(String filePath, int n) {
        try(RandomAccessFile file = new RandomAccessFile(filePath, "r")){
            long fileLength = file.length();
            if(fileLength == 0){
                return;
            }
            long position = fileLength - 1;
            int lineCount = 0;
            while(position >= 0){
                file.seek(position);
                int currentChar = file.read();

                if(currentChar == '\n'){
                    lineCount++;
                    if(lineCount > n){
                        break;
                    }
                }
                position--;
            }

            if(position < 0){
                position = 0;
            }else{
                position += 1;
            }

            file.seek(position);

            String line;
            while((line = file.readLine()) != null){
                System.out.println(line);
            }
        }catch (IOException fne){

        }
    }
}
