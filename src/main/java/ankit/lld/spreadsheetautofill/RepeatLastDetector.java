package ankit.lld.spreadsheetautofill;

import java.util.List;
import java.util.Optional;

public class RepeatLastDetector<T> implements PatternDetector<T>{

    @Override public Optional<PatternMatch<T>> detect(List<T> seeds) {
        if (seeds.isEmpty()) return Optional.empty();
        T last = seeds.getLast();
        return Optional.of(new PatternMatch<>("REPEAT_LAST", 0.30, k -> last));
    }
}
