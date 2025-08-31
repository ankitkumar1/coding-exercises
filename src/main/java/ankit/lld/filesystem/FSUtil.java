package ankit.lld.filesystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FSUtil {

    public static Parsed parse(String path, Drive currentDrive){
        if(path == null || path.isEmpty()){
            throw new IllegalArgumentException("path should not be blank!");
        }
        Parsed parsed = new Parsed();
        path = path.trim();
        int driveSeparatorIndex = path.indexOf(":/");
        if(driveSeparatorIndex>0){
            parsed.drive = path.substring(0, driveSeparatorIndex);
            path = path.substring(driveSeparatorIndex+2);
            parsed.absolute = true;
        }else if(path.startsWith("/")){
            // User might pass the path from the current drive.
            parsed.drive = currentDrive!=null ? currentDrive.getName() : null;
            path = path.substring(1);
            parsed.absolute = true;
        }else{
            parsed.drive = currentDrive != null ? currentDrive.getName() : null;
            parsed.absolute = false;
        }

        var nodeNames = new ArrayList<String>();
        if(!path.isEmpty()){
            nodeNames.addAll(Arrays.asList(path.split("/")));
        }
        parsed.nodeNames = nodeNames;
        return parsed;
    }
}
