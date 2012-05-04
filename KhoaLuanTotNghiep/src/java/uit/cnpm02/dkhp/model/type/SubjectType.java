package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
/**
 * Definition for subject type.
 */
public enum SubjectType {

    REQUIRED(0, "Bắt buộc"),
    SELECTIVE(1, "Tự chọn");
    private int value;
    private String description;

    SubjectType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, SubjectType> lookup_on_index = new HashMap<Integer, SubjectType> ();
    private static Map<String, SubjectType> lookup_on_text = new HashMap<String, SubjectType> ();
    static {
        for (SubjectType st : EnumSet.allOf(SubjectType.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static SubjectType getSubjectType(int value) {
        return lookup_on_index.get(value);
    }
    
    public static SubjectType getSubjectType(String value) {
        return lookup_on_text.get(value);
    }
}