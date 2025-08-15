package composite_pattern.filesystem;

public class Test {
    public static void main(String[] args) {
        Directory root = new Directory("root");
        root.add(new File("file1"));

        Directory document = new Directory("Document");
        document.add(new File("Certificate1"));
        document.add(new File("Certificate2"));

        Directory pics = new Directory("Pics");
        pics.add(new File("pic1"));
        pics.add(new File("pic2"));

        Directory tourPics = new Directory("Trip1");
        tourPics.add(new File("pic1"));
        pics.add(tourPics);

        root.add(document);
        root.add(pics);

        root.ls("");
    }
}
