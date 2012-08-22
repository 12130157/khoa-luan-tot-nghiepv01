/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author LocNguyen
 */
public class BOUtils {
    private final static String ROOM_LIST = "room.list";
    private final static String CURRENT_SEMETER = "current.semeter";
    private final static String CURRENT_YEAR = "current.year";

    private static boolean doLoadConfig = false;

    private static List<String> roomList = new ArrayList(10);
    private static int currentSemeter = -1;
    private static String currentYear = "";
    private static Properties config = null;
    
    
    public BOUtils() {
    }
    
    public static List<String> getListRool() {
        loadConfig();
        return roomList;
    }
    
    private static void loadConfig() {
        if (!doLoadConfig) {
            config = new Properties();
            try {
                config.load(new FileInputStream("config/system.properties"));
                String listClass = config.get(ROOM_LIST).toString();

                roomList = Arrays.asList(listClass.split(","));

                currentSemeter = Integer.parseInt(config.get(CURRENT_SEMETER).toString());
                currentYear = config.get(CURRENT_YEAR).toString();
                
                // All other
                System.setProperties(config);
            } catch (IOException ex) {
                Logger.getLogger(BOUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            doLoadConfig = true;
        }
        
    }
    
    public static boolean getBooleanValue(String key, boolean defauleValue) {
        try {
            String value = System.getProperty(key);
            return Boolean.parseBoolean(value);
        } catch (Exception ex) {
            return defauleValue;
        }
    }
    
    public static boolean getBooleanValue(String key) {
        return getBooleanValue(key, false);
    }
    
    public static void reLoadConfig() {
        doLoadConfig = false;
        loadConfig();
    }
    
    public static void setConfig(Properties prop) {
        BOUtils.config = prop;
    }
    
    public static Properties getConfig() {
        return config;
    }
    
    public static List<String> getListRoom(List<String> defaultValue) {
        loadConfig();
        return roomList.isEmpty() ? defaultValue : roomList;
    }
    public static int getCurrentSemeter(int defaultValue) {
        loadConfig();
        return currentSemeter < 0 ? defaultValue : currentSemeter;
    }
    
    public static String getCurrentYear(String defaultValues) {
        loadConfig();
        return currentYear.isEmpty() ? defaultValues : currentYear;
    }
    
    public static void writeResponse(String jsonData, ServletRequest req, ServletResponse res) {

        PrintWriter printer;
        try {
            printer = res.getWriter();
            res.setContentType("application/x-javascript; charset=utf-8");
            String callback = req.getParameter("jsoncallback");
            if (callback != null) {
                printer.write(callback + "(");
            }
            printer.write(jsonData);
            if (callback != null) {
                printer.write(")");
            }
            printer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
