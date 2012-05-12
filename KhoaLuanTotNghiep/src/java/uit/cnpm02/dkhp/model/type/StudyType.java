package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum StudyType {
    GROUP(0, "Tập chung"),
    ON_NETWORK(1, "Đào tạo từ xa");
    //.. other
    private int value;
    private String description;

    StudyType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, StudyType> lookup_on_index 
            = new HashMap<Integer, StudyType> ();
    private static Map<String, StudyType> lookup_on_text 
            = new HashMap<String, StudyType> ();
    static {
        for (StudyType st : EnumSet.allOf(StudyType.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static StudyType getStudyType(int value) {
        return lookup_on_index.get(value);
    }
    
    public static StudyType getStudyType(String value) {
        return lookup_on_text.get(value);
    }
}
