package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum FileUploadStatus {
    NOT_PROCESS(0, "Chưa xử lý"),
    PROCESSED(1, "Đã xử lý"),
    CANCEL(2, "Bị hủy"),
    UNKNOWN(3, "KXĐ");
    //..
    private int value;
    private String description;

    FileUploadStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int value() {
        return value;
    }

    public String description() {
        return this.description;
    }
    
    private static Map<Integer, FileUploadStatus> lookup_on_index 
            = new HashMap<Integer, FileUploadStatus> ();
    private static Map<String, FileUploadStatus> lookup_on_text 
            = new HashMap<String, FileUploadStatus> ();
    static {
        for (FileUploadStatus st : EnumSet.allOf(FileUploadStatus.class)) {
            lookup_on_index.put(st.value(), st);
            lookup_on_text.put(st.description(), st);
        }
    }
    
    public static FileUploadStatus getFileUploadStatus(int value) {
        return lookup_on_index.get(value);
    }
    
    public static FileUploadStatus getFileUploadStatus(String value) {
        return lookup_on_text.get(value);
    }
}
