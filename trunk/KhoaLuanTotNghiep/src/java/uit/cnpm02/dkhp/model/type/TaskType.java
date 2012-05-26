package uit.cnpm02.dkhp.model.type;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum TaskType implements Serializable {
    GV_TB_NGHI_DAY(0),
    UNKNOWN(1);
    
    //..
    private int value;

    TaskType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    private static Map<Integer, TaskType> lookup_on_index 
            = new HashMap<Integer, TaskType> ();
    static {
        for (TaskType st : EnumSet.allOf(TaskType.class)) {
            lookup_on_index.put(st.value(), st);
        }
    }
    
    public static TaskType getTaskType(int value) {
        return lookup_on_index.get(value);
    }
    
}
