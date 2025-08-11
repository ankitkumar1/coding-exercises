package ankit.lld.texteditor;

public class UpdateCommand implements Command{
    private final TextEditor editor;
    private final String udpatedText;
    private final int start, end;
    private String oldText;

    public UpdateCommand(TextEditor editor, String udpatedText, int start, int end){
        this.editor = editor;
        this.udpatedText = udpatedText;
        this.start = start;
        this.end = end;
    }
    @Override
    public void execute() {
        this.oldText = editor.getText().substring(start, end);
        editor.update(start, end, udpatedText);
    }

    @Override
    public void undo() {
        editor.update(start, start+udpatedText.length(), oldText);
    }
}
