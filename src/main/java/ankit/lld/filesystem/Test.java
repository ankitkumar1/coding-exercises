package ankit.lld.filesystem;

public class Test {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        System.out.println(fileSystem.ls("/"));

        fileSystem.mkdir("/a/b/c");
        fileSystem.addContentToFile("/a/b/c/d", "Hello");

        System.out.println(fileSystem.ls("/"));

        System.out.println(fileSystem.readContentFromFile("/a/b/c/d"));
    }
}
