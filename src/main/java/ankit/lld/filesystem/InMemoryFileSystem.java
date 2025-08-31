package ankit.lld.filesystem;

import java.util.HashMap;
import java.util.Map;

public class InMemoryFileSystem {
    private final Map<String, Drive> drives;
    private Folder cwd;
    private Drive currentDrive;

    public InMemoryFileSystem(){
        this.drives = new HashMap<>();
        this.createDrive("main");
    }

    public void cd(String path){
        Parsed parsed = FSUtil.parse(path, currentDrive);
        Folder current = parsed.absolute ? drives.get(parsed.drive) : this.cwd;
        if(parsed.nodeNames.isEmpty()){
            this.cwd = current;
            return;
        }
        for(String node : parsed.nodeNames){
            FSNode fsNode =  current.getChild(node);
            if(fsNode == null || fsNode.isFile()){
                throw new IllegalArgumentException("Path doesn't exist!");
            }
            current = (Folder) fsNode;
        }
        this.currentDrive = drives.get(parsed.drive);
        this.cwd = current;
    }

    /**
     * It will create a directory.
     * Option 1: abc : create a directory in the current working folder/drive
     * Option 2: /abc/def : Check if abc exists then create def inside abc or if abc doesn't exist then
     *          it will first create abc then def inside that.
     * */
    public void mkdir(String path){
        if(currentDrive == null || cwd == null){
            throw new RuntimeException("First create a drive!");
        }
        Parsed parsed = FSUtil.parse(path, currentDrive);
        Folder parent = this.cwd;
        for(String node : parsed.nodeNames){
            if(!parent.contains(node)){
                parent.addChild(node, new Folder(node, NodeType.FOLDER, parent));
            }
            parent = (Folder) parent.getChild(node);
        }
    }

    /**
     * Print the present working directory
     * */
    public void pwd(){
        System.out.println(this.cwd.getAbsolutePath());
    }

    /**
     * Delete the folder or file.
     * recursive : true means delete all folders on the path.
     * recursive: false -> delete only last value.
     * If we are deleting a file then recursive = false. it will only delete the file.
     * */
    public void delete(String path, boolean recursive){
        Parsed parsed = FSUtil.parse(path, currentDrive);
        Folder current = this.drives.get(parsed.drive);
        FSNode firstNode = current.getChild(parsed.nodeNames.getFirst());
        if(firstNode.isFile() || recursive){
            current.remove(parsed.nodeNames.getFirst());
        }
        current = (Folder) firstNode;
        for(int i=1; i<parsed.nodeNames.size()-1; i++){
            current = (Folder) current.getChild(parsed.nodeNames.get(i));
        }
        if(current == null){
            throw new IllegalArgumentException("Path is not correct!");
        }
        current.remove(parsed.nodeNames.getLast());
    }

    /**
     * It relocates a file or directory from one location to another within the file system.
     * mv [source] [destination]
     * Renaming files and directories
     * mv [old_name] [new_name]
     * */
    public void move(String fromPath, String toPath){

    }

    public void createDrive(String name){
        if(this.drives.containsKey(name))
            throw new IllegalArgumentException("dive with name "+name+" already exists!");

        Drive drive = new Drive(name);
        this.drives.put(name, drive);
        this.currentDrive = drive;
        this.cwd = drive;
        System.out.println("Drive with name "+name+" successfully created!");
    }

}
