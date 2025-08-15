package composite_pattern.filesystem;

public class File implements FileSystemComponent{
    private String name;

    public File(String name){
        this.name = name;
    }
    @Override
    public void ls(String indent) {
        System.out.println(indent+"/"+name);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String updateName(String newName) {
        this.name = newName;
        return newName;
    }
}
