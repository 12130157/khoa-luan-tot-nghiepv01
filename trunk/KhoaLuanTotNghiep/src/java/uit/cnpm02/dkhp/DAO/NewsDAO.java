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
            Connection con = null;
             PreparedStatement statement = null;
             ResultSet rs = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("Update khoaluantotnghiep.bantin set Loai=(Loai+1)%2 where MaTin=").append(id).append("");
            con=getConnection();
            statement = con.prepareStatement(sql.toString());
            statement.execute();
        } catch (Exception ex) {
            throw ex;
       } finally {
            close(rs, statement);
            close(con);
        }
    }
    public void deleteNewsByID(int id) throws SQLException, Exception{
             Connection con = null;
             PreparedStatement statement = null;
             ResultSet rs = null;
             try{
             StringBuffer sql = new StringBuffer();
            sql.append("Delete from khoaluantotnghiep.bantin where MaTin=").append(id).append("");
            con=getConnection();
            statement = con.prepareStatement(sql.toString());
            statement.execute();
            } catch (Exception ex) {
            throw ex;
            } finally {
            close(rs, statement);
            close(con);
        }
            updateNewsId(id);
    }
   public void updateNewsId(int id) throws Exception {
        Connection con = null;
             PreparedStatement statement = null;
             ResultSet rs = null;
             try{
            StringBuffer sql = new StringBuffer();
            sql.append("Update khoaluantotnghiep.bantin set MaTin=MaTin-1 where MaTin>").append(id).append("");
            con=getConnection();    
            statement = con.prepareStatement(sql.toString());
            statement.execute();
         } catch (Exception ex) {
            throw ex;
         } finally {
            close(rs, statement);
            close(con);
        }
    }
    
}
