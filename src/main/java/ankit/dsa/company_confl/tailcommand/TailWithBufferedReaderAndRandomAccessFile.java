package ankit.dsa.company_confl.tailcommand;

import java.io.*;

public class TailWithBufferedReaderAndRandomAccessFile {
    public void tail(String filePath, int n) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            long toSkip = getSeekPosition(filePath, n);
            while(toSkip > 0){
                toSkip -= reader.skip(toSkip);
            }

            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long getSeekPosition(String filePath, int n){
        long position = -1L;
        try(RandomAccessFile file = new RandomAccessFile(filePath, "r")){
            long fileLength = file.length();
            if(fileLength == 0){
                return position;
            }
            position = fileLength - 1;
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
        }catch (IOException fne){
            return 0L;
        }
        return position;
    }
}
