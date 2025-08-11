package ankit.lld.texteditor;

public class InsertCommand implements Command{
    private final TextEditor editor;
    private final String textToInsert;

    public InsertCommand(TextEditor editor, String textToInsert){
        this.editor = editor;
        this.textToInsert = textToInsert;
    }

    @Override
    public void execute() {
        editor.insert(textToInsert);
    }

    @Override
    public void undo() {
        int start = editor.getText().length() - textToInsert.length();
        editor.delete(start, editor.getText().length());
    }
}
