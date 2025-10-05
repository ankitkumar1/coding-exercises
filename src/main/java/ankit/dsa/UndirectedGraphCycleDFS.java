package ankit.dsa;

import java.util.ArrayList;
import java.util.List;

public class UndirectedGraphCycleDFS {
    public boolean isCycle(int V, int[][] edges) {
        // Code here
        boolean[] visited = new boolean[V];
        List<List<Integer>> graph = buildGraph(V, edges);
        for(int i=0; i<V; i++){
            if(!visited[i] && cycleFound(i, -1, graph, visited)){
                return true;
            }
        }
        return true;
    }

    private boolean cycleFound(int vertex, int parent, List<List<Integer>> graph,
                               boolean[] visited){
        if(visited[vertex]){
            return true;
        }
        visited[vertex] = true;
        for(int neighbour : graph.get(vertex)){
            if(neighbour == parent){
                continue;
            }
            if(cycleFound(neighbour, vertex, graph, visited)){
                return true;
            }
        }
        return false;
    }

    private List<List<Integer>> buildGraph(int V, int[][] edges){
        List<List<Integer>> graph = new ArrayList<List<Integer>>();
        for(int i=0; i<V; i++){
            graph.add(new ArrayList<>());
        }
        for(int[] edge : edges){
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        return graph;
    }
}
