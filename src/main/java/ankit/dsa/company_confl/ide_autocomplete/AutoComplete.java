package ankit.dsa.company_confl.ide_autocomplete;

import java.util.ArrayList;
import java.util.List;

public abstract class AutoComplete {

    protected final List<String> classNames;

    public AutoComplete(List<String> classNames){
        this.classNames = classNames;
    }

    public abstract List<String> autoComplete(String pattern);

    /*
     * RPr : R , Pr
     * RP  : R , P
     * RePr : Re , Pr
     * */
    public static List<String> splitPattern(String pattern){
        var parts = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<pattern.length(); i++){
            char c = pattern.charAt(i);
            if(Character.isUpperCase(c)){
                if(!builder.isEmpty()){
                    parts.add(builder.toString());
                    builder = new StringBuilder();
                }
            }
            builder.append(c);
        }
        parts.add(builder.toString());
        return parts;
    }
}
