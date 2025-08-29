package ankit.lld.filesystem;

import java.util.List;

public class File extends FileComponent{
    private StringBuilder content;

    public File(String name) {
        super(name);
        this.content = new StringBuilder();
    }

    @Override
    protected List<String> getList() {
        return List.of(super.getName());
    }

    @Override
    protected String getContent(){
        return this.content.toString();
    }

    @Override
    protected void addContent(String data) {
        content.append(data);
    }
}
