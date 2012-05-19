package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * HỌC HÀM
 * @author LocNguyen
 */
public enum HocHam {
    NOTHING(0, "X"),
    GS(1, "GS"),
    PGS(2, "P. GS");
    //..
    private int value;
    private String description;

    HocHam(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, HocHam> lookup_on_index 
            = new HashMap<Integer, HocHam> ();
    private static Map<String, HocHam> lookup_on_text 
            = new HashMap<String, HocHam> ();
    static {
        for (HocHam st : EnumSet.allOf(HocHam.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static HocHam getHocHam(int value) {
        return lookup_on_index.get(value);
    }
    
    public static HocHam getHocHam(String value) {
        return lookup_on_text.get(value);
    }
}
