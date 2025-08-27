package ankit.dsa;

import java.util.*;

/**
 * First I took PriorityQueue but PriorityQueue takes O(n) time for deletion.
 * so TreeSet would be better choice, it takes O(logn) for deletion.
 * */
public class FoodRatingSystemLC2353 {

    Map<String, TreeSet<FoodRating>> cuisineMap = new HashMap<>();
    Map<String, FoodRating> foodMap = new HashMap<>();

    Comparator<FoodRating> comparator = (r1, r2) -> {
        if(r1.rating == r2.rating){
            return r1.food.compareTo(r2.food);
        }
        return Integer.compare(r2.rating, r1.rating);
    };

    public FoodRatingSystemLC2353(String[] foods, String[] cuisines, int[] ratings) {
        for(int i = 0; i<foods.length; i++){
            cuisineMap.putIfAbsent(cuisines[i], new TreeSet<>(comparator));
            FoodRating foodRating = new FoodRating(foods[i], cuisines[i], ratings[i]);
            foodMap.put(foods[i], foodRating);
            cuisineMap.get(cuisines[i]).add(foodRating);
        }
    }

    public void changeRating(String food, int newRating) {
        FoodRating foodRating = foodMap.get(food);
        cuisineMap.get(foodRating.getCuisine()).remove(foodRating);
        foodRating.setRating(newRating);
        cuisineMap.get(foodRating.getCuisine()).add(foodRating);
    }

    public String highestRated(String cuisine) {
        return cuisineMap.get(cuisine).getFirst().getFood();
    }

    static class FoodRating{
        private String food;
        private String cuisine;
        private int rating;
        public FoodRating(String food, String cuisine, int rating){
            this.food = food;
            this.cuisine = cuisine;
            this.rating = rating;
        }

        public String getFood(){
            return this.food;
        }

        public String getCuisine(){
            return this.cuisine;
        }

        public void setRating(int rating){
            this.rating = rating;
        }
    }

    public static void main(String[] args) {
        String[] foods = new String[]{"kimchi","miso","sushi","moussaka","ramen","bulgogi"};
        String[] cuisines = new String[]{"korean","japanese","japanese","greek","japanese","korean"};
        int[] ratings = new int[]{9,12,8,15,14,7};
        FoodRatingSystemLC2353 obj = new FoodRatingSystemLC2353(foods, cuisines, ratings);

        System.out.println(obj.highestRated("korean")); // kimchi
        System.out.println(obj.highestRated("japanese")); // ramen
        obj.changeRating("sushi",16);
        System.out.println(obj.highestRated("japanese")); // sushi
        obj.changeRating("ramen",16);
        System.out.println(obj.highestRated("japanese")); // ramen

    }
    // japanese - miso 12, sushi- 8, ramen- 14
}
