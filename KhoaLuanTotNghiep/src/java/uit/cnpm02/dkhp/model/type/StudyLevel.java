package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum StudyLevel {
    UNIVERSITY(0, "Đại học"),
    COLLEG(1, "Cao đẳng");
    //.. other
    private int value;
    private String description;

    StudyLevel(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, StudyLevel> lookup_on_index 
            = new HashMap<Integer, StudyLevel> ();
    private static Map<String, StudyLevel> lookup_on_text 
            = new HashMap<String, StudyLevel> ();
    static {
        for (StudyLevel st : EnumSet.allOf(StudyLevel.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static StudyLevel getStudyStatus(int value) {
        return lookup_on_index.get(value);
    }
    
    public static StudyLevel getStudyStatus(String value) {
        return lookup_on_text.get(value);
    }
}
