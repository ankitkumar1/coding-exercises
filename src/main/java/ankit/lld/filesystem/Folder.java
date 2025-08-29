package ankit.lld.filesystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Folder extends FileComponent{

    private Map<String, FileComponent> children;

    public Folder(String name){
        super(name);
        this.children = new HashMap<>();
    }
    @Override
    protected List<String> getList() {
        return new ArrayList<>(this.children.keySet());
    }

    @Override
    protected FileComponent getChild(String name) {
        return this.children.get(name);
    }

    @Override
    protected FileComponent add(FileComponent component) {
        this.children.put(component.getName(), component);
        return component;
    }
}
