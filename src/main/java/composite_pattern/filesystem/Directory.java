package composite_pattern.filesystem;

import java.util.ArrayList;
import java.util.List;

public class Directory implements FileSystemComponent{
    private String name;
    private List<FileSystemComponent> children = new ArrayList<>();

    public Directory(String name){
        this.name = name;
    }

    @Override
    public void ls(String indent) {
        System.out.println(indent + "/" + name);
        children.forEach(fileSystem -> fileSystem.ls(indent+"/"+name));
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String updateName(String newName) {
        this.name = newName;
        return newName;
    }
}
