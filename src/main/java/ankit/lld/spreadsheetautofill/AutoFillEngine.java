package ankit.lld.spreadsheetautofill;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AutoFillEngine<T> {

    private final List<PatternDetector<T>> detectors;

    AutoFillEngine(List<PatternDetector<T>> detectors) {
        this.detectors = detectors;
    }

    public List<T> fill(List<T> seed, int count){
        if(seed == null || seed.isEmpty()){
            throw new IllegalArgumentException("Seed is required to generate sequence");
        }

        var match = detectors.stream().map(d -> d.detect(seed))
                .flatMap(Optional::stream)
                .max(Comparator.comparingDouble(PatternMatch::score))
                .orElseThrow(() -> new IllegalArgumentException("No pattern recognise"));

        List<T> sequences = new ArrayList<>(count);
        for(int i=0; i<count; i++){
            sequences.add(match.generator().next(i));
        }
        return sequences;
    }
}
