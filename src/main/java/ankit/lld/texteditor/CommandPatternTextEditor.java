package ankit.lld.texteditor;

public class CommandPatternTextEditor {

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        EditorInvoker invoker = new EditorInvoker();

        invoker.executeCommand(new InsertCommand(editor, "Hello"));
        invoker.executeCommand(new InsertCommand(editor, " welcome to"));
        invoker.executeCommand(new InsertCommand(editor, " my text editor."));

        System.out.println(editor.getText());
        invoker.undo();
        System.out.println(editor.getText());
        invoker.executeCommand(new UpdateCommand(editor, " Ankit", 5,5));
        invoker.executeCommand(new InsertCommand(editor, " my text editor."));
        invoker.executeCommand(new UpdateCommand(editor, " dsfdsfdsfsdfd", 2,5));
        System.out.println(editor.getText());
        invoker.undo();
        System.out.println(editor.getText());
        invoker.executeCommand(new DeleteCommand(editor, 5,10));
        System.out.println(editor.getText());
        invoker.undo();
        System.out.println(editor.getText());
    }
}
