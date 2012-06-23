package uit.cnpm02.dkhp.service;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum TrainClassStatus {
    ALL(0, "All"),
    OPEN(1, "Opened"), //
    CLOSE(2, "Closed"),
    CANCEL(3, "Canceled"); // Don't remove this enum please, just don't use...
    
    private int value;
    private String description;
    
    /**
     * Initial constructor
     * @param value value
     * @param description description string
     */
    TrainClassStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    private static Map<Integer, TrainClassStatus> lookup_on_index 
            = new HashMap<Integer, TrainClassStatus> ();
    private static Map<String, TrainClassStatus> lookup_on_text 
            = new HashMap<String, TrainClassStatus> ();
    static {
        for (TrainClassStatus st : EnumSet.allOf(TrainClassStatus.class)) {
            lookup_on_index.put(st.getValue(), st);
            lookup_on_text.put(st.getDescription(), st);
        }
    }
    
    public static TrainClassStatus getTrainClassStatus(int value) {
        return lookup_on_index.get(value);
    }
    
    public static TrainClassStatus getTrainClassStatus(String value) {
        return lookup_on_text.get(value);
    }
    
}
