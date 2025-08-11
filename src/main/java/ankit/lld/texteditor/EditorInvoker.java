package ankit.lld.texteditor;

import java.util.Stack;

public class EditorInvoker {

    private final Stack<Command> commandHistory = new Stack<>();

    public void executeCommand(Command command){
        command.execute();;
        commandHistory.push(command);
    }

    public void undo(){
        if(!commandHistory.isEmpty()){
            commandHistory.pop().undo();
        }
    }
}
