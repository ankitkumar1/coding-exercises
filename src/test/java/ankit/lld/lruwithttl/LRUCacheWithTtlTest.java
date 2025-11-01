package ankit.lld.lruwithttl;

import ankit.lld.lru.lruwithttl.LRUCacheWithTtl;
import org.junit.jupiter.api.Test;

public class LRUCacheWithTtlTest {

    @Test
    public void testGetAndPutWithoutTtl(){
        LRUCacheWithTtl withOutTtl = new LRUCacheWithTtl(3);
        withOutTtl.put(1, 1, -1);
        withOutTtl.put(2, 2, -1);
        withOutTtl.put(3, 3, -1);
        System.out.println(withOutTtl.get(1));
        withOutTtl.put(4, 4, -1);
        System.out.println(withOutTtl.get(2));
        System.out.println(withOutTtl.get(3));

        System.out.println(withOutTtl.getAverage());

    }
}
