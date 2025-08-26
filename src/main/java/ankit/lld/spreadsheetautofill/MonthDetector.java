package ankit.lld.spreadsheetautofill;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MonthDetector implements PatternDetector<String>{

    private static final List<List<String>> MONTHS = List.of(
            List.of("jan","january"), List.of("feb","february"), List.of("mar","march"),
            List.of("apr","april"),   List.of("may","may"),      List.of("jun","june"),
            List.of("jul","july"),    List.of("aug","august"),   List.of("sep","september"),
            List.of("oct","october"), List.of("nov","november"), List.of("dec","december")
    );

    private int getIndex(String seed){
        seed = seed.toLowerCase();
        for(int i=0; i<MONTHS.size(); i++){
            if(seed.equals(MONTHS.get(i).getFirst()) || seed.equals(MONTHS.get(i).getLast())){
                return i;
            }
        }
        return -1;
    }

    @Override
    public Optional<PatternMatch<String>> detect(List<String> seeds) {
        if(seeds.isEmpty()) return Optional.empty();

        var ids = new ArrayList<Integer>();
        for(String seed : seeds){
            int id = getIndex(seed);
            if(id < 0){
                return Optional.empty();
            }
            ids.add(id);
        }

        int step = 1;
        if(ids.size() >=2){
            step = ((ids.get(1) - ids.getFirst()) % 12 + 12) % 12;
        }
        if(step == 0){
            step = 1;
        }
        int last = ids.getLast();
        boolean shortForm = seeds.getLast().length() == 3;
        int finalStep = step;
        PatternGenerator<String> generator = offset -> {
            // Because of rolling. like nov, dec, jan, feb
            int next = (last + (offset+1) * finalStep) % 12;
            return MONTHS.get(next).get(shortForm? 0 : 1);
        };
        return Optional.of(new PatternMatch<>("MONTHS", 0.95, generator));
    }
}
