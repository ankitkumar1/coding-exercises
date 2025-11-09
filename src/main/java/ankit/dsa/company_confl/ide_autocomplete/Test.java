package ankit.dsa.company_confl.ide_autocomplete;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        AutoComplete obj = new TrieBasedAutoComplete(List.of("Container", "AutoPanel", "RidePrinter",
                "ResumePanel", "RegularContainer", "RegularPanel", "ResultPlan"));
        /*System.out.println(obj.splitPattern("RP"));
        System.out.println(obj.splitPattern("RPe"));
        System.out.println(obj.splitPattern("RePe"));
        System.out.println(obj.splitPattern("Rxy"));

        System.out.println(obj.splitPattern("Container"));
        System.out.println(obj.splitPattern("AutoPanel"));
        System.out.println(obj.splitPattern("RidePrinter"));
        System.out.println(obj.splitPattern("NaiveAutoComplete"));*/

        System.out.println(obj.autoComplete("R")); // "ResumePanel", "RegularContainer", "RidePrinter"
        System.out.println(obj.autoComplete("Re")); // "ResumePanel", "RegularContainer"
        System.out.println(obj.autoComplete("RP")); // "ResumePanel", "RidePrinter"
        System.out.println(obj.autoComplete("RPr")); // "RidePrinter"
        System.out.println(obj.autoComplete("ReP")); // "ResumePanel" RegularPanel ResultPlan
        System.out.println(obj.autoComplete("ResulP"));

    }
}
