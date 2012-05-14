package uit.cnpm02.dkhp.model.type;

/**
 *
 * @author LocNguyen
 */
public enum AccountStatus {

    NORMAL(0, "Bình thường"),
    LOCKED(1, "Bị khóa");
    private int value;
    private String description;

    AccountStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return this.value;
    }
    
    public String description() {
        return description;
    }
    
    public static String getDescription(int value) {
        if (value == NORMAL.value()) {
            return NORMAL.description();
        } else if (value == LOCKED.value()) {
            return LOCKED.description();
        }
        
        return "KXĐ";
    }
}
