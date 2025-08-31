package ankit.lld.filesystem;

public class File extends FSNode{

    private final StringBuilder content = new StringBuilder();

    public File(String name, Folder parent) {
        super(name, NodeType.FILE, parent);
    }

    public void addContent(String content){
        this.content.append(content);
    }

    public String getContent(){
        return this.content.toString();
    }



}
