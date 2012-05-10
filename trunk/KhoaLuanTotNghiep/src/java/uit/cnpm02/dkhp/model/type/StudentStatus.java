package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum StudentStatus {
    NORMAL(0, "Bình thường"),
    NON_STUDING(1, "Bị đình chỉ học");
    //..
    private int value;
    private String description;

    StudentStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, StudentStatus> lookup_on_index 
            = new HashMap<Integer, StudentStatus> ();
    private static Map<String, StudentStatus> lookup_on_text 
            = new HashMap<String, StudentStatus> ();
    static {
        for (StudentStatus st : EnumSet.allOf(StudentStatus.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static StudentStatus getSubjectType(int value) {
        return lookup_on_index.get(value);
    }
    
    public static StudentStatus getSubjectType(String value) {
        return lookup_on_text.get(value);
    }
}
