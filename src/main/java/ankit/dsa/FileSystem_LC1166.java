package ankit.dsa;

import java.util.HashMap;
import java.util.Map;

public class FileSystem_LC1166 {
    TrieNode trieRoot;
    public FileSystem_LC1166() {
        trieRoot = new TrieNode('-');
    }

    public boolean createPath(String path, int value) {
        return trieRoot.insert(path, value);
    }

    public int get(String path) {
        return trieRoot.getValue(path);
    }

    static class TrieNode{
        char key;
        int value;
        Map<Character, TrieNode> children;
        public TrieNode(char key){
            this.key = key;
            this.value = -1;
            this.children = new HashMap<>();
        }

        public boolean insert(String path, int value){
            TrieNode currentNode = this;
            for(int i=0; i<path.length(); i++){
                char current = path.charAt(i);
                if(current == '/'  && currentNode.key!='-' && currentNode.value == -1){
                    return false;
                }

                currentNode.children.putIfAbsent(current, new TrieNode(current));
                currentNode = currentNode.children.get(current);
                if(i == path.length()-1){
                    if(currentNode.value > -1){
                        return false;
                    }
                    currentNode.value = value;
                }
            }
            return true;
        }

        public int getValue(String path){
            TrieNode currentNode = this;
            for(int i=0; i<path.length(); i++){
                currentNode = currentNode.children.get(path.charAt(i));
                if(currentNode == null){
                    return -1;
                }
                if(i == path.length()-1){
                    return currentNode.value;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        FileSystem_LC1166 obj = new FileSystem_LC1166();
        // Test case 1
        //System.out.println(obj.createPath("/a",1));
        //System.out.println(obj.get("/a"));

        // Test case 2
        System.out.println(obj.createPath("/let",1));
        System.out.println(obj.createPath("/let/code",2));
    }
}
