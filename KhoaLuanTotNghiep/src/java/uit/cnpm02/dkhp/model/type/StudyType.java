package uit.cnpm02.dkhp.model.type;

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
}
