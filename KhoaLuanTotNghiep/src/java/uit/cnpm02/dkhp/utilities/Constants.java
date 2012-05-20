package uit.cnpm02.dkhp.utilities;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author LocNguyen
 */
public class Constants {

    /**Account type**/
    public static int ACCOUNT_TYPE_LECTURE = 2;
    public static int ACCOUNT_TYPE_PDT = 1;
    public static int ACCOUNT_TYPE_STUDENT = 3;
    public static String DATETIME_PARTERM_DEFAULT = "yyyy-MM-dd";
    /**News's type**/
    public static int NEWS_TYPE_READ = 0;
    public static int NEWS_TYPE_UNREAD = 1;
    /**Student's type**/
    public static int STUDENT_STATUS_NORMAL = 0;
    public static int STUDENT_STATUS_reservations = 1;
    //...
    /**Define number row tobe showed on a page**/
    public static int ELEMENT_PER_PAGE_DEFAULT = 10;
    public static int CURRENT_SEMESTER = 2;
    public static String CURRENT_YEAR = "2011-2012";
    public static List<String> ROOM_LISS = Arrays.asList(new String[]{"101","102","103"});
    static {
        CURRENT_SEMESTER = BOUtils.getCurrentSemeter(2);
        //Should get current year...
        //
        CURRENT_YEAR = BOUtils.getCurrentYear("2011-2012");
        ROOM_LISS = BOUtils.getListRoom(Arrays.asList(new String[]{"101","102","103"}));
    }
    
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;
    public static int SUNDAY = 8;
    /**Some page allowed to show more then default value
    you should define below session**/
    //...
    public static String LABEL_CLASS = "lbl_clazz";
    public static String LABEL_COURSE = "lbl_course";
    public static String LABEL_FACULTY = "lbl_faculty";
    public static boolean INTIME_REGISTRY = true;
    
    public final static String FILEUPLOAD_DIR = "upload";
}
