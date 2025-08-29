package ankit.lld.filesystem;

import java.util.List;

public abstract class FileComponent {
    private final String name;

    public FileComponent(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    protected abstract List<String> getList();

    protected String getContent(){
        throw new UnsupportedOperationException("Not supported since its a directory!");
    }

    protected FileComponent getChild(String name){
        throw new UnsupportedOperationException("Not supported since its a file!");
    }

    protected FileComponent add(FileComponent component){
        throw new UnsupportedOperationException("Can't add child to a file!");
    }

    protected void addContent(String data) {
        throw new UnsupportedOperationException("Can't add content to a folder!");
    }

}
