package ankit.lld.filesystem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Folder extends FSNode{

    private final Map<String, FSNode> children = new HashMap<>();

    public Folder(String name, NodeType nodeType , Folder parent) {
        super(name, nodeType, parent);
    }

    public Collection<FSNode> getChildren(){
        return this.children.values();
    }

    public void addChild(String name, FSNode child){
        this.children.put(name, child);
    }

    public boolean contains(String name){
        return this.children.containsKey(name);
    }

    public FSNode getChild(String name){
        return this.children.get(name);
    }

    public FSNode remove(String name){
        return this.children.remove(name);
    }

    public boolean isEmpty(){
        return this.children.isEmpty();
    }
}
