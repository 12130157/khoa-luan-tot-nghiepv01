package uit.cnpm02.dkhp.utilities;

import java.util.List;

/**
 * Most functions are copied from Apache common lang - StringUtils
 * 
 */
public class StringUtils {

    /**
     * @param str The string to be checked.
     * @return true if empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    /**
     * @param str The string to be checked.
     * @return true if empty or null
     */
    public static boolean isEmpty(String[] str) {
        return str == null || str.length <= 0;
    }

    /**
     * 
     * @param str The string to be checked.
     * @return if true if not empty or null.
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * @param text The input text.
     * @param searchString The search string.
     * @param replacement The replacement.
     * @return String The replaced input text.
     */
    public static String replaceOnce(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * @param text The input text
     * @param searchString The search string to be replaced.
     * @param replacement The replacement.
     * @return String
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * @param text The input text.
     * @param searchString The search string to be replaced.
     * @param replacement The replacement.
     * @param max The max number of replacement. 
     * @return String
     */
    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static boolean checkStringExitList(String value, List<String> objects) {
        if ((objects == null) || objects.isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).equalsIgnoreCase(value))
                return true;
        }
        
        return false;
    }
    
    public static String StripHTML(String source) {
        return source.replaceAll("\\<.*?>", "");
        //source.replaceAll("\<.*?>", "");
}
}
