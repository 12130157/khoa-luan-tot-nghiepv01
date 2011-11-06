/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.access.mapper;

import java.sql.Connection;
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
