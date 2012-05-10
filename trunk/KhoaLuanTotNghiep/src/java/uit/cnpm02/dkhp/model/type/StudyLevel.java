package uit.cnpm02.dkhp.model.type;

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
}
