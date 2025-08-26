package ankit.lld.spreadsheetautofill;

import java.util.List;
import java.util.Optional;

public class ArithmeticDetector implements PatternDetector<Double> {

    @Override
    public Optional<PatternMatch<Double>> detect(List<Double> seeds) {
        if (seeds.isEmpty()) return Optional.empty();

        // If only single seed value or all values are same then treat is as constant.
        if (seeds.size() == 1 || seeds.stream().distinct().count() == 1) {
            double c = seeds.getFirst();
            return Optional.of(new PatternMatch<>("CONSTANT", 0.70, offset -> c));
        }

        // Check if differences are same (Arithmetic Progression)→ ARITHMETIC
        boolean equalDifference = true;
        double step = seeds.get(1) - seeds.get(0);
        for(int i=2; i<seeds.size(); i++){
            double diff = seeds.get(i) - seeds.get(i-1);
            if (diff != step) {
                equalDifference = false;
                break;
            }
        }
        if(equalDifference){
            double last = seeds.getLast();
            PatternGenerator<Double> g = offset -> last + (offset + 1) * step;
            return Optional.of(new PatternMatch<>("ARITHMETIC", 0.90, g));
        }

        return Optional.empty();
    }
}
