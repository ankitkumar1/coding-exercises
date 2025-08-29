package ankit.lld.filesystem;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FileSystem {

    FileComponent root;

    public FileSystem(){
        this.root = new Folder("");
    }

    public List<String> ls(String path) {
        FileComponent component = getComponent(path);
        if(component!=null){
            return component.getList().isEmpty() ? List.of("/") : component.getList();
        }
        return Collections.emptyList();
    }

    public void mkdir(String path) {
        setComponent(path, true);
    }

    public void addContentToFile(String filePath, String content) {
        FileComponent component = setComponent(filePath, false);
        if(component!=null){
            component.addContent(content);
        }
    }

    public String readContentFromFile(String filePath) {
        FileComponent file = getComponent(filePath);
        return file.getContent();
    }

    private FileComponent getComponent(String filePath){
        String[] nodes = filePath.split("/");
        FileComponent targetDirectory = this.root;
        for(int i=1; i<nodes.length && targetDirectory!=null; i++){
            targetDirectory = targetDirectory.getChild(nodes[i]);
        }
        return targetDirectory;
    }

    private FileComponent setComponent(String filePath, boolean isDirectory){
        String[] nodes = filePath.split("/");
        FileComponent targetFileSystem = this.root;
        FileComponent next = null;
        for(int i=1; i<nodes.length; i++){
            next = targetFileSystem.getChild(nodes[i]);
            if(next == null){
                if(isDirectory){
                    next = targetFileSystem.add(new Folder(nodes[i]));
                }else if( i == nodes.length-1){
                    next = targetFileSystem.add(new File(nodes[i]));
                }
            }
            targetFileSystem = next;
        }
        return targetFileSystem;
    }

}
