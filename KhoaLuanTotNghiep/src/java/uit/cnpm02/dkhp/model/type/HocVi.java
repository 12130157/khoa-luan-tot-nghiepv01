package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * HỌC VỊ
 * @author LocNguyen
 */
public enum HocVi {
    NOTHING(0, "X"),
    ThS(1, "Thạc sỹ"),
    TS(2, "Tiến sĩ"),
    TSKH(3, "TS Khoa học"),
    CN(4, "Cử nhân"),
    CH(5, "Cao học");
    //..
    private int value;
    private String description;

    HocVi(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, HocVi> lookup_on_index 
            = new HashMap<Integer, HocVi> ();
    private static Map<String, HocVi> lookup_on_text 
            = new HashMap<String, HocVi> ();
    static {
        for (HocVi st : EnumSet.allOf(HocVi.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static HocVi getHocVi(int value) {
        return lookup_on_index.get(value);
    }
    
    public static HocVi getHocVi(String value) {
        return lookup_on_text.get(value);
    }
}
