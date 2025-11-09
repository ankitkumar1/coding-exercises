package ankit.dsa.company_confl.ide_autocomplete;

import java.util.ArrayList;
import java.util.List;

public class NaiveAutoComplete extends AutoComplete{

    public NaiveAutoComplete(List<String> classNames){
        super(classNames);
    }

    @Override
    public List<String> autoComplete(String pattern){
        var result = new ArrayList<String>();
        for(String className : classNames){
            if(matches(className, pattern)){
                result.add(className);
            }
        }
        return result;
    }

    private boolean matches(String className, String pattern) {
        var parts = splitPattern(pattern);
        var segments = splitPattern(className);
        for(int i=0; i<parts.size(); i++){
            if(i >= segments.size() || !segments.get(i).startsWith(parts.get(i))){
                return false;
            }
        }
        return true;
    }

}
