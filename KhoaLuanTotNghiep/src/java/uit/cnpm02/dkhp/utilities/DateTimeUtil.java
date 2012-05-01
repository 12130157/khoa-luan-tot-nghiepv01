/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thanh
 */
public class DateTimeUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PARTERM_DEFAULT);
    public static String format(Date date){
        if(date == null)
            return "";
        else return sdf.format(date);
    }
    public static Date parse(String date){
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(DateTimeUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
