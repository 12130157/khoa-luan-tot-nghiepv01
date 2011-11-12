/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.access.mapper;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.communication.database.ConnectionServer;

/**
 * 
 * @author LocNguyen
 */
public class MapperDb {

    private static Connection connection;

    public MapperDb() throws Exception {
        try {
            if (connection == null) {
                connection = ConnectionServer.getConnection();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            throw e;
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null) {
                connection = ConnectionServer.getConnection();
            }
        } catch (Exception e) {
            Logger.getLogger(MapperDb.class.getName()).log(Level.SEVERE, "Can not get connection.");
        }
        
        return connection;
    }

    public void closeConnection() throws Exception {
        try {
            getConnection().close();
        } catch (Exception e) {
            System.out.println(e.toString());
            throw e;
        }
    }
}
