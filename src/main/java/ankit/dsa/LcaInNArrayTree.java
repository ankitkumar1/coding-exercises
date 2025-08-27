package ankit.dsa;

import java.util.*;

public class LcaInNArrayTree {

    public static void main(String[] args) {
        Node root = new Node(1);

        Node node_2 = new Node(2);
        Node node_3 = new Node(3);
        Node node_4 = new Node(4);
        root.children.addAll(List.of(node_2, node_3, node_4));

        Node node_5 = new Node(5);
        Node node_6 = new Node(6);
        node_2.children.addAll(List.of(node_5, node_6));

        Node node_7 = new Node(7);
        Node node_11 = new Node(11);
        node_4.children.addAll(List.of(node_7, node_11));

        Node node_8 = new Node(8);
        Node node_9 = new Node(9);
        node_7.children.addAll(List.of(node_8, node_9));

        Node node_10 = new Node(10);
        node_3.children.add(node_10);

        LcaInNArrayTree obj = new LcaInNArrayTree();
        Node lca = obj.lca(root, List.of(node_5, node_7));
        System.out.println("LCA for 5 and 7 : "+lca.value);

        Node lca2 = obj.lca(root, List.of(node_11, node_9));
        System.out.println("LCA for 11 and 9 : "+lca2.value);

        Node lca3 = obj.lca(root, List.of(node_8, node_9));
        System.out.println("LCA for 8 and 9 : "+lca3.value);

        Node lca4 = obj.lca(root, List.of(node_2, node_5));
        System.out.println("LCA for 2 and 5 : "+lca4.value);

        Node lca5 = obj.lca(root, List.of(node_8, node_9, node_7, node_11));
        System.out.println("LCA for 8 , 9, 7 and 11 : "+lca5.value);

        Node lca6 = obj.lca(root, List.of(node_8, node_9, node_7, node_4));
        System.out.println("LCA for 8 , 9, 7 and 4 : "+lca6.value);

    }

    /**
     * Approach: sum the count of all matching nodes.. Eg. if decendents.contains(root) then count is 1.
     * Similarly sum all the counts for children.. at any node if all the values are found i.e sum equals
     * the given descendents size.. then that is the LCA(result node).
     * */

    Node lca = null;
    Map<Node, Integer> decendentCount = null;
    public Node lca(Node root, List<Node> nodes){
        lca = null;
        decendentCount = new HashMap<>();
        traverse(root, new HashSet<>(nodes));
        return lca;
    }

    private void traverse(Node root, Set<Node> nodes){
        if(root == null || lca != null){
            return;
        }
        decendentCount.put(root, nodes.contains(root) ? 1 : 0);
        for(Node child : root.children){
            traverse(child, nodes);
            decendentCount.put(root,
                        decendentCount.get(root) + decendentCount.getOrDefault(child, 0));
        }
        if(lca==null && decendentCount.get(root) == nodes.size()){
            lca = root;
        }
    }
}

class Node{
    int value;
    List<Node> children = new ArrayList<>();
    public Node(int value){
        this.value = value;
    }
}
