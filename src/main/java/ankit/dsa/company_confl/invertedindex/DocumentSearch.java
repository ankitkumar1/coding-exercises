package ankit.dsa.company_confl.invertedindex;

import java.util.*;
import java.util.regex.Pattern;

public class DocumentSearch {
    public static void main(String[] args) {
        DocumentSearch documentSearch = new DocumentSearch();
        documentSearch.indexDocument(1, "Cloud computing is the on-demand availability of computer system resources Cloud computing for.");
        documentSearch.indexDocument(2, "One integrated service for metrics uptime cloud monitoring Cloud computing for dashboards and alerts reduces time spent navigating between systems.");
        documentSearch.indexDocument(3, "Cloud computing for Monitor entire cloud infrastructure, whether in the cloud computing is or in virtualized data centers.");

        System.out.println(documentSearch.search("cloud"));
        System.out.println(documentSearch.search("cloud monitoring"));
        System.out.println(documentSearch.search("cloud computing is"));
        System.out.println(documentSearch.search("Cloud computing for"));
    }

    private final IndexStorage storage;
    private final SearchService searchService;

    public DocumentSearch(){
        storage = new DefaultStorage();
        searchService = new SearchServiceImpl(storage);
    }

    public void indexDocument(int docId, String text){
        Tokenizer tokenizer = new DefaultTokenizer();
        var tokens = tokenizer.tokenise(text.toLowerCase(), Constant.PATTERN);
        storage.indexTokens(tokens, docId);
    }

    public List<Integer> search(String searchText){
        return searchService.search(searchText);
    }
}

interface Tokenizer{
    String[] tokenise(String text, Pattern pattern);
}

class DefaultTokenizer implements Tokenizer{
    @Override
    public String[] tokenise(String text, Pattern pattern) {
        return pattern.split(text.toLowerCase());
    }
}

interface SearchService{
    List<Integer> search(String searchText);
}

class SearchServiceImpl implements SearchService{
    private final IndexStorage indexStorage;

    public SearchServiceImpl(IndexStorage indexStorage){
        this.indexStorage = indexStorage;
    }

    @Override
    public List<Integer> search(String searchText){
        if(searchText == null || searchText.isBlank()){
            return List.of();
        }
        String[] words = Constant.PATTERN.split(searchText.toLowerCase());
        if(words.length == 0 || !indexStorage.containsWord(words[0])){
            return List.of();
        }
        if(words.length == 1){
            return wordSearch(words[0]);
        }
        return phraseSearch(words);
    }

    private List<Integer> wordSearch(String word){
        return new ArrayList<>(indexStorage.getAllDocsContainingWord(word));
    }

    private List<Integer> phraseSearch(String[] words){
        var matchedDocIds = new HashSet<>(indexStorage.getAllDocsContainingWord(words[0]));
        for(int i=1; i<words.length; i++){
            if(!indexStorage.containsWord(words[i])){
                return List.of();
            }
            matchedDocIds.retainAll(indexStorage.getAllDocsContainingWord(words[i]));
        }

        var result = new ArrayList<Integer>();
        for(Integer docId : matchedDocIds){
            if(isPhrasePresentInDoc(words, docId)){
                result.add(docId);
            }
        }

        return result;
    }

    private boolean isPhrasePresentInDoc(String[] words, Integer docId) {
        var positions = new ArrayList<List<Integer>>();
        for(String word : words){
            positions.add(new ArrayList<>(indexStorage.getAllOccurrenceInDocument(word, docId)));
        }

        for(int index : positions.getFirst()){
            // Now check if all word found in sequence.
            boolean found = true;
            for(int i=1; i<words.length; i++){
                if(!positions.get(i).contains(index+i)){
                    found = false;
                    break;
                }
            }
            if(found){
                return true;
            }
        }
        return false;
    }
}

interface IndexStorage{
    void indexTokens(String[] tokens, int docId);
    boolean containsWord(String word);

    Set<Integer> getAllDocsContainingWord(String word);

    List<Integer> getAllOccurrenceInDocument(String word, Integer docId);
}

class DefaultStorage implements IndexStorage{
    private final Map<String, Map<Integer, List<Integer>>> indexMap = new HashMap<>();

    @Override
    public void indexTokens(String[] tokens, int docId) {
        for(int i=0; i<tokens.length; i++){
            indexMap.putIfAbsent(tokens[i], new HashMap<>());
            indexMap.get(tokens[i]).putIfAbsent(docId, new ArrayList<>());
            indexMap.get(tokens[i]).get(docId).add(i);
        }
    }

    @Override
    public boolean containsWord(String word) {
        return indexMap.containsKey(word);
    }

    @Override
    public Set<Integer> getAllDocsContainingWord(String word) {
        return indexMap.containsKey(word) ? indexMap.get(word).keySet() : Set.of();
    }

    @Override
    public List<Integer> getAllOccurrenceInDocument(String word, Integer docId) {
        return indexMap.containsKey(word) ? indexMap.get(word).get(docId) : List.of();
    }

}

interface Constant{
    Pattern PATTERN = Pattern.compile("\\W+");
}


