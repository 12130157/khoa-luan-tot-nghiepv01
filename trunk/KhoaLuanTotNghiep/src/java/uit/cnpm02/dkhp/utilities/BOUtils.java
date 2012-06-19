/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            } catch (IOException ex) {
                Logger.getLogger(BOUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            doLoadConfig = true;
        }
        
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
    
}
