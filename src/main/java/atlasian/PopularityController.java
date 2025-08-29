package atlasian;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class PopularityController {

    private long version = 0;

    private final Map<String, Content> idToContentMap = new HashMap<>();
    private final TreeSet<Content> sortedContent =
                    new TreeSet<>((c1, c2) -> {
                        if(c1.getRating() == c2.getRating()){
                            return Long.compare(c2.getVersion(), c1.getVersion());
                        }
                        return Integer.compare(c2.getRating(), c1.getRating());
                    });

    public void changePopularity(ContentAction action){
        Content content = idToContentMap.get(action.id());
        if(content == null){
            content = new Content(action.id());
            idToContentMap.put(action.id(), content);
        }else{
            sortedContent.remove(content);
        }
        if(action.action() == 1){
            content.incrementRating();
        }else{
            content.decrementRating();
        }
        content.setVersion(++this.version);
        sortedContent.add(content);
    }

    public String getPopularContent(){
        if(!sortedContent.isEmpty()){
            return sortedContent.getFirst().getId();
        }
        return "";
    }


    public static void main(String[] args) {
        PopularityController obj = new PopularityController();

        System.out.println(obj.getPopularContent());

        obj.changePopularity(new ContentAction("id1", 1));
        obj.changePopularity(new ContentAction("id2", 1));
        obj.changePopularity(new ContentAction("id3", 1));
        obj.changePopularity(new ContentAction("id2", 1));

        System.out.println(obj.getPopularContent());

        obj.changePopularity(new ContentAction("id3", 1));
        //obj.changePopularity(new ContentAction("id3", 1));
        System.out.println(obj.getPopularContent());
    }
}

