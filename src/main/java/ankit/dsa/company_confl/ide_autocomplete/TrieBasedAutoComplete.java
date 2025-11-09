package ankit.dsa.company_confl.ide_autocomplete;

import java.util.*;
import java.util.stream.Collectors;

//Segmented Trie structure, where each CamelCase segment becomes a searchable unit.
public class TrieBasedAutoComplete extends AutoComplete{

    private final TrieNode trieRoot;

    public TrieBasedAutoComplete(List<String> classNames) {
        super(classNames);
        TrieBuilder builder = new TrieBuilder();
        trieRoot = builder.buildTrie(classNames);
    }

    @Override
    public List<String> autoComplete(String pattern) {
        List<String> parts = splitPattern(pattern);
        Set<TrieNode> frontier = new HashSet<>();
        frontier.add(this.trieRoot);

        for(int i=0; i < parts.size(); i++){
            String part = parts.get(i);
            frontier = matchSegmentPrefix(frontier, part);
            if(frontier.isEmpty()){
                return Collections.emptyList();
            }
            if(i < parts.size()-1){
                frontier = advanceNodesSegmentBoundaries(frontier);
                if(frontier.isEmpty()){
                    return Collections.emptyList();
                }
            }
        }
        return frontier.stream()
                .flatMap(trieNode -> trieNode.classNames.stream())
                .collect(Collectors.toList());
    }

    private Set<TrieNode> matchSegmentPrefix(Set<TrieNode> nodes, String part) {
        Set<TrieNode> matchingNodes = new HashSet<>();
        for(TrieNode node : nodes){
            TrieNode current = node;
            for(char ch : part.toCharArray()){
                current = current.children.get(ch);
                if(current == null){
                    break;
                }
            }
            if(current != null){
                matchingNodes.add(current);
            }
        }
        return matchingNodes;
    }

    private Set<TrieNode> advanceNodesSegmentBoundaries(Set<TrieNode> frontier) {
        Set<TrieNode> advancedNodes = new HashSet<>();
        Queue<TrieNode> queue = new LinkedList<>(frontier);
        while(!queue.isEmpty()){
            TrieNode current = queue.poll();
            // If boundary reaches collect it.
            if(current.children.get('#') != null){
                advancedNodes.add(current.children.get('#'));
            }

            for(Character key : current.children.keySet()){
                if(key == '#'){
                    continue;
                }
                TrieNode node = current.children.get(key);
                queue.offer(node);
            }
        }
        return advancedNodes;
    }

    static class TrieNode{
        Map<Character, TrieNode> children = new HashMap<>();
        List<String> classNames = new ArrayList<>();
    }

    static class TrieBuilder{

        public TrieNode buildTrie(List<String> classNames) {
            TrieNode root = new TrieNode();
            for(String className : classNames){
                insertClassName(root, className);
            }
            return root;
        }

        private void insertClassName(TrieNode root, String className) {
            List<String> segments = splitPattern(className);
            TrieNode current = root;
            for(String segment : segments){
                for(char ch : segment.toCharArray()){
                    current.children.putIfAbsent(ch, new TrieNode());
                    current = current.children.get(ch);
                    current.classNames.add(className);
                }
                current.children.putIfAbsent('#', new TrieNode());
                current = current.children.get('#');
                current.classNames.add(className);
            }
        }
    }
}
