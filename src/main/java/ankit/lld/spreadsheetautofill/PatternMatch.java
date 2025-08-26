package ankit.lld.spreadsheetautofill;

public record PatternMatch<T>(String name, double score, PatternGenerator<T> generator) {
}
