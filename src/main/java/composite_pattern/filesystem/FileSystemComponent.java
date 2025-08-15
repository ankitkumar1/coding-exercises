package composite_pattern.filesystem;

public interface FileSystemComponent {

    void ls(String indent);
    String getName();
    String updateName(String name);
}
