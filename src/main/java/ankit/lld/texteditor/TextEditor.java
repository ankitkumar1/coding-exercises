package ankit.lld.texteditor;

public class TextEditor {
    StringBuilder content = new StringBuilder();

    public void insert(String text){
        content.append(text);
    }

    public void update(int start, int end, String newText){
        content.replace(start, end, newText);
    }

    public void delete(int start, int end){
        content.delete(start, end);
    }

    public String getText(){
        return content.toString();
    }
}
