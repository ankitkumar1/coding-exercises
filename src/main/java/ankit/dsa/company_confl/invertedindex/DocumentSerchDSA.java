package ankit.dsa.company_confl.invertedindex;

import java.util.*;
import java.util.regex.Pattern;

public class DocumentSerchDSA {

    private Pattern PATTERN = Pattern.compile("\\W+") ;
    private final Map<String, Map<Integer, List<Integer>>> indexMap;
    public DocumentSerchDSA(){
        indexMap = new HashMap<>();
    }

    public void indexDocument(int docId, String content){
        String[] words = PATTERN.split(content.toLowerCase());
        for(int i=0; i<words.length; i++){
            String word = words[i];
            indexMap
                    .computeIfAbsent(word, k -> new HashMap<>())
                    .computeIfAbsent(docId, k -> new ArrayList<>()).add(i);
        }
    }

    public List<Integer> search(String searchText){
        if(searchText == null || searchText.isBlank()){
            return List.of();
        }
        String[] words = PATTERN.split(searchText.toLowerCase());
        if(words.length == 1){
            return searchWord(words[0]);
        }
        return searchPhrase(words);
    }

    private List<Integer> searchPhrase(String[] words) {
        // Step1: Get all doc Ids, which contains all the words.
        var docIds = new HashSet<>(indexMap.get(words[0]).keySet());
        for(int i=1; i<words.length; i++){
            docIds.retainAll(indexMap.get(words[i]).keySet());
        }

        var result = new ArrayList<Integer>();
        for(int docId : docIds){
            if(phrasePresentInDoc(words, docId)){
                result.add(docId);
            }
        }
        return result;
    }

    private boolean phrasePresentInDoc(String[] words, int docId) {
        var wordIndicesInDoc = new ArrayList<List<Integer>>();
        for(String word : words){
            wordIndicesInDoc.add(indexMap.get(word).get(docId));
        }

        for(int index : wordIndicesInDoc.getFirst()){
            boolean found = true;
            int nextIndex = index+1;
            for(int i=1; i<wordIndicesInDoc.size(); i++){
                if(!wordIndicesInDoc.get(i).contains(nextIndex)){
                    found = false;
                    break;
                }
                ++nextIndex;
            }
            if(found){
                return true;
            }
        }
        return false;
    }

    private List<Integer> searchWord(String word){
        return new ArrayList<>(indexMap.get(word).keySet());
    }


    public static void main(String[] args) {
        //System.out.println(Arrays.asList("I am here!".split("\\W+")));
        //System.out.println(Arrays.asList("I am here123! &^***".split("\\s+")));
        //System.out.println(Arrays.asList("I am here123!   &^***".split("\\s")));

        DocumentSerchDSA documentSearch = new DocumentSerchDSA();
        documentSearch.indexDocument(1, "Cloud computing is the on-demand availability of computer system resources Cloud computing for.");
        documentSearch.indexDocument(2, "One integrated service for metrics uptime cloud monitoring Cloud computing  for dashboards and alerts reduces time spent navigating between systems.");
        documentSearch.indexDocument(3, "Cloud computing for Monitor entire cloud infrastructure, whether in the cloud computing!! is or in virtualized data centers.");

        System.out.println(documentSearch.search("cloud"));
        System.out.println(documentSearch.search("cloud monitoring"));
        System.out.println(documentSearch.search("cloud computing is"));
        System.out.println(documentSearch.search("Cloud computing for"));
    }



}
