package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum TaskStatus {
    TOBE_PROCESS(0, "Chưa xử lý"),
    PROCESSED(1, "Đã xử lý"),
    UNKNOWN(1, "KXĐ");
    //..
    private int value;
    private String description;

    TaskStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, TaskStatus> lookup_on_index 
            = new HashMap<Integer, TaskStatus> ();
    private static Map<String, TaskStatus> lookup_on_text 
            = new HashMap<String, TaskStatus> ();
    static {
        for (TaskStatus st : EnumSet.allOf(TaskStatus.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static TaskStatus getTaskStatus(int value) {
        return lookup_on_index.get(value);
    }
    
    public static TaskStatus getTaskStatus(String value) {
        return lookup_on_text.get(value);
    }
}
