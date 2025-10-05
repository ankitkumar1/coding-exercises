package ankit.dsa;

import java.util.*;

public class Tekion2 {
    public static void main(String[] args) {
        //int[][] exams= new int[][]{{1,0}, {2, 0}, {3,1}, {3,2}};
        //int[][] exams= new int[][]{{1,0}, {2, 0}, {3,1}, {3,2}, {0, 3}};
        int[][] exams= new int[][]{{1,0}, {3,1}, {3,2}, {20, 1}, {3, 20}};
        List<Integer> result = getExamOrder(exams);
        System.out.println(result);
    }

    public static List<Integer> getExamOrder(int[][] exams){
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> indegree = new HashMap<>();
        buildGraphWithIndegree(exams, graph, indegree);

        // BFS
        Queue<Integer> queue = new LinkedList<>();
        for(int key : indegree.keySet()){
            if(indegree.get(key) == 0){
                queue.offer(key);
            }
        }

        Set<Integer> visited = new HashSet<>();

        List<Integer> result = new ArrayList<>();

        while(!queue.isEmpty()){
            int currentExam = queue.poll();
            if(!visited.add(currentExam)){
                continue;
            }
            result.add(currentExam);
            for(int dependentExam : graph.getOrDefault(currentExam, Collections.emptyList())){
                indegree.put(dependentExam, indegree.get(dependentExam)-1);
                if(indegree.get(dependentExam) == 0){
                    queue.offer(dependentExam);
                }
            }
        }

        return indegree.size() == result.size() ? result : List.of(-1);
    }

    private static Map<Integer, List<Integer>> buildGraphWithIndegree(int[][] exams,
                                                               Map<Integer, List<Integer>> graph,
                                                               Map<Integer, Integer> indegree) {
        for(int[] exam : exams){
            indegree.put(exam[0], 0);
            indegree.put(exam[1], 0);
        }
        for(int[] exam : exams){
            graph.putIfAbsent(exam[1], new ArrayList<>());
            graph.get(exam[1]).add(exam[0]);
            indegree.put(exam[0], indegree.get(exam[0])+1);
        }
        return graph;
    }
}

/**
 * 2D array
 * [[1,0],[2,0],[3,1],[3,2]]
 * ids- it could be number or alphanumeric.
 * to cover exam 10 need to cover 20 first.
 *
 * 0 -> 1, 2
 * 1 -> 3
 * 2 -> 3
 *
 * 0 : 0
 * 1 : 0
 * 2 : 0
 * 3 : 0
 * 0, 1, 2, 3
 * 0, 2, 1, 3
 * */
