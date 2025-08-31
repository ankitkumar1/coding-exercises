package ankit.lld.filesystem;

public class Drive extends Folder{

    public Drive(String name) {
        super(name, NodeType.DRIVE, null);
    }

    // TODO: Validate Drive name since it should not contain "/" or ":".




}
