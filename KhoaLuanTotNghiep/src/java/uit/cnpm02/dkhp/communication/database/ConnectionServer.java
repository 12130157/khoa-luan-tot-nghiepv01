package uit.cnpm02.dkhp.communication.database;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * @author LocNguyen
 */
public class ConnectionServer {

    public static Connection getConnection() throws NamingException, SQLException {
        DataSource ds = (DataSource) new InitialContext().lookup("KLTNPool");
        Connection con = ds.getConnection();
        return con;
    }
}
