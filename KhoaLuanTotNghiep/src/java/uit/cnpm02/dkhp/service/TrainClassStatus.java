package uit.cnpm02.dkhp.service;

/**
 *
 * @author LocNguyen
 */
public enum TrainClassStatus {
    ALL(0, "All"),
    OPEN(1, "Opened"), //
    CANCEL(2, "Canceled"),
    CLOSE(3, "Closed");
    
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
    
}
