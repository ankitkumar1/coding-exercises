package ankit.lld.spreadsheetautofill;

import java.util.List;
import java.util.Optional;

public interface PatternDetector<T> {
    Optional<PatternMatch<T>> detect(List<T> seeds);
}
