package ankit.lld.spreadsheetautofill;

public interface PatternGenerator<T> {

    // offset=0 → first value after the seed window
    T next(int offset);
}
