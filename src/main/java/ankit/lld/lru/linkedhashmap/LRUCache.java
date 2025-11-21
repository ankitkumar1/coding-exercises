package ankit.lld.lru.linkedhashmap;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache {

    private LinkedHashMap<Integer, Integer> map = null;

    public LRUCache(int capacity){
        map = new LinkedHashMap<>(){
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest){
                return  size() > capacity;
            }
        };
    }

    public int get(int key) {
        if(!map.containsKey(key)){
            return -1;
        }
        int value = map.remove(key);
        map.put(key, value);
        return value;
    }

    public void put(int key, int value) {
        map.remove(key);
        map.put(key, value);
    }

}
