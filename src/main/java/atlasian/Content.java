package atlasian;

public class Content {
    private String id;
    private int rating;
    private long version;

    public Content(String id){
        this.id = id;
        this.rating = 0;
    }

    public String getId(){
        return this.id;
    }

    public void incrementRating(){
        this.rating++;
    }

    public void decrementRating(){
        this.rating--;
    }

    public int getRating(){
        return this.rating;
    }

    public void setVersion(long version){
        this.version = version;
    }

    public long getVersion(){
        return this.version;
    }

}
