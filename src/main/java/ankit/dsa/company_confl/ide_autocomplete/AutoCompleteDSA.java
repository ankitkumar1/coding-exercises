package ankit.dsa.company_confl.ide_autocomplete;

import java.util.*;
import java.util.stream.Collectors;

public class AutoCompleteDSA {

    private final TrieNode root;

    public AutoCompleteDSA(List<String> classes){
        this.root = buildTrie(classes);
    }

    public List<String> autoComplete(String pattern){
        if(!isValidatePattern(pattern)){
            return Collections.emptyList();
        }
        List<String> parts = splitPattern(pattern);
        Set<TrieNode> frontier = new HashSet<>();
        frontier.add(root);
        for(int i=0; i<parts.size(); i++){
            String part = parts.get(i);
            frontier = matchSegment(frontier, part);
            if(frontier.isEmpty()){
                return Collections.emptyList();
            }
            if(i < parts.size()-1){
                frontier = advanceToTerminal(frontier);
                if(frontier.isEmpty()){
                    return Collections.emptyList();
                }
            }
        }
        return frontier
                .stream()
                .flatMap(trieNode -> trieNode.classes.stream())
                .collect(Collectors.toList());
    }

    private Set<TrieNode> advanceToTerminal(Set<TrieNode> frontier) {
        Queue<TrieNode> queue = new LinkedList<>(frontier);
        var nodes = new HashSet<TrieNode>();
        while(!queue.isEmpty()){
            TrieNode current = queue.poll();
            if(current.children.containsKey('*')){
                nodes.add(current.children.get('*'));
            }
            for(Character key : current.children.keySet()){
                if(key == '*'){
                    continue;
                }
                queue.offer(current.children.get(key));
            }
        }
        return nodes;
    }

    private Set<TrieNode> matchSegment(Set<TrieNode> frontier, String part) {
        Set<TrieNode> matchinNodes = new HashSet<>();
        for(TrieNode node : frontier){
            TrieNode current = node;
            for(char ch : part.toCharArray()){
                current = current.children.get(ch);
                if(current == null){
                    break;
                }
            }
            if(current!=null){
                matchinNodes.add(current);
            }
        }
        return matchinNodes;
    }

    private TrieNode buildTrie(List<String> classes) {
        TrieNode root = new TrieNode();
        for(String className : classes){
            if(!isValidatePattern(className)){
                continue;
            }
            insertInTrie(root, className);
        }
        return root;
    }

    private void insertInTrie(TrieNode root, String className) {
        TrieNode current = root;
        List<String> segments = splitPattern(className);
        for(String segment : segments){
            for(char c : segment.toCharArray()){
                current.children.putIfAbsent(c, new TrieNode());
                current = current.children.get(c);
                current.classes.add(className);
            }
            current.children.putIfAbsent('*', new TrieNode());
            current = current.children.get('*');
            current.classes.add(className);
        }
    }

    private boolean isValidatePattern(String pattern){
        for(char ch : pattern.toCharArray()){
            if(ch == ' ' || !(Character.isAlphabetic(ch) || Character.isDigit(ch))){
                //throw new IllegalArgumentException("Only alphanumeric allowed in pattern and class : "+pattern);
                return false;
            }
        }
        return true;
    }

    public List<String> splitPattern(String pattern){
        var splitSegment = new ArrayList<String>();
        StringBuilder currentSegment = new StringBuilder();
        for(char ch : pattern.toCharArray()){
            if(Character.isUpperCase(ch)){
                if(!currentSegment.isEmpty()){
                    splitSegment.add(currentSegment.toString());
                    currentSegment.setLength(0);
                }
            }
            currentSegment.append(ch);
        }
        if(!currentSegment.isEmpty()){
            splitSegment.add(currentSegment.toString());
        }
        return splitSegment;
    }

    static class TrieNode{
        Map<Character, TrieNode> children = new HashMap<>();
        Set<String> classes = new HashSet<>();
    }

    public static void main(String[] args) {
        AutoCompleteDSA autoComplete = new AutoCompleteDSA(List.of("Container", "AutoPanel", "RidePrinter",
                "ResumePanel", "RegularContainer", "RegularPanel", "ResultPlan"));
        //System.out.println(autoComplete.splitPattern("Rv&Ab"));
        System.out.println(autoComplete.autoComplete("C")); // Container
        System.out.println(autoComplete.autoComplete("RP")); // ResumePanel , RegularPanel, ResultPlan
        System.out.println(autoComplete.autoComplete("RegPa")); //RegularPanel
        System.out.println(autoComplete.autoComplete("RegcPa")); //""
    }
}
