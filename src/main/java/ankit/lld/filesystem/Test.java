package ankit.lld.filesystem;

import java.nio.file.FileSystem;

public class Test {
    public static void main(String[] args) {
        InMemoryFileSystem fs = new InMemoryFileSystem();

        fs.createDrive("OS");
        fs.pwd();

        fs.cd("main:/");
        fs.pwd();

        fs.cd("OS:/");
        fs.pwd();

        fs.mkdir("/a/b/c");
        fs.cd("/a/b");

        fs.pwd();

    }
}
