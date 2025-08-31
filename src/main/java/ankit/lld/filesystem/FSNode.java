package ankit.lld.filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public abstract class FSNode {
    private String name;
    private final NodeType nodeType;
    private Folder parent;

    public FSNode(String name, NodeType nodeType, Folder parent){
        this.name = Objects.requireNonNull(name);
        this.nodeType = Objects.requireNonNull(nodeType);
        this.parent = parent;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String updatedName){
        this.name = updatedName;
    }

    public NodeType getNodeType(){
        return this.nodeType;
    }

    public Folder getParent(){
        return this.parent;
    }

    public String getAbsolutePath(){
        if(this.nodeType == NodeType.DRIVE)
            return this.getName()+":/";
        var hierarchy = new ArrayList<String>();
        FSNode current = this;
        while(current != null && current.nodeType != NodeType.DRIVE){
            hierarchy.add(current.getName());
            current = current.parent;
        }
        // Ideally this should not happen.
        if(current == null){
            return "/";
        }
        // Ideally this should not happen.
        Collections.reverse(hierarchy);
        return current.getAbsolutePath() + String.join( "/", hierarchy);
    }

    public boolean isFile(){
        return this.nodeType==NodeType.FILE;
    }
}
