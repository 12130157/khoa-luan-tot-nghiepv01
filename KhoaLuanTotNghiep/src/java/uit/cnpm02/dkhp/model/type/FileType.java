package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum FileType {
    SCORE_SHEET(0, "Bảng điểm SV"),
    LIST_STUDENT(1, "Danh sách SV"),
    LIST_LECTURER(2, "Danh sách GV"),
    UNKNOWN(3, "KXĐ");
    //..
    private int value;
    private String description;

    FileType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, FileType> lookup_on_index 
            = new HashMap<Integer, FileType> ();
    private static Map<String, FileType> lookup_on_text 
            = new HashMap<String, FileType> ();
    static {
        for (FileType st : EnumSet.allOf(FileType.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static FileType getFileType(int value) {
        return lookup_on_index.get(value);
    }
    
    public static FileType getFileType(String value) {
        return lookup_on_text.get(value);
    }
}
