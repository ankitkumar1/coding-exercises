package ankit.lld.spreadsheetautofill;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        AutoFillEngine<Double> engine = new AutoFillEngine<>(List.of(
                new ArithmeticDetector(),
                new RepeatLastDetector<>()
        ));

        System.out.println(engine.fill(List.of(1.0, 3.0, 5.0), 5)); // 7,9,11,13,15
        System.out.println(engine.fill(List.of(10.0, 10.0), 4));     // 10,10,10,10
        System.out.println(engine.fill(List.of(42.0), 3));           // 42,42,42 (single seed)
        System.out.println(engine.fill(List.of(-2.0, 0.0, 2.0), 3)); // 4,6,8 (step +2)
        System.out.println(engine.fill(List.of(42.0, 60.0, 61.0), 3));

        AutoFillEngine<String> stringEngine = new AutoFillEngine<>(List.of(
                new MonthDetector(),
                new RepeatLastDetector<>()
        ));

        System.out.println(stringEngine.fill(List.of("feb", "mar"), 5));
        System.out.println(stringEngine.fill(List.of("january", "february"), 4));     // 10,10,10,10
        System.out.println(stringEngine.fill(List.of("october", "november"), 4));           // 42,42,42 (single seed)
    }
}
