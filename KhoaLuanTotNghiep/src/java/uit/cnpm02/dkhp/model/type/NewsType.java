package uit.cnpm02.dkhp.model.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public enum NewsType {
    OPEN(0),
    IMPORTANT(1),
    CLOSED(2);
    
    //..
    private int value;

    NewsType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    private static Map<Integer, NewsType> lookup_on_index 
            = new HashMap<Integer, NewsType> ();
    static {
        for (NewsType st : EnumSet.allOf(NewsType.class)) {
            lookup_on_index.put(st.value(), st);
        }
    }
    
    public static NewsType getNewsType(int value) {
        return lookup_on_index.get(value);
    }    
}
