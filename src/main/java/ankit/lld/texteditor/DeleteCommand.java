package ankit.lld.texteditor;

public class DeleteCommand implements Command{

    private final TextEditor editor;
    private final int start, end;
    private String deletedText;

    public DeleteCommand(TextEditor editor, int start, int end){
        this.editor = editor;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute() {
        deletedText = editor.getText().substring(start, end);
        editor.remove(start, end);
    }

    @Override
    public void undo() {
        editor.replace(start, start, deletedText);
    }
}
