package uit.cnpm02.dkhp.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.News;
/**
 *
 * @author LocNguyen
 */
public class NewsDAO extends AbstractJdbcDAO<News, Integer> {
    
    public void updateNewsStatus(int id) throws Exception {
        checkModelWellDefined();
        News t = new News();
        if (t == null) {
            throw new Exception("Cannot initialize the " + News.class.getName()
                    + " class");
        }
        Connection con = null;
        PreparedStatement statement = null;
        String selectQuery = "Update "+t.getTableName()+" set Loai=(Loai+1)%2 where MaTin= ? ";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, id);
            statement.execute();

        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
           close(con);
        }
    }
    public void deleteNewsByID(int id) throws SQLException, Exception{
        checkModelWellDefined();
        News t = new News();
        if (t == null) {
            throw new Exception("Cannot initialize the " + News.class.getName()
                    + " class");
        }
        Connection con = null;
        PreparedStatement statement = null;
        String selectQuery = "Delete from "+t.getTableName()+" where MaTin= ? ";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, id);
            statement.execute();

        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
           close(con);
        }
            
        updateNewsId(id);
    }
   public void updateNewsId(int id) throws Exception {
        checkModelWellDefined();
        News t = new News();
        if (t == null) {
            throw new Exception("Cannot initialize the " + News.class.getName()
                    + " class");
        }
        Connection con = null;
        PreparedStatement statement = null;
        String selectQuery = "Update "+t.getTableName()+" set MaTin=MaTin-1 where MaTin> ? ";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, id);
            statement.execute();

        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
           close(con);
        }
    }
    
}
